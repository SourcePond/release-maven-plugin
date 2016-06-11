package ch.sourcepond.maven.release.version;

import static java.lang.Long.valueOf;

import org.apache.maven.project.MavenProject;

/**
 *
 */
final class DefaultVersionBuilder implements VersionBuilder {
	static final String SNAPSHOT_EXTENSION = "-SNAPSHOT";
	private final BuildNumberFinder finder;
	private final ChangeDetectorFactory detectorFactory;
	private MavenProject project;
	private boolean userLastNumber;
	private Long buildNumber;
	private String relativePathToModuleOrNull;
	private String changedDependencyOrNull;

	DefaultVersionBuilder(final BuildNumberFinder finder, final ChangeDetectorFactory detectorFactory) {
		this.finder = finder;
		this.detectorFactory = detectorFactory;
	}

	@Override
	public VersionBuilder setProject(final MavenProject project) {
		this.project = project;
		return this;
	}

	@Override
	public VersionBuilder setUseLastNumber(final boolean useLastNumber) {
		this.userLastNumber = useLastNumber;
		return this;
	}

	@Override
	public VersionBuilder setBuildNumber(final Long buildNumberOrNull) {
		this.buildNumber = buildNumberOrNull;
		return this;
	}

	@Override
	public VersionBuilder setRelativePath(final String relativePathToModuleOrNull) {
		this.relativePathToModuleOrNull = relativePathToModuleOrNull;
		return this;
	}

	@Override
	public VersionBuilder setChangedDependency(final String changedDependencyOrNull) {
		this.changedDependencyOrNull = changedDependencyOrNull;
		return this;
	}

	@Override
	public Version build() throws VersionException {
		String businessVersion = project.getVersion().replace(SNAPSHOT_EXTENSION, "");
		long actualBuildNumber;

		if (userLastNumber) {
			final int idx = businessVersion.lastIndexOf('.');
			actualBuildNumber = valueOf(businessVersion.substring(idx + 1));
			businessVersion = businessVersion.substring(0, idx);

			if (buildNumber != null && buildNumber > actualBuildNumber) {
				actualBuildNumber = buildNumber;
			}
		} else if (buildNumber != null) {
			actualBuildNumber = buildNumber;
		} else {
			actualBuildNumber = finder.findBuildNumber(project, businessVersion);
		}

		final String releaseVersion = businessVersion + "." + actualBuildNumber;
		final DefaultVersion version = new DefaultVersion();
		version.setReleaseVersion(releaseVersion);
		version.setBuildNumber(actualBuildNumber);
		version.setBusinessVersion(businessVersion);
		version.setEquivalentVersion(detectorFactory.newDetector().setProject(project).setBuildNumber(actualBuildNumber)
				.setChangedDependency(changedDependencyOrNull).setRelativePathToModule(relativePathToModuleOrNull)
				.setBusinessVersion(businessVersion).equivalentVersionOrNull());
		return version;
	}

}