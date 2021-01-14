package ca.concordia.mccord.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.mccord.TestMod;
import ca.concordia.mccord.discord.TestUser;

public class TestMCCordUser {
    public static class Mocked {
        public static MCCordUser createValid() {
            return new MCCordUser(TestPlayerEntity.Mocked.create(), TestUser.Mocked.createValid());
        }
    }

    @Test
    public void testValidFromDiscordUUID() {
        MCCordUser user = MCCordUser.fromDiscordUUID(TestMod.Mocked.MOD, TestUser.Mocked.VALID_DISCORD_UUID).get();

        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, user.getDiscordUUID());

        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID, user.getMCUUID());
    }

    @Test
    public void testValidFromPlayerUUID() {
        MCCordUser user = MCCordUser.fromMCUUID(TestMod.Mocked.MOD, TestPlayerEntity.Mocked.VALID_MC_UUID).get();

        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, user.getDiscordUUID());

        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID, user.getMCUUID());
    }
}
