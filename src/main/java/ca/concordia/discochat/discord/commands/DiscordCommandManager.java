package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.CommandDispatcher;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;

public class DiscordCommandManager extends AbstractManager {
    private static final Command[] COMMANDS = new Command[] { new CommandLink(), new CommandDefault(),
            new CommandMCTextFormat(), new CommandDiscordTextFormat(), new CommandColor(), new CommandData(),
            new CommandHelp() };

    public static Command[] getCommands() {
        return COMMANDS;
    }

    public DiscordCommandManager(IMod mod) {
        super(mod);
    }

    public DiscordCommandManager register(CommandDispatcher<CommandSourceDiscord> dispatcher) {
        for (Command command : COMMANDS) {
            command.register(getMod()).register(dispatcher);
        }

        return this;
    }
}
