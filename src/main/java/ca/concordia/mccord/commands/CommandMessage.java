package ca.concordia.mccord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.chat.ChatManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage extends Command {
    @Override
    protected LiteralArgumentBuilder<CommandSource> parser() {
        return Commands.literal(COMMAND_PREFIX)
                .then(Commands.literal("msg")
                        .then(Commands.argument("channel", StringArgumentType.word())
                                .then(Commands.argument("message", StringArgumentType.greedyString())
                                        .executes(commandContext -> execute(commandContext)))));
    }

    @Override
    protected int execute(CommandContext<CommandSource> commandContext) {
        Entity entity = commandContext.getSource().getEntity();

        if (entity == null || !(entity instanceof PlayerEntity)) {
            return 0;
        }

        PlayerEntity player = (PlayerEntity) entity;
        String channel = StringArgumentType.getString(commandContext, "channel");
        String message = StringArgumentType.getString(commandContext, "message");

        if(!ChatManager.discordChannelMessage(player, channel, message)) {
            sendErrorMessage(commandContext, new StringTextComponent(TextFormatting.RED + "Unable to send message."));
        }

        return 1;
    }

}
