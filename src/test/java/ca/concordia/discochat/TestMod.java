package ca.concordia.discochat;

import ca.concordia.discochat.DiscoChatMod;
import ca.concordia.discochat.chat.TestChatManager;
import ca.concordia.discochat.data.TestDataManager;
import ca.concordia.discochat.discord.TestDiscordManager;
import ca.concordia.discochat.server.TestConfigManager;
import ca.concordia.discochat.server.TestServerManager;
import ca.concordia.discochat.utils.IMod;

public class TestMod {
    public static class Mocked {
        public static IMod MOD = create();

        private static DiscoChatMod create() {
            TestChatManager.Mocked.clearMessages();

            return new DiscoChatMod(TestDiscordManager.Mocked.create(), TestServerManager.Mocked.create(),
                    TestDataManager.Mocked.create(), TestConfigManager.Mocked.create());
        }
    }
}
