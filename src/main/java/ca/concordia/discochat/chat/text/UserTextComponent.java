package ca.concordia.discochat.chat.text;

import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.IMod;
import ca.concordia.discochat.utils.IModProvider;
import net.dv8tion.jda.api.entities.TextChannel;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class UserTextComponent extends TextComponent implements IModProvider {
    private IMod mod;

    private ModUser user;

    public UserTextComponent(IMod mod, String uuid, TextChannel textChannel) {
        this.mod = mod;

        this.user = ModUser.fromDiscordUUID(getMod(), uuid).get();

        StringTextComponent stringText = new StringTextComponent(user.getMCName());

        stringText.setStyle(Style.EMPTY
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new StringTextComponent("Mention @" + user.getDiscordName())))
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getDiscordAsMention()))
                .setColor(Color.fromInt(getUserHexColor(textChannel, user))));

        siblings.add(stringText);
    }

    public UserTextComponent(IMod mod, String uuid) {
        this.mod = mod;

        this.user = ModUser.fromDiscordUUID(getMod(), uuid).get();

        StringTextComponent stringText = new StringTextComponent("@" + user.getMCName());

        stringText.setStyle(Style.EMPTY
                .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new StringTextComponent("Mention @" + user.getDiscordName())))
                .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, user.getDiscordAsMention()))
                .setColor(getMod().getConfigManager().getMentionColor()));

        siblings.add(stringText);
    }

    public ModUser getUser() {
        return user;
    }

    @Override
    public IMod getMod() {
        return mod;
    }

    @Override
    public TextComponent copyRaw() {
        // TODO Auto-generated method stub
        return null;
    }

    private int getUserHexColor(TextChannel textChannel, ModUser user) {
        return textChannel.getGuild().getMemberById(user.getDiscordUUID()).getColor().getRGB();
    }
}
