package ca.concordia.mccord.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import ca.concordia.mccord.entity.UserManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class Command {
    public static final String ALL_CHANNELS = "all";
    public static final String NO_CHANNEL = "none";

    public static final SuggestionProvider<CommandSource> CHANNEL_SUGGEST = (context, builder) -> {
        List<String> channels = new ArrayList<String>();

        try {
            List<TextChannel> textChannels = UserManager.getAccessibleTextChannels(context.getSource().asPlayer());

            channels = textChannels.stream().map(channel -> channel.getName()).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return ISuggestionProvider.suggest(channels.toArray(new String[0]), builder);
    };

    public static String COMMAND_PREFIX = "discord";

    protected abstract LiteralArgumentBuilder<CommandSource> parser();

    protected abstract ITextComponent defaultExecute(CommandContext<CommandSource> commandContext)
            throws CommandException;

    public void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(this.parser());
    }

    /**
     * @deprecated Use the sendFeedback, sendErrorMessage methods.
     */
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

    private void sendFeedback(CommandContext<CommandSource> commandContext, ITextComponent message) {
        commandContext.getSource().sendFeedback(message, true);
    }

    private void sendErrorMessage(CommandContext<CommandSource> commandContext, ITextComponent message) {
        commandContext.getSource().sendErrorMessage(message);
    }

    protected Optional<PlayerEntity> getSourcePlayer(CommandContext<CommandSource> commandContext) {
        Optional<Entity> oEntity = Optional.ofNullable(commandContext.getSource().getEntity());

        if (oEntity.isEmpty()) {
            return Optional.empty();
        }

        Entity entity = oEntity.get();

        if (!(entity instanceof PlayerEntity)) {
            return Optional.empty();
        }

        return Optional.of((PlayerEntity) entity);
    }

    protected int execute(CommandContext<CommandSource> commandContext) {
        return execute(commandContext, (context) -> defaultExecute(context));
    }

    protected int execute(CommandContext<CommandSource> commandContext,
            Function<CommandContext<CommandSource>, ITextComponent> function) {
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
}
