import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  application
  idea
  java
  `maven-publish`

  alias(libraries.plugins.shadow)
}

description = "A simple plugin for Minecraft that hides commands for players " +
  "who do not have privileges to use those commands."

group = "me.kvdpxne"
version = "0.1.0"

// The latest LTS version of Java.
val latestJavaVersion = 21

// The Java version in which the project will be compiled.
val targetJavaVersion = 8

application {
  mainClass = "me.kvdpxne.hc.HideCommands"
}

idea {
  project {
    jdkName = latestJavaVersion.toString()
  }

  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}

dependencies {
  compileOnly(libraries.bukkit) {
    sequenceOf(
      "commons-lang",
      "com.googlecode.json-simple",
      "com.google.guava",
      "org.avaje"
    ).forEach {
      exclude(it)
    }
  }

  compileOnly(libraries.protocollib.legacy) {
    // The Spigot module that used ProtocolLib in the legacy version currently
    // does not exist in the official Spigot repository but its exclusion and
    // use of another API will not cause problems during compilation.
    exclude("org.spigotmc")

    // ProtocolLib has compiled these dependencies in itself.
    exclude("com.comphenix.executors")
    exclude("cglib")
  }

  implementation(libraries.notchity)
}

java {
  val javaVersion = JavaVersion.toVersion(targetJavaVersion)

  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion

  if (JavaVersion.current() < javaVersion) {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
  }
}

tasks {

  withType<Wrapper> {
    distributionType = Wrapper.DistributionType.ALL
  }

  withType<JavaCompile> {
    options.apply {
      if (10 <= targetJavaVersion || JavaVersion.current().isJava10Compatible) {
        release = targetJavaVersion
      }

      encoding = Charsets.UTF_8.name()
      compilerArgs.add("-Xlint:-options")
    }
  }

  withType<ProcessResources> {
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

  withType<Test> {
    useJUnitPlatform()
  }

  withType<ShadowJar> {
    archiveFileName = rootProject.run {
      "$name-v$version-bukkit.jar"
    }

    val group = rootProject.group.toString()
    val destination = "$group.hc.dependencies"

    sequenceOf(
      "notchity"
    ).forEach {
      relocate("$group.$it", "$destination.$it")
    }

    mergeServiceFiles()
    minimize()
  }
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {

      pom {
        name = rootProject.name
        description = rootProject.description
        url = "https://github.com/kvdpxne/hide-commands"

        licenses {
          license {
            name = "MIT License"
            url = "https://opensource.org/licenses/MIT"
          }
        }
      }

      from(components["java"])
    }
  }
}
