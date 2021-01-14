package ca.concordia.mccord.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.Config;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandStart extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("start").requires(command -> command.hasPermissionLevel(3))
                .executes(context -> execute(context, this::defaultExecute));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        if (getMod().getDiscordManager().isConnected()) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Discord already connected."));
        }

        if (Config.DISCORD_API_KEY.get().isBlank()) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "No valid token set."));
        }

        if (!getMod().getDiscordManager().connect(Config.DISCORD_API_KEY.get())) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to connect to Discord."));
        }

        return new StringTextComponent(TextFormatting.GREEN + "Discord connected.");
    }
}