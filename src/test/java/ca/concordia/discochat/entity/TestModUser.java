package ca.concordia.discochat.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.discord.TestUser;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.ServerPlayerEntity;

public class TestModUser {
    public static class Mocked {
        public static ModUser createValid() throws Exception {
            return new ModUser(TestMod.Mocked.MOD, Optional.of(TestPlayerEntity.Mocked.createValid()), Optional.of(TestUser.Mocked.createValid()));
        }

        public static ModUser createUnverified() throws Exception {
            return new ModUser(TestMod.Mocked.MOD, Optional.of(TestPlayerEntity.Mocked.createUnverified()), Optional.of(TestUser.Mocked.createUnverified()));
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

    @Test
    public void testNullForUnverifiedMCUser() {
        Optional<ServerPlayerEntity> user = TestMod.Mocked.MOD.getUserManager().getPlayerEntityFromDiscordUser(
            TestMod.Mocked.MOD.getUserManager().fromDiscordUUID(TestUser.Mocked.UNVERIFIED_DISCORD_UUID).get()
        );

        assertTrue(!user.isPresent());
    }

    @Test
    public void testNotNullForVerifiedMCUser() {
        Optional<ServerPlayerEntity> user = TestMod.Mocked.MOD.getUserManager().getPlayerEntityFromDiscordUser(
            TestMod.Mocked.MOD.getUserManager().fromDiscordUUID(TestUser.Mocked.VALID_DISCORD_UUID).get());

        assertTrue(user.isPresent());
    }

    @Test
    public void testNullForUnverifiedDiscordUser() {
        Optional<User> user = TestMod.Mocked.MOD.getUserManager().getDiscordUserFromPlayerEntity(
            TestMod.Mocked.MOD.getUserManager().fromMCName(TestPlayerEntity.Mocked.UNVERIFIED_MC_NAME).get()
        );

        assertTrue(!user.isPresent());
    }

    @Test
    public void testNotNullForVerifiedDiscordUser() {
        Optional<User> user = TestMod.Mocked.MOD.getUserManager().getDiscordUserFromPlayerEntity(
            TestMod.Mocked.MOD.getUserManager().fromMCName(TestPlayerEntity.Mocked.VALID_MC_NAME).get()
        );

        assertTrue(user.isPresent());
    }
}
