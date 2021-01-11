package ca.concordia.b4dis;

import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * <a href="https://github.com/DV8FromTheWorld/JDA">JDA</a> Plugin for Parsing
 * Commands using <a href="https://github.com/Mojang/brigadier">Brigardier</a>.
 * 
 * <h2>Ping Pong Example</h2>
 * 
 * <pre>
 * <code>
 * DiscordBrigadier discordBrigadier = new DiscordBrigadier();
 * 
 * discordBrigadier.register(
 *  DiscordBrigadier
 *      .literal("ping")
 *      .executes(context -> { 
 *          context
 *              .getChannel()
 *              .sendMessage("pong")
 *              .queue(); 
 * 
 *          return 1; 
 *      })
 * );
 * 
 * try {
 *  JDA jda = JDABuilder.createDefault("Discord_Bot_Token_Here").build();
 * 
 *  jda.addEventListener(discordBrigadier);
 * 
 *  jda.awaitReady();
 * } catch(Exception e) {}
 * </code>
 * </pre>
 */
public class DiscordBrigadier extends ListenerAdapter {
    /**
     * Default value for Discord command prefix if not set.
     */
    private static final String DEFAULT_COMMAND_PREFIX = "!";

    /**
     * Brigadier command dispatcher.
     */
    private CommandDispatcher<CommandSourceDiscord> dispatcher = new CommandDispatcher<>();

    /**
     * Non-command Discord message handler method.
     */
    private Consumer<Message> messageFunction;

    /**
     * Command prefix provider.
     */
    private Provider<String> commandPrefixProvider;

    public DiscordBrigadier() {
        this(null, null);
    }

    public DiscordBrigadier(Provider<String> commandPrefixProvider, Consumer<Message> messageFunction) {
        this.commandPrefixProvider = commandPrefixProvider;
        this.messageFunction = messageFunction;
    }

    /**
     * Discord message received listener. Blocks bots from issuing commands.
     * Offloads Discord message to {@link #handleCommand} or {@link #handleMessage}
     * according to the message contents.
     *
     * @param event Event to handle.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        final Message message = event.getMessage();

        if (isCommand(message)) {
            handleCommand(message);
        } else {
            handleMessage(message);
        }
    }

    /**
     * Handles command Discord message. If there is an exception, throws the
     * Brigadier message.
     * 
     * TODO: Error handling lambda?
     * 
     * @param message Command Discord message to handle.
     */
    private void handleCommand(Message message) {
        try {
            execute(message);
        } catch (CommandSyntaxException e) {
            message.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

    /**
     * Handles non-command Discord message. If {@literal messageFunction} is
     * defined, uses the external handler.
     * 
     * @param message Non-command Discord message to handle.
     */
    private void handleMessage(Message message) {
        if (messageFunction == null) {
            return;
        }

        messageFunction.accept(message);
    }

    /**
     * Checks if Discord message stars with the set command prefix.
     * 
     * @param message Discord message to handle.
     * @return Discord message is a command.
     */
    public boolean isCommand(Message message) {
        return message.getContentDisplay().startsWith(getCommandPrefix());
    }

    /**
     * Extracts the non-prefixed command string from Discord message.
     * 
     * @param message Discord message to handle.
     * @return Non-prefixed command string.
     */
    private Optional<String> getCommand(Message message) {
        String string = message.getContentDisplay();

        if (!string.startsWith(getCommandPrefix())) {
            return Optional.empty();
        }

        return Optional.of(string.substring(getCommandPrefix().length()));
    }

    /**
     * Parses Discord message using Discord sourced
     * {@link com.mojang.brigadier.CommandDispatcher}. Will throw a runtime
     * exception if the Discord message does not begin with the command prefix.
     * 
     * @param message Discord message to parse.
     * @return Parsed Discord message.
     * @see com.mojang.brigadier.CommandDispatcher#parse
     */
    private ParseResults<CommandSourceDiscord> parse(Message message) {
        return dispatcher.parse(getCommand(message).orElseThrow(RuntimeException::new),
                new CommandSourceDiscord(message));
    }

    /**
     * Excutes command using {@link com.mojang.brigadier.CommandDispatcher}.
     * 
     * @param parseResults Parsed message to execute.
     * @return Execute code defined by programmer.
     * @throws CommandSyntaxException
     * @see com.mojang.brigadier.CommandDispatcher#execute(ParseResults)
     */
    private int execute(ParseResults<CommandSourceDiscord> parseResults) throws CommandSyntaxException {
        return dispatcher.execute(parseResults);
    }

    /**
     * Extended {@link #execute(ParseResults)}.
     * 
     * @see #execute(ParseResults)
     */
    private int execute(Message message) throws CommandSyntaxException {
        return execute(parse(message));
    }

    /**
     * Sets the non-command Discord message handler.
     * 
     * @param function Message handler to set.
     */
    public void setMessageEvent(Consumer<Message> function) {
        this.messageFunction = function;
    }

    /**
     * Sets the command prefix provider.
     * 
     * @param prefixProvider
     */
    public void setCommandPrefix(Provider<String> prefixProvider) {
        this.commandPrefixProvider = prefixProvider;
    }

    /**
     * Extended {@link #setCommandPrefix(Provider)}.
     * 
     * @param prefix
     * @see #setCommandPrefix(Provider)
     */
    public void setCommandPrefix(String prefix) {
        setCommandPrefix(() -> prefix);
    }

    /**
     * Gets the command prefix from the command prefix provider.
     * 
     * @return Command prefix.
     */
    public String getCommandPrefix() {
        if (commandPrefixProvider == null) {
            return DEFAULT_COMMAND_PREFIX;
        }

        return commandPrefixProvider.invoke();
    }

    /**
     * Discord-sourced registration to
     * {@link com.mojang.brigadier.CommandDispatcher}.
     * 
     * @param command Command tree to register.
     * @see com.mojang.brigadier.CommandDispatcher#register
     */
    public void register(LiteralArgumentBuilder<CommandSourceDiscord> command) {
        dispatcher.register(command);
    }

    /**
     * Discord-sourced literal.
     * 
     * @param name Literal name.
     * @return Literal.
     * @see com.mojang.brigadier.builder.LiteralArgumentBuilder#literal(String)
     */
    public static LiteralArgumentBuilder<CommandSourceDiscord> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Discord-sourced argument.
     * 
     * @param name Argument name.
     * @param type Argument type.
     * @return Argument.
     * @see com.mojang.brigadier.builder.RequiredArgumentBuilder#argument(String, ArgumentType)
     */
    public static <T> RequiredArgumentBuilder<CommandSourceDiscord, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
