package ca.concordia.mccord.chat;

import java.util.Stack;

public class TestChatManager {
    public static class Mocked {
        private static Stack<String> MC_MESSAGES = new Stack<String>();
        private static Stack<String> DISCORD_MESSAGES = new Stack<String>();

        public static void clearMessages() {
            MC_MESSAGES = new Stack<String>();
            DISCORD_MESSAGES = new Stack<String>();
        }

        public static void addMCMessage(String message) {
            MC_MESSAGES.add(message);
        }

        public static void addDiscordMessage(String message) {
            DISCORD_MESSAGES.add(message);
        }

        public static String getLastMCMessage() {
            return MC_MESSAGES.lastElement();
        }

        public static String getLastDiscordMessage() {
            return DISCORD_MESSAGES.lastElement();
        }
    }
}
