package ca.concordia.discochat.events;

import com.mojang.authlib.exceptions.AuthenticationException;

import ca.concordia.discochat.Resources;
import ca.concordia.discochat.chat.text.FormatTextComponent;
import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
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

    public void onServerChat(ServerChatEvent event) {
        if (!getMod().getDiscordManager().isConnected()) {
            event.setCanceled(false);

            return;
        }

        ServerPlayerEntity playerEntity = event.getPlayer();

        try {
            ModUser user = ModUser.fromMCPlayerEntity(getMod(), playerEntity).get();

            String channel = user.getCurrentChannel();

            if (channel.equals(Resources.NULL_CHANNEL)) {
                throw new Exception("Null channel.");
            }

            getMod().getChatManager().broadcastAll(user, channel, event.getMessage());

            event.setCanceled(true);

            return;
        } catch (AuthenticationException e) {
            playerEntity
                    .sendMessage(
                            new StringTextComponent(
                                    TextFormatting.RED + "Invalid credentials to send message to Discord. "
                                            + "Make sure your account is linked and you have the privilidges."),
                            Util.DUMMY_UUID);
        } catch (Exception e) {
            event.setCanceled(false);
        }

        String message = new FormatTextComponent(getMod().getConfigManager().getDiscordTextFormat())
                .put("p", event.getUsername()).put("m", event.getMessage()).build().getString();

        getMod().getDiscordManager().getChannelByName(getMod().getConfigManager().getNotificationChannel()).get()
                .sendMessage(message).queue();
    }
}
