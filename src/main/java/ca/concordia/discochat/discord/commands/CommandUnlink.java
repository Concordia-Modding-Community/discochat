package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import ca.concordia.discochat.entity.ModUser;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandUnlink extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("unlink").executes(context -> execute(context, this::defaultExecute));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        ModUser modUser = ModUser.fromDiscordUUID(getMod(), context.getSource().getUser().getId())
                .orElseThrow(() -> new CommandException(
                        new StringTextComponent("Unable to find DiscoChat account. Are the account(s) linked?")));

        getMod().getDataManager().removeUserDataMC(modUser.getMCUUID());

        return new StringTextComponent("Account(s) Unlinked.");
    }
}
