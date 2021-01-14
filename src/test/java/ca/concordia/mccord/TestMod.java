package ca.concordia.mccord;

import ca.concordia.mccord.chat.TestChatManager;
import ca.concordia.mccord.data.TestDataManager;
import ca.concordia.mccord.discord.TestDiscordManager;
import ca.concordia.mccord.server.TestConfigManager;
import ca.concordia.mccord.server.TestServerManager;
import ca.concordia.mccord.utils.IMod;

public class TestMod {
    public static class Mocked {
        public static IMod MOD = create();

        private static MCCord create() {
            TestChatManager.Mocked.clearMessages();

            return new MCCord(TestDiscordManager.Mocked.create(), TestServerManager.Mocked.create(),
                    TestDataManager.Mocked.create(), TestConfigManager.Mocked.create());
        }
    }
}
