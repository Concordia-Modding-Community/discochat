package ca.concordia.mccord.discord;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.dv8tion.jda.api.entities.User;

public class TestUser {
    public static class Mocked {
        public static final String VALID_DISCORD_UUID = "12345";
        public static final String VALID_DISCORD_NAME = "dev";
        public static final String VALID_DISCORD_NUMBER = "1234";

        public static User create() {
            User user = mock(User.class);

            when(user.getId()).thenReturn(VALID_DISCORD_UUID);

            when(user.getName()).thenReturn(VALID_DISCORD_NAME);

            return user;
        }

        public static String getValidTag() {
            return VALID_DISCORD_NAME + "#" + VALID_DISCORD_NUMBER;
        }
    }
}
