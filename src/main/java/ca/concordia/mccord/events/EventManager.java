package ca.concordia.mccord.events;

import net.minecraftforge.eventbus.api.IEventBus;

public class EventManager {
    public static void register(IEventBus bus) {
        new ChatEvents().register(bus);
    }
}
