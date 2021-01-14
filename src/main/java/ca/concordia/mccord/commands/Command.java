package ca.concordia.mccord.commands;

import java.util.Optional;
import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.entity.MCCordUser;
import ca.concordia.mccord.utils.ICommand;
import ca.concordia.mccord.utils.IMod;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class Command implements ICommand<CommandSource, ITextComponent> {
    private IMod mod;

    @Override
    public void register(IMod mod, CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal(Config.MC_COMMAND_PREFIX.get()).then(getParser()));
    }

    @Override
    public IMod getMod() {
        return mod;
    }

    /**
     * TODO: Code dupe with {@link ca.concordia.mccord.discord.commands.Command#execute(CommandContext, Function)}.
     */
    @Override
    public int execute(CommandContext<CommandSource> commandContext,
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

    public Optional<MCCordUser> getSourceMCCordUser(CommandContext<CommandSource> commandContext) {
        Optional<ServerPlayerEntity> playerEntity = getSourcePlayer(commandContext);

        return MCCordUser.fromMCPlayerEntity(getMod(), playerEntity);
    }

    public Optional<ServerPlayerEntity> getSourcePlayer(CommandContext<CommandSource> commandContext) {
        Optional<Entity> oEntity = Optional.ofNullable(commandContext.getSource().getEntity());

        if (oEntity.isEmpty()) {
            return Optional.empty();
        }

        Entity entity = oEntity.get();

        if (!(entity instanceof ServerPlayerEntity)) {
            return Optional.empty();
        }

        return Optional.of((ServerPlayerEntity) entity);
    }
}
