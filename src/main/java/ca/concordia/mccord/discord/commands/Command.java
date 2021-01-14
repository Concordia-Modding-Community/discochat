package ca.concordia.mccord.discord.commands;

import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.mccord.utils.ICommand;
import ca.concordia.mccord.utils.IMod;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;

public abstract class Command implements ICommand<CommandSourceDiscord, ITextComponent> {
    private IMod mod;

    @Override
    public void register(IMod mod, CommandDispatcher<CommandSourceDiscord> dispatcher) {
        this.mod = mod;
        dispatcher.register(getParser());
    }

    @Override
    public IMod getMod() {
        return mod;
    }

    /**
     * TODO: Code dupe with
     * {@link ca.concordia.mccord.commands.Command#execute(CommandContext, Function)}.
     */
    @Override
    public int execute(CommandContext<CommandSourceDiscord> commandContext,
            Function<CommandContext<CommandSourceDiscord>, ITextComponent> function) {
        try {
            ITextComponent text = function.apply(commandContext);

            if (text != null) {
                sendFeedback(commandContext, text);
            }

            return 1;
        } catch (CommandException e) {
            sendErrorMessage(commandContext, e.getComponent());

            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * TODO: Code dupe with
     * {@link #sendErrorMessage(CommandContext, ITextComponent)}.
     * 
     * @param commandContext
     * @param text
     */
    protected void sendFeedback(CommandContext<CommandSourceDiscord> commandContext, ITextComponent text) {
        commandContext.getSource().getTextChannel().sendMessage(text.getString()).queue();
    }

    /**
     * TODO: Code dupe with {@link #sendFeedback(CommandContext, ITextComponent)}.
     * 
     * @param commandContext
     * @param text
     */
    protected void sendErrorMessage(CommandContext<CommandSourceDiscord> commandContext, ITextComponent text) {
        commandContext.getSource().getTextChannel().sendMessage(text.getString()).queue();
    }
}
