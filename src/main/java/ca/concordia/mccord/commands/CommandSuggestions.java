package ca.concordia.mccord.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mojang.brigadier.suggestion.SuggestionProvider;

import ca.concordia.mccord.entity.MCCordUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

public class CommandSuggestions {
    public static final SuggestionProvider<CommandSource> ACCESSIBLE_CHANNEL_SUGGEST = (context, builder) -> {
        List<String> channels = new ArrayList<String>();

        try {
            List<TextChannel> textChannels = MCCordUser.fromMCPlayerEntity(context.getSource().asPlayer()).get()
                    .getAccessibleChannels();

            channels = textChannels.stream().map(channel -> channel.getName()).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return ISuggestionProvider.suggest(channels.toArray(new String[0]), builder);
    };

    // TODO: Prevent code duplicate.
    public static final SuggestionProvider<CommandSource> VISIBLE_CHANNEL_SUGGEST = (context, builder) -> {
        List<String> channels = new ArrayList<String>();

        try {
            List<TextChannel> textChannels = MCCordUser.fromMCPlayerEntity(context.getSource().asPlayer()).get()
                    .getVisibleChannels();

            channels = textChannels.stream().map(channel -> channel.getName()).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return ISuggestionProvider.suggest(channels.toArray(new String[0]), builder);
    };
}
