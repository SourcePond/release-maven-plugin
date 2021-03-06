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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.logging.Log;

import ch.sourcepond.maven.release.scm.SCMRepository;

@Named
@Singleton
class DefaultChangeSetFactory {
	private final Log log;
	private final SCMRepository repository;
	private final MavenXpp3Writer writer;

	@Inject
	DefaultChangeSetFactory(final Log pLog, final SCMRepository pRepository, final MavenXpp3Writer pWriter) {
		log = pLog;
		repository = pRepository;
		writer = pWriter;
	}

	DefaultChangeSet newChangeSet() {
		return new DefaultChangeSet(log, repository, writer);
	}
}
