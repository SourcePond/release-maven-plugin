/*Copyright (C) 2016 Roland Hauser, <sourcepond@gmail.com>
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package ch.sourcepond.maven.release.pom;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.Model;
import org.apache.maven.project.MavenProject;

import ch.sourcepond.maven.release.reactor.Reactor;

@Named
@Singleton
class ContextFactory {

	Context newContext(final Reactor reactor, final MavenProject project, final Model model,
			final boolean incrementSnapshotVersionAfterRelease) {
		return new Context(reactor, project, model, incrementSnapshotVersionAfterRelease);
	}
}
