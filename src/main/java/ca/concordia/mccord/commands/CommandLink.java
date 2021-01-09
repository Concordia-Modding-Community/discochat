package ca.concordia.mccord.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.entity.UserManager;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandLink extends Command {
    @Override
    protected LiteralArgumentBuilder<CommandSource> parser() {
        return Commands.literal(COMMAND_PREFIX)
                .then(Commands.literal("link")
                        .then(Commands.argument("username", StringArgumentType.word())
                                .then(Commands.argument("discriminator", IntegerArgumentType.integer(0, 10000))
                                        .executes(commandContext -> execute(commandContext))))
                        .then(Commands.argument("discordUUID", StringArgumentType.word())
                                .executes(commandContext -> executeUUID(commandContext))));
    }

    /**
     * TODO: Check why method is unstable.
     */
    @Override
    protected int execute(CommandContext<CommandSource> commandContext) {
        Entity entity = commandContext.getSource().getEntity();

        if (entity == null || !(entity instanceof PlayerEntity)) {
            return 0;
        }

        PlayerEntity playerEntity = (PlayerEntity) entity;

        String mcUUID = playerEntity.getUniqueID().toString();

        String username = StringArgumentType.getString(commandContext, "username");

        int intDiscriminator = IntegerArgumentType.getInteger(commandContext, "discriminator");

        String discriminator = String.format("%04d", intDiscriminator);

        User user = UserManager.fromDiscordTag(username, discriminator);

        if (user == null) {
            sendErrorMessage(commandContext,
                    new StringTextComponent(TextFormatting.RED + "Unable to find Discord account."));

            return 0;
        }

        String discordUUID = user.getId();

        UserManager.linkUsers(discordUUID, mcUUID);

        sendFeedback(commandContext, new StringTextComponent(TextFormatting.GREEN + "Discord linked."));

        return 1;
    }

    protected int executeUUID(CommandContext<CommandSource> commandContext) {
        Entity entity = commandContext.getSource().getEntity();

        if (entity == null || !(entity instanceof PlayerEntity)) {
            return 0;
        }

        PlayerEntity playerEntity = (PlayerEntity) entity;

        String mcUUID = playerEntity.getUniqueID().toString();

        String uuid = StringArgumentType.getString(commandContext, "discordUUID");

        User user = UserManager.fromDiscordUUID(uuid);

        if (user == null) {
            sendErrorMessage(commandContext,
                    new StringTextComponent(TextFormatting.RED + "Unable to find Discord account."));

            return 0;
        }

        String discordUUID = user.getId();

        UserManager.linkUsers(discordUUID, mcUUID);

        sendFeedback(commandContext, new StringTextComponent(TextFormatting.GREEN + "Discord linked."));

        return 1;
    }
}
