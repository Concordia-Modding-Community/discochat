package ca.concordia.discochat.commands;

import com.mojang.brigadier.CommandDispatcher;

import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
import net.minecraft.command.CommandSource;

public class CommandManager extends AbstractManager {
    public static final Command[] COMMANDS = new Command[] { new CommandToken(), new CommandStart(),
            new CommandMessage(), new CommandLink(), new CommandStop(), new CommandSwitch(), new CommandListen(),
            new CommandUnlink(), new CommandHelp() };

    public CommandManager(IMod mod) {
        super(mod);
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (Command command : COMMANDS) {
            command.register(getMod()).register(dispatcher);
        }
    }
}
