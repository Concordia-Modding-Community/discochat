package ca.concordia.discochat.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.discord.TestUser;

public class TestModUser {
    public static class Mocked {
        public static ModUser createValid() {
            return new ModUser(TestPlayerEntity.Mocked.create(), TestUser.Mocked.createValid());
        }
    }

    @Test
    public void testValidFromDiscordUUID() {
        ModUser user = ModUser.fromDiscordUUID(TestMod.Mocked.MOD, TestUser.Mocked.VALID_DISCORD_UUID).get();

        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, user.getDiscordUUID());

        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID, user.getMCUUID());
    }

    @Test
    public void testValidFromPlayerUUID() {
        ModUser user = ModUser.fromMCUUID(TestMod.Mocked.MOD, TestPlayerEntity.Mocked.VALID_MC_UUID).get();

        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, user.getDiscordUUID());

        assertEquals(TestPlayerEntity.Mocked.VALID_MC_UUID, user.getMCUUID());
    }
}
