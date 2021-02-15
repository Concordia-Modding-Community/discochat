package ca.concordia.discochat.commands;

import java.util.Optional;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.discochat.data.UserData;
import ca.concordia.discochat.data.UserData.VerifyStatus;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandVerify extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("verify").executes(context -> execute(context, this::defaultExecute));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> context) throws CommandException {
        ServerPlayerEntity playerEntity = getSourcePlayer(context).get();

        Optional<UserData> oUserData = getMod().getDataManager().getUserData(playerEntity);

        if (!oUserData.isPresent()) {
            throw new CommandException(new StringTextComponent(
                    TextFormatting.RED + "No account to verify with your MC account. Make sure to link."));
        }

        UserData userData = oUserData.get();

        switch (userData.getVerifyStatus()) {
            case BOTH:
                throw new CommandException(new StringTextComponent(TextFormatting.RED + "Account already verified."));
            case MINECRAFT:
                throw new CommandException(new StringTextComponent(
                        TextFormatting.RED + "Account linked from MC. You must verify on Discord."));
            case NONE:
                throw new CommandException(new StringTextComponent(
                    TextFormatting.RED + "No verification status. This should not occur!"));
            default:
                break;
        }

        getMod().getDataManager().setUserData(userData.getMCUUID(), ud -> {
            ud.setVerifyStatus(VerifyStatus.BOTH);
        });

        return new StringTextComponent(TextFormatting.GREEN + getMod().getConfigManager().getModName() + " Account Verified");
    }
}