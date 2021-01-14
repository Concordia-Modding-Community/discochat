package ca.concordia.mccord.commands;

import com.mojang.brigadier.CommandDispatcher;

import ca.concordia.mccord.utils.IMod;
import net.minecraft.command.CommandSource;

public class CommandManager {
    public static final Command[] COMMANDS = new Command[] { new CommandToken(), new CommandStart(),
            new CommandMessage(), new CommandLink(), new CommandStop(), new CommandSwitch() };

    private IMod mod;

    public CommandManager(IMod mod) {
        this.mod = mod;
    }

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        for (Command command : COMMANDS) {
            command.register(mod, dispatcher);
        }
    }
}
