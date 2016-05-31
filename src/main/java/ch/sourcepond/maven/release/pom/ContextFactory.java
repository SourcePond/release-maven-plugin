package ch.sourcepond.maven.release.pom;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.component.annotations.Component;

import ch.sourcepond.maven.release.reactor.Reactor;

@Component(role = ContextFactory.class)
class ContextFactory {

	Context newContext(final Reactor reactor, final MavenProject project, final Model model,
			final boolean incrementSnapshotVersionAfterRelease) {
		return new Context(reactor, project, model, incrementSnapshotVersionAfterRelease);
	}
}
