package ca.concordia.mccord.commands;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.chat.ChatManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandMessage extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("msg")
                .then(Commands.argument("channel", StringArgumentType.word())
                        .suggests(CommandSuggestions.ACCESSIBLE_CHANNEL_SUGGEST)
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(context -> execute(context, this::defaultExecute))));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        ServerPlayerEntity player = getSourcePlayer(commandContext).get();

        String channel = StringArgumentType.getString(commandContext, "channel");

        String message = StringArgumentType.getString(commandContext, "message");

        try {
            ChatManager.broadcastDiscord(player, channel, message);
        } catch (AuthenticationException e) {
            throw new CommandException(
                    new StringTextComponent(TextFormatting.RED + "Invalid credentials to send message to Discord. "
                            + "Make sure your account is linked and you have the privilidges."));
        } catch (Exception e) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to send message."));
        }

        return null;
    }
}
