package ca.concordia.discochat.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class CommandTest extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("test").executes(context -> execute(context, this::defaultExecute));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> context) throws CommandException {
        return null;
    }
}
