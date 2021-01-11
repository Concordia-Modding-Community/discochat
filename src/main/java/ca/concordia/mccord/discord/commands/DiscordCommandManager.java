package ca.concordia.mccord.discord.commands;

import com.mojang.brigadier.CommandDispatcher;

import ca.concordia.b4dis.CommandSourceDiscord;

public class DiscordCommandManager {
    private static final Command[] COMMANDS = new Command[] { new CommandLink(), new CommandDefault() };

    public static void register(CommandDispatcher<CommandSourceDiscord> dispatcher) {
        for (Command command : COMMANDS) {
            command.register(dispatcher);
        }
    }
}
