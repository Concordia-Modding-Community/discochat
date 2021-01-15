package ca.concordia.discochat.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.discord.TestChannel;
import ca.concordia.discochat.discord.TestUser;
import ca.concordia.discochat.entity.TestPlayerEntity;

public class TestDataManager {
    public static class Mocked {
        public static DataManager create() {
            DataManager dataManager = new DataManager(TestMod.Mocked.MOD);

            dataManager = spy(dataManager);

            doAnswer(invocation -> null).when(dataManager).loadUserData();

            doAnswer(invocation -> null).when(dataManager).saveUserData();

            dataManager.setUserData(TestPlayerEntity.Mocked.VALID_MC_UUID, userData -> {
                userData.setDiscordUUID(TestUser.Mocked.VALID_DISCORD_UUID);
                userData.setCurrentChannel(TestChannel.Mocked.VALID_CHANNEL_NAME);
            });

            return dataManager;
        }
    }

    @Test
    public void testGetUserDataFromPlayerEntity() {
        DataManager dataManager = TestMod.Mocked.MOD.getDataManager();

        UserData userData = dataManager.getUserData(TestPlayerEntity.Mocked.create()).get();

        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, userData.getDiscordUUID());
    }

    @Test
    public void testGetUserDataFromUser() {
        DataManager dataManager = TestMod.Mocked.MOD.getDataManager();

        UserData userData = dataManager.getUserData(TestUser.Mocked.createValid()).get();

        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID, userData.getMCUUID());
    }
}
