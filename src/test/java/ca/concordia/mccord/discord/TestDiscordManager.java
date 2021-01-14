package ca.concordia.mccord.discord;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ca.concordia.mccord.TestMod;
import net.dv8tion.jda.api.entities.TextChannel;

public class TestDiscordManager {
    public static class Mocked {
        public static DiscordManager create() {
            DiscordManager discordManager = mock(DiscordManager.class);

            when(discordManager.getMod()).thenReturn(TestMod.Mocked.MOD);

            when(discordManager.getChannelByName(anyString())).then(invocation -> {
                String channelName = invocation.getArgument(0, String.class);

                if (channelName.equals(TestChannel.Mocked.VALID_CHANNEL_NAME)) {
                    return Optional.of(TestChannel.Mocked.create());
                }

                return Optional.empty();
            });

            when(discordManager.getChannels()).then(invocation -> {
                List<TextChannel> textChannels = new ArrayList<TextChannel>();

                textChannels.add(TestChannel.Mocked.create());

                return textChannels;
            });

            when(discordManager.getUserByTag(anyString())).then(invocation -> {
                String discordTag = invocation.getArgument(0, String.class);

                if (discordTag.equals(TestUser.Mocked.getValidTag())) {
                    return Optional.of(TestUser.Mocked.create());
                }

                return Optional.empty();
            });

            when(discordManager.getUserByTag(anyString(), anyString())).then(invocation -> {
                String discordName = invocation.getArgument(0, String.class);
                String discordNumber = invocation.getArgument(1, String.class);

                if (discordName.equals(TestUser.Mocked.VALID_DISCORD_NAME)
                        && discordNumber.equals(TestUser.Mocked.VALID_DISCORD_NUMBER)) {
                    return Optional.of(TestUser.Mocked.create());
                }

                return Optional.empty();
            });

            when(discordManager.getUserFromUUID(anyString())).then(invocation -> {
                String discordUUID = invocation.getArgument(0, String.class);

                if (discordUUID.equals(TestUser.Mocked.VALID_DISCORD_UUID)) {
                    return Optional.of(TestUser.Mocked.create());
                }

                return Optional.empty();
            });

            when(discordManager.isConnected()).thenReturn(true);

            when(discordManager.connect(anyString())).thenReturn(true);

            return discordManager;
        }
    }

    @Test
    public void testGetUserBySingleTag() {
        assertEquals(TestUser.Mocked.VALID_DISCORD_NAME,
                TestMod.Mocked.MOD.getDiscordManager().getUserByTag(TestUser.Mocked.getValidTag()).get().getName());
    }

    @Test
    public void testGetUserByMultiTag() {
        assertEquals(TestUser.Mocked.VALID_DISCORD_NAME,
                TestMod.Mocked.MOD.getDiscordManager()
                        .getUserByTag(TestUser.Mocked.VALID_DISCORD_NAME, TestUser.Mocked.VALID_DISCORD_NUMBER).get()
                        .getName());
    }

    @Test
    public void testGetUserByUUID() {
        assertEquals(TestUser.Mocked.VALID_DISCORD_NAME, TestMod.Mocked.MOD.getDiscordManager()
                .getUserFromUUID(TestUser.Mocked.VALID_DISCORD_UUID).get().getName());
    }
}
