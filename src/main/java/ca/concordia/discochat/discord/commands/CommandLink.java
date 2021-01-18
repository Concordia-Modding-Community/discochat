package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

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
            getMod().getUserManager().link(mcUUID, discordUUID);

            ITextComponent text = new StringTextComponent("Discord Account " + TextFormatting.BLUE + "@"
                    + author.getAsTag() + TextFormatting.RESET + " Linked.");

            playerEntity.sendStatusMessage(text, false);

            return new StringTextComponent("MC Account Linked.");
        } catch (Exception e) {
            throw new CommandException(
                    new StringTextComponent("Account(s) already linked. Try unlinking your account(s)."));
        }
    }
}
