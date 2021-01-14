package ca.concordia.mccord.events;

import com.mojang.authlib.exceptions.AuthenticationException;

import ca.concordia.mccord.entity.MCCordUser;
import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class ChatEvents extends AbstractManager {
    public ChatEvents(IMod mod) {
        super(mod);
    }

    public void register(IEventBus bus) {
        bus.addListener(EventPriority.NORMAL, this::onServerChat);
    }

    private void onServerChat(ServerChatEvent event) {
        if (!getMod().getDiscordManager().isConnected()) {
            event.setCanceled(false);

            return;
        }

        ServerPlayerEntity playerEntity = event.getPlayer();

        try {
            MCCordUser mcCordUser = MCCordUser.fromMCPlayerEntity(getMod(), playerEntity).get();

            getMod().getChatManager().broadcastAll(mcCordUser, mcCordUser.getCurrentChannel(), event.getMessage());
        } catch (AuthenticationException e) {
            playerEntity
                    .sendMessage(
                            new StringTextComponent(
                                    TextFormatting.RED + "Invalid credentials to send message to Discord. "
                                            + "Make sure your account is linked and you have the privilidges."),
                            Util.DUMMY_UUID);
        } catch (Exception e) {
            e.printStackTrace();

            playerEntity.sendMessage(new StringTextComponent(TextFormatting.RED + "Unable to send message."),
                    Util.DUMMY_UUID);
        }

        event.setCanceled(true);
    }
}
