package ca.concordia.discochat.entity;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ca.concordia.discochat.TestMod;
import ca.concordia.discochat.discord.TestUser;
import net.dv8tion.jda.api.entities.User;
import net.minecraft.entity.player.ServerPlayerEntity;

public class TestUserManager {
    @Test
    public void getUserFromPlayerEntity() {
        ServerPlayerEntity playerEntity = TestPlayerEntity.Mocked.createValid();

        UserManager userManager = TestMod.Mocked.MOD.getUserManager();

        User user = userManager.getDiscordUserFromPlayerEntity(playerEntity).get();

        assertEquals(TestUser.Mocked.VALID_DISCORD_UUID, user.getId());
    }

    @Test
    public void getPlayerEntityFromUser() {
        User user = TestUser.Mocked.createValid();

        UserManager userManager = TestMod.Mocked.MOD.getUserManager();

        ServerPlayerEntity playerEntity = userManager.getPlayerEntityFromDiscordUser(user).get();

        assertEquals(TestPlayerEntity.Mocked.VALID_MC_NAME, playerEntity.getName().getString());
    }
}
