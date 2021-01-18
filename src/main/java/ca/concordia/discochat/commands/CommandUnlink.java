package ca.concordia.discochat.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandUnlink extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("unlink").executes(context -> execute(context, this::defaultExecute));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> context) throws CommandException {
        if (getMod().getDataManager().removeUserDataMC(getSourcePlayer(context).get().getUniqueID().toString())) {
            return new StringTextComponent(TextFormatting.GREEN + "Account(s) Unlinked.");
        } else {
            throw new CommandException(new StringTextComponent(TextFormatting.RED + "Unable to find "
                    + getMod().getConfigManager().getModName() + " account. Are the account(s) linked?"));
        }
    }
}
