package ca.concordia.discochat.discord.commands;

import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import net.dv8tion.jda.api.EmbedBuilder;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;

public class CommandHelp extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("help").executes(context -> execute(context, this::defaultExecute));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        CommandDispatcher<CommandSourceDiscord> dispatcher = getMod().getDiscordManager().getDispatcher();

        Map<CommandNode<CommandSourceDiscord>, String> map = dispatcher.getSmartUsage(dispatcher.getRoot(),
                context.getSource());

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("DiscoChat Help");

        String discordPrefix = getMod().getConfigManager().getDiscordCommandPrefix();

        String commands = "";

        for (String command : map.values()) {
            commands += discordPrefix + command + "\n";
        }

        embed.setDescription(commands);

        context.getSource().getTextChannel().sendMessage(embed.build()).queue();

        return null;
    }
}
