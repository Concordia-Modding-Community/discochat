# DiscoChat
Minecraft 1.16 Discord Integration

![Repository Image](./images/repo_image.png)

```
We want to thank the Concordia University Part-time Faculty Association for the Professional Development Grant that allows us to develop/contribute to the open source community. 
```

## Features

- Bidirectional communication between Minecraft and Discord.
- Automatic account linking and permissions.
- Channel based communication on Minecraft.
- Highly configurable plugin.

## Install

To completely install the repo for development:

```bash
git clone https://github.com/Concordia-Modding-Community/discochat
git submodule init
git submodule update
./gradlew genVsCodeRuns
```

Add a new Discord bot to your Discord server as Administrator (8). Don't forget to activate the `Privileged Gateway Intents`.

In game chat, don't forget to put your Discord bot token:

```bash
/discord token BOT_TOKEN
```

And link your discord account:

```bash
/discord link DISCORD_NAME DISCORD_#
```