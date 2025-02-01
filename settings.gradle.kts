pluginManagement {

  repositories {
    gradlePluginPortal()

    mavenCentral()
    mavenLocal()
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {

  versionCatalogs {
    val fileName = "libraries"
    create(fileName) {
      from(files("gradle/$fileName.versions.toml"))
    }
  }

  repositories {
    mavenCentral()
    mavenLocal()

    maven("https://jitpack.io/") {
      name = "JitPack Service"

      content {
        includeGroupAndSubgroups("com.github.kvdpxne")
      }
    }

    // Cloudflare's application serving static files converted to a Maven
    // repository containing clean Bukkit files (API) and along with NMS from
    // version 1.7.2 up to the latest.
    maven("https://ivdcecsyltzbz.pages.dev/") {
      name = "Cloudflare Static Files"
    }

    // The Maven repository for the ProtocolLibrary plugin.
    maven("https://repo.dmulloy2.net/repository/public/") {
      name = "Dmulloy2 Repository"
    }
  }

  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}

rootProject.name = "hide-commands"
