package ca.concordia.mccord.discord.commands;

import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import ca.concordia.mccord.utils.ICommand;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;

public abstract class Command implements ICommand<CommandSourceDiscord, ITextComponent> {
    @Override
    public void register(CommandDispatcher<CommandSourceDiscord> dispatcher) {
        dispatcher.register(getParser());
    }

    /**
     * TODO: Code dupe with {@link ca.concordia.mccord.commands.Command#execute(CommandContext, Function)}.
     */
    @Override
    public int execute(CommandContext<CommandSourceDiscord> commandContext,
            Function<CommandContext<CommandSourceDiscord>, ITextComponent> function) {
        try {
            ITextComponent text = function.apply(commandContext);

            if (text != null) {
                sendFeedback(commandContext, text);
            }

            return DiscordBrigadier.SUCCESS;
        } catch (CommandException e) {
            sendErrorMessage(commandContext, e.getComponent());

            return DiscordBrigadier.FAIL;
        } catch (Exception e) {
            return DiscordBrigadier.FAIL;
        }
    }

    /**
     * TODO: Code dupe with {@link #sendErrorMessage(CommandContext, ITextComponent)}.
     * @param commandContext
     * @param text
     */
    protected void sendFeedback(CommandContext<CommandSourceDiscord> commandContext, ITextComponent text) {
        commandContext.getSource().getChannel().get().sendMessage(text.getString()).queue();
    }

    /**
     * TODO: Code dupe with {@link #sendFeedback(CommandContext, ITextComponent)}.
     * @param commandContext
     * @param text
     */
    protected void sendErrorMessage(CommandContext<CommandSourceDiscord> commandContext, ITextComponent text) {
        commandContext.getSource().getChannel().get().sendMessage(text.getString()).queue();
    }
}
