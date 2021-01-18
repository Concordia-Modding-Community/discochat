package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandNotification extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("notification")
                .requires(context -> context.hasRole(getMod().getConfigManager().getDiscordAdminRole()))
                .then(DiscordBrigadier.argument("channel", StringArgumentType.word())
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        String channel = StringArgumentType.getString(context, "channel");

        TextChannel textChannel = getMod().getDiscordManager().getChannelByName(channel)
                .orElseThrow(() -> new CommandException(
                        new StringTextComponent("Unable to set notification channel. Does the channel exist?")));

        getMod().getConfigManager().setNotificationChannel(textChannel.getName());

        return new StringTextComponent("Set notification channel to " + textChannel.getAsMention() + ".");
    }
}
