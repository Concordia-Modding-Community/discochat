package ca.concordia.discochat.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.discochat.chat.text.ChannelTextComponent;
import ca.concordia.discochat.chat.text.FormatTextComponent;
import ca.concordia.discochat.entity.ModUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class CommandListen extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("listen")
                .then(Commands.literal("everything").executes(context -> execute(context, this::everythingExecute)))
                .then(Commands.literal("add").then(Commands.argument("channel", StringArgumentType.word())
                        .suggests((context, builder) -> getMod().getCommandSuggestions().getInvisibleChannels(context,
                                builder))
                        .executes(context -> execute(context, this::addExecute))))
                .then(Commands.literal("remove").then(Commands.argument("channel", StringArgumentType.word())
                        .suggests((context, builder) -> getMod().getCommandSuggestions().getVisibleChannels(context,
                                builder))
                        .executes(context -> execute(context, this::removeExecute))))
                .then(Commands.literal("nothing").executes(context -> execute(context, this::nothingExecute)));
    }

    private ITextComponent addExecute(CommandContext<CommandSource> context) throws CommandException {
        ModUser user = getSourceUser(context).get();

        String channelName = StringArgumentType.getString(context, "channel");

        TextChannel channel = getMod().getDiscordManager().getChannelByName(channelName).orElseThrow(
                () -> new CommandException(new FormatTextComponent("Cannot listen to @c.").put("c", channelName).build()
                        .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED)))));

        if (!user.isChannelAccessible(channel)) {
            throw new CommandException(new FormatTextComponent("Cannot listen to @c.").put("c", channelName).build()
                    .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))));
        }

        user.setChannelVisible(channel.getName());

        return new FormatTextComponent("Listening to @c.").put("c", ChannelTextComponent.from(getMod(), channel))
                .build().setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN)));
    }

    private ITextComponent removeExecute(CommandContext<CommandSource> context) throws CommandException {
        ModUser user = getSourceUser(context).get();

        String channelName = StringArgumentType.getString(context, "channel");

        TextChannel channel = getMod().getDiscordManager().getChannelByName(channelName).orElseThrow(
                () -> new CommandException(new FormatTextComponent("Cannot ignore @c.").put("c", channelName).build()
                        .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED)))));

        if (!user.isChannelAccessible(channel)) {
            throw new CommandException(new FormatTextComponent("Cannot ignore @c.").put("c", channelName).build()
                    .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))));
        }

        user.setChannelHidden(channel.getName());

        return new FormatTextComponent("Ignoring @c.").put("c", ChannelTextComponent.from(getMod(), channel)).build()
                .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN)));
    }

    private ITextComponent everythingExecute(CommandContext<CommandSource> context) throws CommandException {
        ModUser user = getSourceUser(context).get();

        user.setAllChannelVisible();

        return new StringTextComponent("Listening to all channels.")
                .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN)));
    }

    private ITextComponent nothingExecute(CommandContext<CommandSource> context) throws CommandException {
        ModUser user = getSourceUser(context).get();

        user.setNoChannelVisible();

        return new StringTextComponent("Ignoring all channels.")
                .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN)));
    }
}
