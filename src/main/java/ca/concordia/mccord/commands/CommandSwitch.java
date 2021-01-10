package ca.concordia.mccord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.mccord.entity.MCCordUser;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandSwitch extends Command {
    @Override
    protected LiteralArgumentBuilder<CommandSource> parser() {
        return Commands.literal(COMMAND_PREFIX)
                .then(Commands.literal("switch").then(Commands.argument("channel", StringArgumentType.word())
                        .suggests(Command.VISIBLE_CHANNEL_SUGGEST).executes(commandContext -> execute(commandContext))));
    }

    @Override
    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        String channel = StringArgumentType.getString(commandContext, "channel");

        try {
            MCCordUser mcCordUser = getSourceMCCordUser(commandContext).get();

            mcCordUser.setCurrentChannel(channel);
        } catch (Exception e) {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to find channel "
                    + TextFormatting.BOLD + "#" + channel + TextFormatting.RESET + "" + TextFormatting.RED + "."));
        }

        return new StringTextComponent(TextFormatting.GREEN + "Switched to " + TextFormatting.BLUE + "#" + channel
                + TextFormatting.GREEN + ".");
    }
}
