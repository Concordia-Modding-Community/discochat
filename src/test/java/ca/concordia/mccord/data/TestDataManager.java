package ca.concordia.mccord.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.mccord.TestMod;
import ca.concordia.mccord.discord.TestChannel;
import ca.concordia.mccord.discord.TestUser;
import ca.concordia.mccord.entity.TestPlayerEntity;

public class TestDataManager {
    public static class Mocked {
        public static DataManager create() {
            DataManager dataManager = new DataManager(TestMod.Mocked.MOD);

            dataManager.setUserData(TestPlayerEntity.Mocked.VALID_MC_UUID, userData -> {
                userData.setDiscordUUID(TestUser.Mocked.VALID_DISCORD_UUID);
                userData.setCurrentChannel(TestChannel.Mocked.VALID_CHANNEL_NAME);
            });

            return dataManager;
        }
    }

    @Test
    public void testGetUserDataFromPlayerEntity() {
        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, TestMod.Mocked.MOD.getDataManager()
                .getUserData(TestPlayerEntity.Mocked.create()).get().getDiscordUUID());
    }

    @Test
    public void testGetUserDataFromUser() {
        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID,
                TestMod.Mocked.MOD.getDataManager().getUserData(TestUser.Mocked.create()).get().getMCUUID());
    }
}
