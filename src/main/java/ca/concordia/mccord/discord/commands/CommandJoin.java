package ca.concordia.mccord.discord.commands;

import java.util.List;

import ca.concordia.mccord.entity.UserManager;
import net.dv8tion.jda.api.entities.Message;
import net.minecraft.entity.player.ServerPlayerEntity;

public class CommandJoin extends Command {
    @Override
    protected String commandKey() {
        return "join";
    }

    @Override
    public void execute(Message message) {
        List<String> arguments = getArguments(message.getContentRaw());

        String minecraftName = arguments.get(0);

        ServerPlayerEntity playerEntity = UserManager.fromMCName(minecraftName);

        if(playerEntity == null) {
            message.getChannel().sendMessage(String.format("Unable to find MC username.")).queue();

            return;
        }
    
        String discordUUID = message.getAuthor().getId();

        String mcUUID = playerEntity.getUniqueID().toString();

        UserManager.linkUsers(discordUUID, mcUUID);

        message.getChannel().sendMessage(String.format("MC linked.")).queue();
    }
}
