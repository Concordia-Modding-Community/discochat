package ca.concordia.mccord.entity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.concordia.mccord.chat.TestChatManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class TestPlayerEntity {
    public static class Mocked {
        public static final String VALID_MC_UUID = UUID.randomUUID().toString();
        public static final String VALID_MC_NAME = "dev";

        public static PlayerList createList() {
            PlayerList playerList = mock(PlayerList.class);

            when(playerList.getPlayerByUUID(any(UUID.class))).then(invocation -> {
                UUID uuid = invocation.getArgument(0, UUID.class);

                if (uuid.toString().equals(VALID_MC_UUID)) {
                    return create();
                }

                return null;
            });

            when(playerList.getPlayerByUsername(anyString())).then(invocation -> {
                String username = invocation.getArgument(0, String.class);

                if (username.equals(VALID_MC_NAME)) {
                    return create();
                }

                return null;
            });

            return playerList;
        }

        public static ServerPlayerEntity create() {
            ServerPlayerEntity playerEntity = mock(ServerPlayerEntity.class);

            when(playerEntity.getUniqueID()).thenReturn(UUID.fromString(VALID_MC_UUID));

            when(playerEntity.getName()).thenReturn(new StringTextComponent(VALID_MC_NAME));

            doAnswer(new Answer<Void>() {
                @Override
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    TestChatManager.Mocked.addMCMessage(invocation.getArgument(0, ITextComponent.class).getString());

                    return null;
                }
            }).when(playerEntity).sendStatusMessage(any(ITextComponent.class), anyBoolean());

            return playerEntity;
        }
    }

    @Test
    public void testSendStatusMessage() {
        PlayerEntity playerEntity = Mocked.create();

        playerEntity.sendStatusMessage(new StringTextComponent("Hello World!"), true);

        assertEquals("Hello World!", TestChatManager.Mocked.getLastMCMessage());
    }
}
