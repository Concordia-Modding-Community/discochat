package ca.concordia.mccord.events;

import ca.concordia.mccord.utils.AbstractManager;
import ca.concordia.mccord.utils.IMod;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventManager extends AbstractManager {
    private ChatEvents chatEvents;

    public EventManager(IMod mod) {
        super(mod);

        this.chatEvents = new ChatEvents(mod);
    }

    public void register(IEventBus bus) {
        chatEvents.register(bus);
    }
}
