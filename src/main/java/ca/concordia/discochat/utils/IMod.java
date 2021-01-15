package ca.concordia.discochat.utils;

import ca.concordia.discochat.chat.ChatManager;
import ca.concordia.discochat.commands.CommandSuggestions;
import ca.concordia.discochat.data.DataManager;
import ca.concordia.discochat.discord.DiscordManager;
import ca.concordia.discochat.entity.UserManager;
import ca.concordia.discochat.server.ConfigManager;
import ca.concordia.discochat.server.ServerManager;

public interface IMod {
    ChatManager getChatManager();

    DataManager getDataManager();

    UserManager getUserManager();

    DiscordManager getDiscordManager();

    ServerManager getServerManager();

    CommandSuggestions getCommandSuggestions();

    ConfigManager getConfigManager();
}
