package ca.concordia.discochat.events;

import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventManager extends AbstractManager {
    private ChatEvents chatEvents;
    private PlayerEvents playerEvents;

    public EventManager(IMod mod) {
        super(mod);

        this.chatEvents = new ChatEvents(mod);

        this.playerEvents = new PlayerEvents(mod);
    }

    public void register(IEventBus bus) {
        chatEvents.register(bus);
        playerEvents.register(bus);
    }
}
