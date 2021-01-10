package ca.concordia.mccord.commands;

import com.mojang.brigadier.CommandDispatcher;

import ca.concordia.mccord.Resources;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Resources.MOD_ID, bus = Bus.FORGE)
public class ModCommands {
    public static final Command[] COMMANDS = new Command[] { new CommandToken(), new CommandStart(),
            new CommandMessage(), new CommandLink(), new CommandStop(), new CommandSwitch() };

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();

        for (Command command : COMMANDS) {
            command.register(dispatcher);
        }
    }
}
