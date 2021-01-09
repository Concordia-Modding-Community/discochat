package ca.concordia.mccord.events;

import ca.concordia.mccord.Resources;
import ca.concordia.mccord.chat.ChatManager;
import ca.concordia.mccord.discord.DiscordManager;
import ca.concordia.mccord.entity.UserManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Resources.MOD_ID, bus = Bus.FORGE)
public class ChatEvents {
    @SubscribeEvent
    public static void onServerChat(ServerChatEvent event) {
        if (!DiscordManager.isConnected()) {
            event.setCanceled(false);
            
            return;
        }

        PlayerEntity player = event.getPlayer();

        ChatManager.broadcastAll(player, UserManager.getCurrentChannel(player), event.getMessage());

        event.setCanceled(true);
    }
}
