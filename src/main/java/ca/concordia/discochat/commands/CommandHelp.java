package ca.concordia.discochat.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.discochat.chat.text.FormatTextComponent;
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

public class CommandHelp extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("help").executes(context -> execute(context, this::defaultExecute));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        StringTextComponent finalText = new StringTextComponent("");

        String modName = getMod().getConfigManager().getModName();

        finalText.append(new StringTextComponent(modName + " Help")
                .setStyle(Style.EMPTY.setBold(true).setColor(getMod().getConfigManager().getMentionColor())));

        finalText.appendString("\n\n");

        finalText.append(new StringTextComponent("Linking:").setStyle(Style.EMPTY.setItalic(true)));

        finalText.appendString(" ");

        finalText.append(new StringTextComponent("[Link Discord Account]").setStyle(Style.EMPTY
                .setColor(Color.fromTextFormatting(TextFormatting.GREEN)).setUnderlined(true)
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                        "/" + getMod().getConfigManager().getMCCommandPrefix() + " link "))
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new StringTextComponent("Click and enter your Discord username and Discord number.")))));

        finalText.appendString("\n\n");

        finalText.append(new StringTextComponent("Channels:").setStyle(Style.EMPTY.setItalic(true)));

        finalText.appendString(" ");

        finalText.append(new StringTextComponent("[Use MC Chat]")
                .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.DARK_GREEN)).setUnderlined(true)
                        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + getMod().getConfigManager().getMCCommandPrefix() + " switch none"))
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new StringTextComponent("Click to return to normal Minecraft chat.")))));

        finalText.appendString(" ");

        finalText.append(new StringTextComponent("[Switch Channel]")
                .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.BLUE)).setUnderlined(true)
                        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + getMod().getConfigManager().getMCCommandPrefix() + " switch "))
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new StringTextComponent("Click and enter channel name to switch to.")))));

        finalText.appendString(" ");

        finalText.append(new StringTextComponent("[Listen Channel]")
                .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED)).setUnderlined(true)
                        .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                "/" + getMod().getConfigManager().getMCCommandPrefix() + " listen "))
                        .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new StringTextComponent("Click and enter which channel(s) to listen to or ignore.")))));

        finalText.appendString("\n\n");

        finalText.append(new StringTextComponent("Styling").setStyle(Style.EMPTY.setItalic(true)));

        finalText.appendString("\n");

        finalText.append(new FormatTextComponent(
                "**Bold** -> @b, __Underline__ -> @u, *Italic* -> @i, ~~Strikethrough~~ -> @s, http://url -> @l")
                        .put("b", new StringTextComponent("Bold").setStyle(Style.EMPTY.setBold(true)))
                        .put("u", new StringTextComponent("Underline").setStyle(Style.EMPTY.setUnderlined(true)))
                        .put("i", new StringTextComponent("Italic").setStyle(Style.EMPTY.setItalic(true)))
                        .put("s", new StringTextComponent("Strikethrough").setStyle(Style.EMPTY.setStrikethrough(true)))
                        .put("l", new StringTextComponent("http://url").setStyle(Style.EMPTY.setUnderlined(true)
                                .setColor(Color.fromTextFormatting(TextFormatting.BLUE))))
                        .build());

        return finalText;
    }
}
