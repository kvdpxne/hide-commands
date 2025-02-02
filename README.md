## Introduction

**Hide Commands** is a simple and lightweight
[Minecraft](https://www.minecraft.net/) plugin designed to enhance server
security and privacy by hiding commands from players who lack the necessary
permissions to use them. With this plugin, commands are concealed in such a way
that unauthorized players cannot determine whether a specific command exists or
not.

### An example of how it works

If a player who does not have privileges to use the `/op` command, uses this
command then instead of the information about the lack of privileges to execute
this command, a message will be displayed saying that the `/op` command does not
exist.

On the other hand, if the player uses a command that does not exist (for
example, `/unexists-command`) he will also receive the same message informing
that the command does not exist.

Receiving two identical messages in the case of an existing and non-existing
command will result in the player not being able to determine whether the
command actually exists or not, and if the player knows the plugin-specific
commands he will also not be able to determine whether the plugin exists on the
server or not.

## Installation

### Requirements

- **Minecraft[^1]**: Versions 1.7.2 through 1.16.5
- **Server platform**: [Bukkit](https://dev.bukkit.org/) 1.7.2+ or
  [Spigot](https://www.spigotmc.org/)/[Paper](https://papermc.io/) 1.7.2+
- **Java[^2]**: Version 8 (1.8) or higher.
- **Others**: [ProtocolLib](https://github.com/dmulloy2/ProtocolLib)[^3] plugin

### Step by step

1. **Download the plugin:**

* Go to the [release](https://github.com/kvdpxne/hide-commands/releases)
  section on GitHub.
* Download the latest version of the `.jar` plugin file (
  e.g. `hide-commands-v0.1.0.jar`)

2. **Prepare the server:**

* Make sure you have a working [Minecraft](https://www.minecraft.net/) game
  server based on the platform [Bukkit](https://dev.bukkit.org/),
  [Spigot](https://www.spigotmc.org/) or [Paper](https://papermc.io/)

3. **Place the file on the server:**

* Open your [Minecraft](https://www.minecraft.net/) game server directory.
* Go to the `plugins`[^4] directory.
* Copy the downloaded `.jar` file to the `plugins`[^4] directory.

4. **Start the server:**

* Start or restart the [Minecraft](https://www.minecraft.net/) game server,
  to load the plugin.
* Check the server console to make sure the plugin was loaded without errors.

5. **Configuration (optional):**

* After starting the server, find the `plugins/hide-commands` directory in your
  server directory.
* Edit the `settings.properties` configuration file to customize the plugin
  settings to your individual needs.

## License

This project is licensed under the **MIT License**. This means you are free to
use, modify, and distribute the code in both private and commercial projects, as
long as you include the original license in any substantial portions of the
code.

For more details, please refer to the full license text in the
[LICENSE](https://github.com/kvdpxne/dico/blob/master/LICENSE) file.

[^1]: The plugin has been tested from version 1.7.2 up to version 1.16.5, but
this does not prove that the plugin will malfunction on versions below 1.7.2 or
above 1.16.5.

[^2]: The plugin was compiled in Java version 8 (1.8) this means that it will
work on any newer version of Java but may (not necessarily) not work in older
versions of Java

[^3]: The plugin uses the ProtocolLib library to ensure compatibility between
different versions of Minecraft.

[^4]: Depending on your settings, the `plugins` directory may be named
differently, the `plugins` directory name is the default directory name that
stores all plugins on the server.