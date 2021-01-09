package ca.concordia.mccord.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class Command {
    public static String COMMAND_PREFIX = "discord";

    protected abstract LiteralArgumentBuilder<CommandSource> parser();

    protected abstract int execute(CommandContext<CommandSource> commandContext);

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(this.parser());
    }

    public void sendMessage(CommandContext<CommandSource> commandContext, String message) {
        TranslationTextComponent finalText = new TranslationTextComponent("chat.type.announcement",
                commandContext.getSource().getDisplayName(), new StringTextComponent(message));

        Entity entity = commandContext.getSource().getEntity();

        if (entity == null) {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.SYSTEM,
                    Util.DUMMY_UUID);
        } else {
            commandContext.getSource().getServer().getPlayerList().func_232641_a_(finalText, ChatType.CHAT,
                    entity.getUniqueID());
        }
    }

    public void sendFeedback(CommandContext<CommandSource> commandContext, ITextComponent message) {
        commandContext.getSource().sendFeedback(message, true);
    }

    public void sendErrorMessage(CommandContext<CommandSource> commandContext, ITextComponent message) {
        commandContext.getSource().sendErrorMessage(message);
    }
}
