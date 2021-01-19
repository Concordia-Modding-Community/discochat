package ca.concordia.discochat.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.discochat.Resources;
import ca.concordia.discochat.chat.text.ChannelTextComponent;
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

public class CommandSwitch extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("switch")
                .then(Commands.literal("none").executes(context -> execute(context, this::noneExecute)))
                .then(Commands.argument("channel", StringArgumentType.word()).suggests(
                        (context, builder) -> getMod().getCommandSuggestions().getAccessibleChannels(context, builder))
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> context) throws CommandException {
        String channel = StringArgumentType.getString(context, "channel");

        try {
            ModUser user = getSourceUser(context).get();

            TextChannel textChannel = getMod().getDiscordManager().getChannelByName(channel).get();

            if (!user.isChannelAccessible(textChannel)) {
                throw new RuntimeException("Channel not accessible.");
            }

            if (!user.isChannelVisible(textChannel)) {
                sendFeedback(context, new StringTextComponent(
                        TextFormatting.YELLOW + "You are switching to a channel that you are not listening to."));
            }

            user.setCurrentChannel(channel);

            Style style = Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN));

            return new StringTextComponent("Switched to ").setStyle(style)
                    .append(ChannelTextComponent.from(getMod(), textChannel)).appendString(".");
        } catch (Exception e) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to find channel "
                    + TextFormatting.BOLD + "#" + channel + TextFormatting.RESET + "" + TextFormatting.RED + "."));
        }
    }

    protected ITextComponent noneExecute(CommandContext<CommandSource> context) throws CommandException {
        try {
            ModUser user = getSourceUser(context).get();

            user.setCurrentChannel(Resources.NULL_CHANNEL);

            Style style = Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN));

            return new StringTextComponent("Switched to global chat.").setStyle(style);
        } catch (Exception e) {
            throw new CommandException(new StringTextComponent(
                    TextFormatting.RED + "Unable to switch channel. Did you link your account?"));
        }
    }
}
