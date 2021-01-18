package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandColor extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("color")
                .requires(context -> context.hasRole(getMod().getConfigManager().getDiscordAdminRole()))
                .then(DiscordBrigadier.argument("hex", StringArgumentType.greedyString())
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        String textHex = StringArgumentType.getString(context, "hex");

        getMod().getConfigManager().setMentionColor(textHex);

        return new StringTextComponent("Mention Text Color Set.");
    }
}
