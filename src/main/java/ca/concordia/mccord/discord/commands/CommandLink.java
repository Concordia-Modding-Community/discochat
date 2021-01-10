package ca.concordia.mccord.discord.commands;

import java.util.List;

import ca.concordia.mccord.entity.UserManager;
import net.dv8tion.jda.api.entities.Message;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandLink extends Command {
    @Override
    protected String commandKey() {
        return "link";
    }

    @Override
    public ITextComponent execute(Message message) throws Exception {
        List<String> arguments = getArguments(message.getContentRaw());

        String minecraftName = arguments.get(0);

        PlayerEntity playerEntity = UserManager.fromMCName(minecraftName)
                .orElseThrow(() -> new CommandException(new StringTextComponent("Unable to find MC username.")));

        String discordUUID = message.getAuthor().getId();

        String mcUUID = playerEntity.getUniqueID().toString();

        ITextComponent text = new StringTextComponent("Discord Account " + TextFormatting.BLUE + "@"
                + message.getAuthor().getAsTag() + TextFormatting.RESET + " Linked.");

        playerEntity.sendStatusMessage(text, false);

        UserManager.link(mcUUID, discordUUID);

        return new StringTextComponent("MC Account Linked.");
    }
}
