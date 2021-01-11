package ca.concordia.mccord.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.entity.UserManager;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandLink extends Command {
        @Override
        public LiteralArgumentBuilder<CommandSource> getParser() {
                return Commands.literal("link").then(Commands.argument("username", StringArgumentType.word()).then(
                                Commands.argument("discriminator", IntegerArgumentType.integer(0, 10000)).executes(
                                                commandContext -> execute(commandContext, this::defaultExecute))))
                                .then(Commands.argument("discordUUID", StringArgumentType.word()).executes(
                                                commandContext -> execute(commandContext, this::uuidExecute)));
        }

        private ITextComponent commonExecute(CommandContext<CommandSource> commandContext, User user)
                        throws CommandException {
                PlayerEntity playerEntity = getSourcePlayer(commandContext).get();

                String mcUUID = playerEntity.getUniqueID().toString();

                String discordUUID = user.getId();

                UserManager.link(mcUUID, discordUUID);

                user.openPrivateChannel()
                                .flatMap(channel -> channel.sendMessage(
                                                "MC Account `" + playerEntity.getName().getString() + "` Linked."))
                                .queue();

                return new StringTextComponent(TextFormatting.GREEN + "Discord Account Linked.");
        }

        protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
                String username = StringArgumentType.getString(commandContext, "username");

                int intDiscriminator = IntegerArgumentType.getInteger(commandContext, "discriminator");

                String discriminator = String.format("%04d", intDiscriminator);

                User user = UserManager.fromDiscordTag(username, discriminator).orElseThrow(() -> new CommandException(
                                new StringTextComponent(TextFormatting.RED + "Unable to find Discord account.")));

                return commonExecute(commandContext, user);
        }

        protected ITextComponent uuidExecute(CommandContext<CommandSource> commandContext) throws CommandException {
                String uuid = StringArgumentType.getString(commandContext, "discordUUID");

                User user = UserManager.fromDiscordUUID(uuid).orElseThrow(() -> new CommandException(
                                new StringTextComponent(TextFormatting.RED + "Unable to find Discord account.")));

                return commonExecute(commandContext, user);
        }
}
