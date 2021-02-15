package ca.concordia.discochat.discord.commands;

import java.util.Optional;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import ca.concordia.discochat.data.UserData;
import ca.concordia.discochat.data.UserData.VerifyStatus;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandVerify extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("verify").executes(context -> execute(context, this::defaultExecute));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        User author = context.getSource().getUser();

        String discordUUID = author.getId();

        Optional<UserData> oUserData = getMod().getDataManager().getUserDataDiscord(discordUUID);

        if (!oUserData.isPresent()) {
            throw new CommandException(
                    new StringTextComponent("No account to verify with your Discord account. Make sure to link."));
        }

        UserData userData = oUserData.get();

        switch (userData.getVerifyStatus()) {
            case BOTH:
                throw new CommandException(new StringTextComponent("Account already verified."));
            case DISCORD:
                throw new CommandException(
                        new StringTextComponent("Account linked from Discord. You must verify on MC."));
            case NONE:
                throw new CommandException(new StringTextComponent("No verification status. This should not occur!"));
            default:
                break;
        }

        getMod().getDataManager().setUserData(userData.getMCUUID(), ud -> {
            ud.setVerifyStatus(VerifyStatus.BOTH);
        });

        return new StringTextComponent(getMod().getConfigManager().getModName() + " Account Verified");
    }
}
