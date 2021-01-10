package ca.concordia.mccord.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class CommandManager {
    public static final Command[] COMMANDS = new Command[] { new CommandToken(), new CommandStart(),
            new CommandMessage(), new CommandLink(), new CommandStop(), new CommandSwitch() };

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        for (Command command : COMMANDS) {
            command.register(dispatcher);
        }
    }
}
