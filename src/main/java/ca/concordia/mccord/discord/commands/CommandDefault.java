package ca.concordia.mccord.discord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import ca.concordia.mccord.Config;
import ca.concordia.mccord.discord.DiscordManager;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandDefault extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("default").requires(context -> context.hasRole(Config.DISCORD_ADMIN_ROLE.get()))
                .then(DiscordBrigadier.argument("channel", StringArgumentType.word())
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        String channel = StringArgumentType.getString(context, "channel");

        TextChannel textChannel = DiscordManager.getChannelByName(channel)
                .orElseThrow(() -> new CommandException(new StringTextComponent("Unable to find channel #" + channel)));

        Config.DEFAULT_CHANNEL.set(textChannel.getName());

        return new StringTextComponent("Set default channel to #" + textChannel.getName());
    }
}