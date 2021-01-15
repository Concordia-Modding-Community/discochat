package ca.concordia.discochat.utils;

import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

public interface ICommand<T, U> extends IModProvider {
    LiteralArgumentBuilder<T> getParser();

    ICommand<T, U> register(IMod mod);

    ICommand<T, U> register(CommandDispatcher<T> dispatcher);

    int execute(CommandContext<T> commandContext, Function<CommandContext<T>, U> function);
}
