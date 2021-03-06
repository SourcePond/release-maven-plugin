package ch.sourcepond.integrationtest;

import static ch.sourcepond.integrationtest.utils.ExactCountMatcher.oneOf;
import static ch.sourcepond.integrationtest.utils.ExactCountMatcher.threeOf;
import static ch.sourcepond.integrationtest.utils.ExactCountMatcher.twoOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.StoredConfig;
import org.junit.Assert;
import org.junit.Test;

import ch.sourcepond.integrationtest.utils.MavenExecutionException;
import ch.sourcepond.integrationtest.utils.MvnRunner;
import ch.sourcepond.integrationtest.utils.Photocopier;
import ch.sourcepond.integrationtest.utils.TestProject;

public class GitRelatedTest extends E2ETest {

	@Test
	public void ifTheReleaseIsRunFromANonGitRepoThenAnErrorIsClearlyDisplayed() throws IOException {
		final File projectRoot = Photocopier.copyTestProjectToTemporaryLocation("single-module");
		TestProject.performPomSubstitution(projectRoot);
		try {
			new MvnRunner().runMaven(projectRoot, "releaser:release");
			Assert.fail("Should have failed");
		} catch (final MavenExecutionException e) {
			assertThat(e.output, threeOf(containsString("Releases can only be performed from Git repositories.")));
			assertThat(e.output, oneOf(containsString(projectRoot.getCanonicalPath() + " is not a Git repository.")));
		}
	}

	@Test
	public void ifThereIsNoScmInfoAndNoRemoteBranchThenAnErrorIsThrown()
			throws GitAPIException, IOException, InterruptedException {
		final TestProject testProject = TestProject.singleModuleProject();

		final StoredConfig config = testProject.local.getRepository().getConfig();
		config.unsetSection("remote", "origin");
		config.save();

		try {
			testProject.mvnRelease("1");
			Assert.fail("Should have failed");
		} catch (final MavenExecutionException e) {
			assertThat(e.output, oneOf(containsString("[ERROR] Remote tags could not be listed!")));
		}
	}

	@Test
	public void ifTheScmIsSpecifiedButIsNotGitThenThisIsThrown()
			throws GitAPIException, IOException, InterruptedException {
		final TestProject testProject = TestProject.moduleWithScmTag();
		final File pom = new File(testProject.localDir, "pom.xml");
		String xml = FileUtils.readFileToString(pom, "UTF-8");
		xml = xml.replace("scm:git:", "scm:svn:");
		FileUtils.writeStringToFile(pom, xml, "UTF-8");
		testProject.local.add().addFilepattern("pom.xml").call();
		testProject.local.commit().setMessage("Changing pom for test").call();

		try {
			testProject.mvnRelease("1");
			Assert.fail("Should have failed");
		} catch (final MavenExecutionException e) {
			assertThat(e.output,
					twoOf(containsString("Cannot run the release plugin with a non-Git version control system")));
			assertThat(e.output, oneOf(containsString("The value in your scm tag is scm:svn:")));
		}
	}

	@Test
	public void ifThereIsNoRemoteButTheScmDetailsArePresentThenThisIsUsed()
			throws GitAPIException, IOException, InterruptedException {
		final TestProject testProject = TestProject.moduleWithScmTag();

		final StoredConfig config = testProject.local.getRepository().getConfig();
		config.unsetSection("remote", "origin");
		config.save();

		testProject.mvnRelease("1");
	}

}