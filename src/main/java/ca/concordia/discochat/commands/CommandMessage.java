package ca.concordia.discochat.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

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
                .then(Commands.argument("channel", StringArgumentType.word()).suggests(
                        (context, builder) -> getMod().getCommandSuggestions().getAccessibleChannels(context, builder))
                        .then(Commands.argument("message", StringArgumentType.greedyString())
                                .executes(context -> execute(context, this::defaultExecute))));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        ServerPlayerEntity player = getSourcePlayer(commandContext).get();

        String channel = StringArgumentType.getString(commandContext, "channel");

        String message = StringArgumentType.getString(commandContext, "message");

        try {
            getMod().getChatManager().broadcastAll(player, channel, message);
        } catch (Exception e) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to send message. Did you link your account?"));
        }

        return null;
    }
}
