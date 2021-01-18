package ca.concordia.discochat.discord.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import ca.concordia.b4dis.CommandSourceDiscord;
import ca.concordia.b4dis.DiscordBrigadier;
import net.dv8tion.jda.api.entities.Role;
import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class CommandAdmin extends Command {
    @Override
    public LiteralArgumentBuilder<CommandSourceDiscord> getParser() {
        return DiscordBrigadier.literal("admin")
                .requires(context -> context.hasRole(getMod().getConfigManager().getDiscordAdminRole()))
                .then(DiscordBrigadier.argument("role", StringArgumentType.word())
                        .executes(context -> execute(context, this::defaultExecute)));
    }

    public ITextComponent defaultExecute(CommandContext<CommandSourceDiscord> context) throws CommandException {
        String roleName = StringArgumentType.getString(context, "role");

        try {
            Role role = getMod().getDiscordManager().getRoleByName(roleName).get();

            getMod().getConfigManager().setAdminRole(role.getName());

            return new StringTextComponent("Set admin role to " + role.getAsMention() + ".");
        } catch(Exception e) {
        }

        throw new CommandException(new StringTextComponent("Unable to set admin role. Does the role exist?"));
    }
}