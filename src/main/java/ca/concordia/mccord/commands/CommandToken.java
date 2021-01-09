package ca.concordia.mccord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.Config;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandToken extends Command {
    @Override
    protected LiteralArgumentBuilder<CommandSource> parser() {
        return Commands.literal(COMMAND_PREFIX)
                .then(Commands.literal("token").requires(command -> command.hasPermissionLevel(3))
                        .then(Commands.argument("key", StringArgumentType.string()).executes(command -> execute(command))));
    }

    @Override
    protected int execute(CommandContext<CommandSource> commandContext) {
        String key = StringArgumentType.getString(commandContext, "key");

        Config.DISCORD_API_KEY.set(key);

        sendFeedback(commandContext, new StringTextComponent(TextFormatting.GREEN + "Discord token set."));

        return 1;
    }
}
