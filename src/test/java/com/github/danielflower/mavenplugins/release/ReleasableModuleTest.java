package com.github.danielflower.mavenplugins.release;

import org.apache.maven.project.MavenProject;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.danielflower.mavenplugins.release.version.Version;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static scaffolding.ReleasableModuleBuilder.aModule;

public class ReleasableModuleTest {
    @Test
    public void getsTheTagFromTheArtifactAndVersion() throws Exception {
        ReleasableModule module = aModule()
            .withArtifactId("my-artifact")
            .withSnapshotVersion("1.0-SNAPSHOT")
            .withBuildNumber(123)
            .build();
        assertThat(module.getTagName(), equalTo("my-artifact-1.0.123"));
    }

    @Test
    public void aReleaseableModuleCanBeCreatedFromAnUnreleasableOne() {
        MavenProject project = new MavenProject();
        project.setArtifactId("some-arty");
        project.setGroupId("some-group");
        Version version = mock(Version.class);
        when(version.getBuildNumber()).thenReturn(12l);
        when(version.getBusinessVersion()).thenReturn("1.2.3");
        when(version.getDevelopmentVersion()).thenReturn("1.2.3-SNAPSHOT");
        ReleasableModule first = new ReleasableModule(
            project, version, "1.2.3.11", "somewhere"
        );
        assertThat(first.willBeReleased(), is(false));

        when(version.getReleaseVersion()).thenReturn("1.2.3.12");
        ReleasableModule changed = first.createReleasableVersion();
        assertThat(changed.getArtifactId(), equalTo("some-arty"));
        assertThat(changed.getBuildNumber(), equalTo(12L));
        assertThat(changed.getGroupId(), equalTo("some-group"));
        assertThat(changed.getNewVersion(), equalTo("1.2.3.12"));
        assertThat(changed.getProject(), is(project));
        assertThat(changed.getRelativePathToModule(), equalTo("somewhere"));
        assertThat(changed.willBeReleased(), is(true));
    }
}
