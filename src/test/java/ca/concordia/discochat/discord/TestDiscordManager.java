package ca.concordia.discochat.discord;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class TestDiscordManager {
    public static class Mocked {
        public static DiscordManager create() {
            DiscordManager discordManager = mock(DiscordManager.class);

            when(discordManager.getMod()).thenReturn(TestMod.Mocked.MOD);

            when(discordManager.getChannelByName(anyString())).then(invocation -> {
                String channelName = invocation.getArgument(0, String.class);

                TextChannel textChannel = TestChannel.Mocked.createValid();

                if (channelName.equals(textChannel.getName())) {
                    return Optional.of(textChannel);
                }

                return Optional.empty();
            });

            when(discordManager.getChannels()).then(invocation -> {
                List<TextChannel> textChannels = new ArrayList<TextChannel>();

                textChannels.add(TestChannel.Mocked.createValid());

                return Optional.of(textChannels);
            });

            when(discordManager.getUserByTag(anyString())).then(invocation -> {
                String discordTag = invocation.getArgument(0, String.class);

                User user = TestUser.Mocked.createValid();

                if (discordTag.equals(user.getAsTag())) {
                    return Optional.of(user);
                }

                User unverifiedUser = TestUser.Mocked.createUnverified();
                if (discordTag.equals(unverifiedUser.getAsTag())) {
                    return Optional.of(unverifiedUser);
                }

                return Optional.empty();
            });

            when(discordManager.getUserByTag(anyString(), anyString())).then(invocation -> {
                String discordName = invocation.getArgument(0, String.class);
                String discordNumber = invocation.getArgument(1, String.class);

                User user = TestUser.Mocked.createValid();

                if (discordName.equals(user.getName()) && discordNumber.equals(user.getAsTag().split("#")[1])) {
                    return Optional.of(user);
                }

                User unverifiedUser = TestUser.Mocked.createUnverified();
                if (discordName.equals(unverifiedUser.getName()) && discordNumber.equals(unverifiedUser.getAsTag().split("#")[1])) {
                    return Optional.of(unverifiedUser);
                }

                return Optional.empty();
            });

            when(discordManager.getUserFromUUID(anyString())).then(invocation -> {
                String discordUUID = invocation.getArgument(0, String.class);

                User user = TestUser.Mocked.createValid();

                if (discordUUID.equals(user.getId())) {
                    return Optional.of(user);
                }

                User unverifiedUser = TestUser.Mocked.createUnverified();
                if (discordUUID.equals(unverifiedUser.getId())) {
                    return Optional.of(unverifiedUser);
                }

                return Optional.empty();
            });

            when(discordManager.isConnected()).thenReturn(true);

            when(discordManager.connect(anyString())).thenReturn(true);

            when(discordManager.isChannelAccessible(any(User.class), any(TextChannel.class))).then(invocation -> {
                User user = invocation.getArgument(0, User.class);

                return user.getId() == TestUser.Mocked.VALID_DISCORD_UUID;
            });

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
