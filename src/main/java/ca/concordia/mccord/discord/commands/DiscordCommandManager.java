package ca.concordia.mccord.discord.commands;

import com.mojang.brigadier.CommandDispatcher;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;

public class DiscordCommandManager extends AbstractManager {
    private static final Command[] COMMANDS = new Command[] { new CommandLink(), new CommandDefault() };

    public DiscordCommandManager(IMod mod) {
        super(mod);
    }

    public DiscordCommandManager register(CommandDispatcher<CommandSourceDiscord> dispatcher) {
        for (Command command : COMMANDS) {
            command.register(getMod(), dispatcher);
        }

        return this;
    }
}
