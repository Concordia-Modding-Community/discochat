package ca.concordia.mccord.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandStop extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("stop").requires(command -> command.hasPermissionLevel(3))
                .executes(context -> execute(context, this::defaultExecute));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        getMod().getDiscordManager().disconnect();

        return new StringTextComponent(TextFormatting.GREEN + "Discord disconnected.");
    }
}
