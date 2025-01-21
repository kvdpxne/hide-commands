plugins {
  id("java")
  id("maven-publish")
}

description = ""
group = "me.kvdpxne"
version = "0.1.0"

configurations.all {
  resolutionStrategy.dependencySubstitution {
    //
    val name = "org.spigotmc:spigot"

    // The version of the spigot api that the ProtocolLib plugin uses.
    val version = libraries.versions.spigot.legacy.orNull

    substitute(module("$name:$version"))
      .using(module("$name-api:$version"))
      .because("The artifact named spigot has been replaced by spigot-api.")
  }
}

dependencies {
  compileOnly("org.spigotmc:spigot-api:1.8-R0.1-SNAPSHOT")

  implementation(libraries.notchity)
  implementation(libraries.protocollib.legacy)
}

// The version of java used throughout the project.
val targetJavaVersion = 8

java {
  val javaVersion = JavaVersion.toVersion(targetJavaVersion)

  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion

  if (JavaVersion.current() < javaVersion) {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
  }
}

tasks {

  withType<JavaCompile> {
    options.apply {
      if (10 <= targetJavaVersion || JavaVersion.current().isJava10Compatible) {
        release.set(targetJavaVersion)
      }

      encoding = Charsets.UTF_8.name()
      compilerArgs.add("-Xlint:-options")
    }
  }

  withType<Test> {
    useJUnitPlatform()
  }

  publishing {

  }

  wrapper {
    distributionType = Wrapper.DistributionType.ALL
  }

  processResources {
    val properties = mapOf(
      "description" to rootProject.description,
      "version" to rootProject.version
    )

    inputs.properties(properties)
    filteringCharset = Charsets.UTF_8.name()

    filesMatching(listOf("plugin.yaml", "plugin.yml")) {
      expand(properties)
    }
  }
}
