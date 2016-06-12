package ch.sourcepond.maven.release.config;

import java.io.File;
import java.util.List;

public interface Configuration {
	/**
	 * <p>
	 * The build number to use in the release version. Given a snapshot version
	 * of "1.0-SNAPSHOT" and a buildNumber value of "2", the actual released
	 * version will be "1.0.2".
	 * </p>
	 * <p>
	 * By default, the plugin will automatically find a suitable build number.
	 * It will start at version 0 and increment this with each release.
	 * </p>
	 * <p>
	 * This can be specified using a command line parameter ("-DbuildNumber=2")
	 * or in this plugin's configuration.
	 * </p>
	 */
	String BUILD_NUMBER = "buildNumber";

	/**
	 * The modules to release, or no value to to release the project from the
	 * root pom, which is the default. The selected module plus any other
	 * modules it needs will be built and released also. When run from the
	 * command line, this can be a comma-separated list of module names.
	 */
	String MODULES_TO_RELEASE = "modulesToRelease";

	/**
	 * A module to force release on, even if no changes has been detected.
	 */
	String MODULES_TO_FORCE_RELEASE = "modulesToForceRelease";

	String DISABLE_SSH_AGENT = "disableSshAgent";

	/**
	 * Specifies whether the release build should run with the "-X" switch.
	 */
	String DEBUG_ENABLED = "debugEnabled";

	/**
	 * If set, the identityFile and passphrase will be read from the Maven
	 * settings file.
	 */
	String SERVER_ID = "serverId";

	/**
	 * If set, this file will be used to specify the known_hosts. This will
	 * override any default value.
	 */
	String KNOWN_HOSTS = "knownHosts";

	/**
	 * Specifies the private key to be used.
	 */
	String PRIVATE_KEY = "privateKey";

	/**
	 * Specifies the passphrase to be used with the identityFile specified.
	 */
	String PASSPHRASE = "passphrase";

	/**
	 * <p>
	 * The goals to run against the project during a release. By default this is
	 * "deploy" which means the release version of your artifact will be tested
	 * and deployed.
	 * </p>
	 * <p>
	 * You can specify more goals and maven options. For example if you want to
	 * perform a clean, build a maven site, and then deploys it, use:
	 * </p>
	 * 
	 * <pre>
	 * {@code
	 * <releaseGoals>
	 *     <releaseGoal>clean</releaseGoal>
	 *     <releaseGoal>site</releaseGoal>
	 *     <releaseGoal>deploy</releaseGoal>
	 * </releaseGoals>
	 * }
	 * </pre>
	 */
	String GOALS = "goals";

	/**
	 * <p>
	 * Profiles to activate during the release.
	 * </p>
	 * <p>
	 * Note that if any profiles are activated during the build using the `-P`
	 * or `--activate-profiles` will also be activated during release. This
	 * gives two options for running releases: either configure it in the plugin
	 * configuration, or activate profiles from the command line.
	 * </p>
	 * 
	 * @since 1.0.1
	 */
	String RELEASE_PROFILES = "releaseProfiles";

	String INCREMENT_SNAPSHOT_VERSION_AFTER_RELEASE = "incrementSnapshotVersionAfterRelease";

	/**
	 * If true then tests will not be run during a release. This is the same as
	 * adding -DskipTests=true to the release goals.
	 */
	String SKIP_TESTS = "skipTests";

	/**
	 * Specifies a custom, user specific Maven settings file to be used during
	 * the release build.
	 */
	String USER_SETTINGS = "userSettings";

	/**
	 * Specifies a custom, global Maven settings file to be used during the
	 * release build.
	 */
	String GLOBAL_SETTINGS = "globalSettings";

	/**
	 * Specifies a custom directory which should be used as local Maven
	 * repository.
	 */
	String LOCAL_MAVEN_REPO = "localMavenRepo";

	Long getBuildNumberOrNull();

	List<String> getModulesToRelease();

	List<String> getModulesToForceRelease();

	boolean isDisableSshAgent();

	boolean isDebugEnabled();

	String getServerId();

	String getKnownHosts();

	String getPrivateKey();

	String getPassphrase();

	List<String> getGoals();

	List<String> getReleaseProfiles();

	boolean isIncrementSnapshotVersionAfterRelease();

	boolean isSkipTests();

	File getUserSettings();

	File getGlobalSettings();

	File getLocalMavenRepo();

}