package ca.concordia.mccord.utils;

import java.util.function.Function;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

public interface ICommand<T, U> {
    LiteralArgumentBuilder<T> getParser();

    void register(CommandDispatcher<T> dispatcher);

    int execute(CommandContext<T> commandContext, Function<CommandContext<T>, U> function);
}
