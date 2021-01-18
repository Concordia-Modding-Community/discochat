package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandDiscordTextFormat extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("dcformat")
                .requires(context -> context.hasRole(getMod().getConfigManager().getDiscordAdminRole()))
                .then(DiscordBrigadier.argument("format", StringArgumentType.greedyString())
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        String textFormat = StringArgumentType.getString(context, "format");

        getMod().getConfigManager().setDiscordTextFormat(textFormat);

        return new StringTextComponent("Discord Text Format Set.");
    }
}
