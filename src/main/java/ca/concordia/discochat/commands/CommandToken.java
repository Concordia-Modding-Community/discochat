package ca.concordia.discochat.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class CommandToken extends Command {
    public static final ITextComponent SUCCESS = new StringTextComponent(TextFormatting.GREEN + "Discord Token Set and Discord Linked.");

    @Override
    public LiteralArgumentBuilder<CommandSource> getParser() {
        return Commands.literal("token").requires(command -> command.hasPermissionLevel(3))
                .then(Commands.argument("key", StringArgumentType.string())
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    protected ITextComponent defaultExecute(CommandContext<CommandSource> commandContext) throws CommandException {
        String key = StringArgumentType.getString(commandContext, "key");

        getMod().getConfigManager().setDiscordToken(key);

        getMod().getDiscordManager().connect();

        getMod().getChatManager().notifyDiscord("DiscoChat Linked!");

        return SUCCESS;
    }
}
