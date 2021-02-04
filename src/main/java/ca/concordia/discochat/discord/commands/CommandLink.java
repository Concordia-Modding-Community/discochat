package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import ca.concordia.discochat.data.UserData.VerifyStatus;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class CommandLink extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("link").then(DiscordBrigadier.argument("player", StringArgumentType.word())
                .executes(context -> execute(context, this::defaultExecute)));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        String minecraftName = StringArgumentType.getString(context, "player");

        PlayerEntity playerEntity = getMod().getUserManager().fromMCName(minecraftName)
                .orElseThrow(() -> new CommandException(new StringTextComponent("Unable to find MC username.")));

        User author = context.getSource().getUser();

        String discordUUID = author.getId();

        String mcUUID = playerEntity.getUniqueID().toString();

        try {
            getMod().getUserManager().link(mcUUID, discordUUID, VerifyStatus.DISCORD);

            StringTextComponent finalText = new StringTextComponent("Discord account " + TextFormatting.BLUE + "@"
                    + author.getAsTag() + TextFormatting.RESET + " is trying to link with this account. ");

            finalText.append(new StringTextComponent("[Click here]").setStyle(Style.EMPTY
                    .setColor(Color.fromTextFormatting(TextFormatting.GREEN)).setUnderlined(true)
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                            "/" + getMod().getConfigManager().getMCCommandPrefix() + " verify"))
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new StringTextComponent("Click to verify account.")))));

            finalText.appendString(" to complete the linking process.");

            playerEntity.sendStatusMessage(finalText, false);

            return new StringTextComponent("MC Account Link Request Sent.");
        } catch (Exception e) {
            throw new CommandException(
                    new StringTextComponent("Account(s) already linked. Try unlinking your account(s)."));
        }
    }
}
