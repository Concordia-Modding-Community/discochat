package ca.concordia.mccord.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;

import ca.concordia.mccord.TestMod;
import ca.concordia.mccord.entity.TestPlayerEntity;
import net.minecraft.server.MinecraftServer;

public class TestServerManager {
    public static class Mocked {
        private static MinecraftServer createMinecraftServer() {
            MinecraftServer server = mock(MinecraftServer.class);

            when(server.getPlayerList()).then(invocation -> TestPlayerEntity.Mocked.createList());

            return server;
        }

        public static ServerManager create() {
            ServerManager serverManager = mock(ServerManager.class);

            when(serverManager.getMod()).thenReturn(TestMod.Mocked.MOD);

            when(serverManager.getServer()).then(invocation -> Optional.of(createMinecraftServer()));

            return serverManager;
        }
    }

    @Test
    public void testGetPlayerFromUUID() {
        assertEquals(TestPlayerEntity.Mocked.VALID_MC_NAME,
                TestMod.Mocked.MOD.getServerManager().getServer().get().getPlayerList()
                        .getPlayerByUUID(UUID.fromString(TestPlayerEntity.Mocked.VALID_MC_UUID)).getName().getString());
    }

    @Test
    public void testGetPlayerFromUsername() {
        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID, TestMod.Mocked.MOD.getServerManager().getServer().get()
                .getPlayerList().getPlayerByUsername(TestPlayerEntity.Mocked.VALID_MC_NAME).getUniqueID().toString());
    }
}
