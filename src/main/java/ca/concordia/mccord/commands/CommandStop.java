package ca.concordia.mccord.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.discord.DiscordManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandStop extends Command {
    @Override
    protected LiteralArgumentBuilder<CommandSource> parser() {
        return Commands.literal(COMMAND_PREFIX).then(Commands.literal("stop")
                .requires(command -> command.hasPermissionLevel(3)).executes(command -> execute(command)));
    }

    @Override
    protected int execute(CommandContext<CommandSource> commandContext) {
        DiscordManager.disconnect();

        sendFeedback(commandContext, new StringTextComponent(TextFormatting.GREEN + "Discord disconnected."));

        return 1;
    }
}
