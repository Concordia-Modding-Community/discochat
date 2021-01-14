package ca.concordia.mccord.utils;

import ca.concordia.mccord.chat.ChatManager;
import ca.concordia.mccord.commands.CommandSuggestions;
import ca.concordia.mccord.data.DataManager;
import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.entity.UserManager;
import ca.concordia.mccord.server.ConfigManager;
import ca.concordia.mccord.server.ServerManager;

public interface IMod {
    ChatManager getChatManager();

    DataManager getDataManager();

    UserManager getUserManager();

    DiscordManager getDiscordManager();

    ServerManager getServerManager();

    CommandSuggestions getCommandSuggestions();

    ConfigManager getConfigManager();
}
