package ca.concordia.discochat.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.IMod;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

public class CommandSuggestions {
    private IMod mod;

    public CommandSuggestions(IMod mod) {
        this.mod = mod;
    }

    public IMod getMod() {
        return mod;
    }

    // TODO: Prevent code duplicate.
    public CompletableFuture<Suggestions> getAccessibleChannels(CommandContext<CommandSource> context,
            SuggestionsBuilder builder) {
        List<String> channels = new ArrayList<String>();

        try {
            List<TextChannel> textChannels = ModUser.fromMCPlayerEntity(getMod(), context.getSource().asPlayer()).get()
                    .getAccessibleChannels();

            channels = textChannels.stream().map(channel -> channel.getName()).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return ISuggestionProvider.suggest(channels.toArray(new String[0]), builder);
    }

    // TODO: Prevent code duplicate.
    public CompletableFuture<Suggestions> getVisibleChannels(CommandContext<CommandSource> context,
            SuggestionsBuilder builder) {
        List<String> channels = new ArrayList<String>();

        try {
            List<TextChannel> textChannels = ModUser.fromMCPlayerEntity(getMod(), context.getSource().asPlayer()).get()
                    .getVisibleChannels();

            channels = textChannels.stream().map(channel -> channel.getName()).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return ISuggestionProvider.suggest(channels.toArray(new String[0]), builder);
    }

    // TODO: Prevent code duplicate.
    public CompletableFuture<Suggestions> getInvisibleChannels(CommandContext<CommandSource> context,
            SuggestionsBuilder builder) {
        List<String> channels = new ArrayList<String>();

        try {
            List<TextChannel> textChannels = ModUser.fromMCPlayerEntity(getMod(), context.getSource().asPlayer()).get()
                    .getInvisibleChannels();

            channels = textChannels.stream().map(channel -> channel.getName()).collect(Collectors.toList());
        } catch (Exception e) {
        }

        return ISuggestionProvider.suggest(channels.toArray(new String[0]), builder);
    }
}
