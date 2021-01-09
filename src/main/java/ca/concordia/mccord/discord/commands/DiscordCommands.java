package ca.concordia.mccord.discord.commands;

import java.util.HashMap;
import java.util.List;

public class DiscordCommands {
    private static final Command[] COMMANDS = new Command[] { new CommandJoin() };

    private static HashMap<String, Command> COMMAND_MAPPING = new HashMap<String, Command>();

    static {
        for(Command command : COMMANDS) {
            COMMAND_MAPPING.put(command.commandKey(), command);
        }
    }

    /**
     * Gets a command by name.
     * @param tokens Input tokens.
     * @return Command instance.
     */
    public static Command get(List<String> tokens) {
        String commandWithPrefix = tokens.get(0);

        String command = commandWithPrefix.substring(Command.COMMAND_PREFIX.length()).strip();

        if (!COMMAND_MAPPING.containsKey(command)) {
            return null;
        }

        return COMMAND_MAPPING.get(command);
    }
}
