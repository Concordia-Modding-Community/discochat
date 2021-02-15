package ca.concordia.discochat.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import java.util.Optional;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.data.UserData.VerifyStatus;
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
                userData.setVerifyStatus(VerifyStatus.BOTH);
            });

            dataManager.setUserData(TestPlayerEntity.Mocked.UNVERIFIED_MC_UUID, userData -> {
                userData.setDiscordUUID(TestUser.Mocked.UNVERIFIED_DISCORD_UUID);
                userData.setVerifyStatus(VerifyStatus.MINECRAFT);
            });

            return dataManager;
        }
    }

    @Test
    public void testGetUserDataFromValidPlayerEntity() {
        DataManager dataManager = TestMod.Mocked.MOD.getDataManager();

        UserData userData = dataManager.getUserData(TestPlayerEntity.Mocked.createValid()).get();

        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, userData.getDiscordUUID());
    }

    @Test
    public void testGetUserDataFromValidUser() {
        DataManager dataManager = TestMod.Mocked.MOD.getDataManager();

        UserData userData = dataManager.getUserData(TestUser.Mocked.createValid()).get();

        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID, userData.getMCUUID());
    }

    @Test
    public void testGetStatusOfUnverifiedUser() {
        DataManager dataManager = TestMod.Mocked.MOD.getDataManager();

        UserData userData = dataManager.getUserData(TestUser.Mocked.createUnverified()).get();

        assertNotEquals(VerifyStatus.BOTH, userData.getVerifyStatus());
    }
}
