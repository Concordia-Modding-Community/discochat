package ca.concordia.mccord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.entity.UserManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandChannel extends Command {
    @Override
    protected LiteralArgumentBuilder<CommandSource> parser() {
        return Commands.literal(COMMAND_PREFIX).then(Commands.literal("channel").then(Commands
                .argument("channel", StringArgumentType.word()).executes(commandContext -> execute(commandContext))));
    }

    @Override
    protected int execute(CommandContext<CommandSource> commandContext) {
        Entity entity = commandContext.getSource().getEntity();

        if (entity == null || !(entity instanceof PlayerEntity)) {
            return 0;
        }

        PlayerEntity player = (PlayerEntity) entity;
        String channel = StringArgumentType.getString(commandContext, "channel");

        if (!UserManager.setCurrentChannel(player, channel)) {
            sendErrorMessage(commandContext, new StringTextComponent(TextFormatting.RED + "Unable to find channel."));

            return 0;
        }

        sendFeedback(commandContext, new StringTextComponent(TextFormatting.GREEN + "Switched to " + TextFormatting.BLUE
                + "#" + channel + TextFormatting.GREEN + "."));

        return 1;
    }

}
