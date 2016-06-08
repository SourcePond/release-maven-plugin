package ch.sourcepond.maven.release.pom;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Writer which stores all passed data into a buffer. After all data has been
 * written and {@link #close()} has been called, all <em>&lt;version&gt;</em>
 * tags of the original file will be updated with the data actually written. The
 * format of of the original file will be preserved.
 *
 */
final class VersionTransferWriter extends StringWriter {
	static final Pattern VERSION_PATTERN = Pattern.compile("<version>\\s*(.*?)\\s*<\\/version>");
	static final int VERSION_VALUE = 1;
	private final StringBuilder original = new StringBuilder();
	private final File file;

	/**
	 * Constructs a new instance of this class. During construction, the file
	 * specified will be read and stored in a buffer.
	 * 
	 * @param file
	 *            File to be read, must not be {@code null}
	 * @throws IOException
	 */
	public VersionTransferWriter(final File file) throws IOException {
		notNull(file, "File specified is null");
		this.file = file;
		final char[] buffer = new char[1024];
		try (final Reader rd = new BufferedReader(new FileReader(file))) {
			int readChars = rd.read(buffer);
			while (readChars != -1) {
				original.append(buffer, 0, readChars);
				readChars = rd.read(buffer);
			}
		}
	}

	private boolean find(final Matcher updated, final Matcher original, final int originalIdx) throws IOException {
		final boolean findUpdated = updated.find();
		final boolean findOriginal = original.find(originalIdx);

		if (findOriginal && !findUpdated || !findOriginal && findUpdated) {
			throw new IOException("File cannot be updated because it has incompatbility been changed!");
		}

		return findOriginal;
	}

	@Override
	public void close() throws IOException {
		final Matcher matcher = VERSION_PATTERN.matcher(toString());
		final Matcher originalMatcher = VERSION_PATTERN.matcher(original);
		int originalIdx = 0;
		int startIdx = 0;

		while (find(matcher, originalMatcher, originalIdx)) {
			final String newVersion = matcher.group(VERSION_VALUE);
			startIdx = originalMatcher.start(VERSION_VALUE);
			original.replace(startIdx, originalMatcher.end(VERSION_VALUE), newVersion);
			originalIdx = startIdx + newVersion.length();
		}

		try (final Writer writer = new BufferedWriter(new FileWriter(file))) {
			writer.write(original.toString());
		}
	}
}