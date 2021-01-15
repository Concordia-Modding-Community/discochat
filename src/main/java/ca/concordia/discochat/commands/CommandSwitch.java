package ca.concordia.discochat.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

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
                .then(Commands.argument("channel", StringArgumentType.word()).suggests(
                        (context, builder) -> getMod().getCommandSuggestions().getVisibleChannels(context, builder))
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        String channel = StringArgumentType.getString(commandContext, "channel");

        try {
            ModUser getUser = getSourceUser(commandContext).get();

            TextChannel textChannel = getMod().getDiscordManager().getChannelByName(channel).get();

            getUser.setCurrentChannel(channel);

            Style style = Style.EMPTY;

            style = style.setColor(Color.fromTextFormatting(TextFormatting.GREEN));

            return new StringTextComponent("Switched to ").setStyle(style)
                    .append(new ChannelTextComponent(getMod(), textChannel))
                    .append(new StringTextComponent(".").setStyle(style));
        } catch (Exception e) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to find channel "
                    + TextFormatting.BOLD + "#" + channel + TextFormatting.RESET + "" + TextFormatting.RED + "."));
        }
    }
}
