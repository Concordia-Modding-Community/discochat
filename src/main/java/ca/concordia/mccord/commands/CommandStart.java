package ca.concordia.mccord.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.Config;
import ca.concordia.mccord.discord.DiscordManager;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandStart extends Command {
    @Override
    protected LiteralArgumentBuilder<CommandSource> parser() {
        return Commands.literal(COMMAND_PREFIX).then(Commands.literal("start")
                .requires(command -> command.hasPermissionLevel(3)).executes(command -> execute(command)));
    }

    @Override
    protected int execute(CommandContext<CommandSource> commandContext) {
        if (DiscordManager.isConnected()) {
            sendErrorMessage(commandContext,
                    new StringTextComponent(TextFormatting.RED + "Discord already connected."));

            return 0;
        }

        if (Config.DISCORD_API_KEY.get().isBlank()) {
            sendErrorMessage(commandContext, new StringTextComponent(TextFormatting.RED + "No valid token set."));

            return 0;
        }

        if (!DiscordManager.connect(Config.DISCORD_API_KEY.get())) {
            sendErrorMessage(commandContext,
                    new StringTextComponent(TextFormatting.RED + "Unable to connect to Discord."));

            return 0;
        }

        sendFeedback(commandContext, new StringTextComponent(TextFormatting.GREEN + "Discord connected."));

        return 1;
    }
}
