package ca.concordia.mccord.commands;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.chat.ChatManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
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
    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        PlayerEntity player = getSourcePlayer(commandContext).get();

        String channel = StringArgumentType.getString(commandContext, "channel");
        String message = StringArgumentType.getString(commandContext, "message");

        try {
            ChatManager.discordChannelMessage(player, channel, message);
        } catch(AuthenticationException e) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to send message to channel."));
        } catch(Exception e) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to send message."));
        }

        return null;
    }

}
