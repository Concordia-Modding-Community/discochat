package ca.concordia.discochat.events;

import ca.concordia.discochat.chat.text.FormatTextComponent;
import ca.concordia.discochat.entity.ModUser;
import ca.concordia.discochat.utils.AbstractManager;
import ca.concordia.discochat.utils.IMod;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

public class PlayerEvents extends AbstractManager {
    public PlayerEvents(IMod mod) {
        super(mod);
    }

    public void register(IEventBus bus) {
        bus.addListener(EventPriority.NORMAL, this::onPlayerJoin);
        bus.addListener(EventPriority.NORMAL, this::onPlayerLeave);
        bus.addListener(EventPriority.NORMAL, this::onPlayerDeath);
        bus.addListener(EventPriority.NORMAL, this::onPlayerAdvancement);
        // bus.addListener(EventPriority.NORMAL, this::onScreenshot);
    }

    public Tuple<ITextComponent, Boolean> getPlayerName(PlayerEntity playerEntity) {
        if (getMod().getDataManager().containsUserVerified(playerEntity)) {
            ModUser user = ModUser.fromMCUUID(getMod(), playerEntity.getUniqueID().toString()).get();

            return new Tuple<>(new StringTextComponent(user.getDiscordName()), true);
        } else {
            return new Tuple<>(playerEntity.getName(), false);
        }
    }

    public void onPlayerJoin(PlayerLoggedInEvent event) {
        PlayerEntity playerEntity = event.getPlayer();

        getMod().getDiscordManager().updateActivity();

        Tuple<ITextComponent, Boolean> playerName = getPlayerName(playerEntity);

        getMod().getChatManager()
                .notifyDiscord(new FormatTextComponent(getMod().getConfigManager().getPlayerJoinMessage())
                        .put("p", playerName.getA()).build().getString());

        if (playerName.getB()) {
            return;
        }

        StringTextComponent finalText = new StringTextComponent("");

        String modName = getMod().getConfigManager().getModName();

        finalText.append(new StringTextComponent("Welcome to " + modName + "\n\n")
                .setStyle(Style.EMPTY.setBold(true).setColor(getMod().getConfigManager().getMentionColor())));

        finalText.append(new StringTextComponent(modName)
                .setStyle(Style.EMPTY.setColor(getMod().getConfigManager().getMentionColor())));

        if (playerEntity.hasPermissionLevel(3) && !getMod().getConfigManager().isDiscordTokenValid()) {
            finalText.appendString(" is not linked to your ");

            finalText.append(
                    new StringTextComponent("Discord").setStyle(Style.EMPTY.setColor(Color.fromHex("#7289da"))));

            finalText.appendString(" Bot.\n\n");

            finalText.append(new StringTextComponent("[Link Discord Bot]")
                    .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GREEN)).setUnderlined(true)
                            .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                                    "/" + getMod().getConfigManager().getMCCommandPrefix() + " token "))
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new StringTextComponent("Click and enter your Discord bot token.")))));

            finalText.append(new StringTextComponent("[More Help?]").setStyle(Style.EMPTY
                    .setColor(Color.fromTextFormatting(TextFormatting.YELLOW)).setUnderlined(true)
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                            "/" + getMod().getConfigManager().getMCCommandPrefix() + " help"))
                    .setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click here.")))));
        } else {
            finalText.appendString(" lets you use ");

            finalText.append(
                    new StringTextComponent("Discord").setStyle(Style.EMPTY.setColor(Color.fromHex("#7289da"))));

            finalText.appendString(" from ");

            finalText.append(new StringTextComponent("Minecraft")
                    .setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.DARK_GREEN))));

            finalText.appendString("!\n\n");

            finalText.append(new StringTextComponent("[Link Discord Account]").setStyle(Style.EMPTY
                    .setColor(Color.fromTextFormatting(TextFormatting.GREEN)).setUnderlined(true)
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                            "/" + getMod().getConfigManager().getMCCommandPrefix() + " link "))
                    .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            new StringTextComponent("Click and enter your Discord username and Discord number.")))));

            finalText.appendString(" ");

            finalText.append(new StringTextComponent("[Get Discord]").setStyle(Style.EMPTY
                    .setColor(Color.fromHex("#7289da")).setUnderlined(true)
                    .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.com/")).setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click to visit.")))));

            finalText.appendString(" ");

            finalText.append(new StringTextComponent("[More Help?]").setStyle(Style.EMPTY
                    .setColor(Color.fromTextFormatting(TextFormatting.YELLOW)).setUnderlined(true)
                    .setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
                            "/" + getMod().getConfigManager().getMCCommandPrefix() + " help"))
                    .setHoverEvent(
                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Click here.")))));
        }

        playerEntity.sendStatusMessage(finalText, false);
    }

    public void onPlayerLeave(PlayerLoggedOutEvent event) {
        PlayerEntity playerEntity = event.getPlayer();

        getMod().getDiscordManager().updateActivity();

        ITextComponent playerName = getPlayerName(playerEntity).getA();

        getMod().getChatManager()
                .notifyDiscord(new FormatTextComponent(getMod().getConfigManager().getPlayerLeaveMessage())
                        .put("p", playerName).build().getString());
    }

    public void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = (PlayerEntity) entity;

        ITextComponent playerName = getPlayerName(playerEntity).getA();

        String message = playerEntity.getCombatTracker().getDeathMessage().getString().trim();

        message = message.replaceAll(playerEntity.getName().getString(), playerName.getString());

        getMod().getChatManager().notifyDiscord(message);
    }

    public void onPlayerAdvancement(AdvancementEvent event) {
        Advancement advancement = event.getAdvancement();
        DisplayInfo displayInfo = advancement.getDisplay();

        if (displayInfo == null || !displayInfo.shouldAnnounceToChat()) {
            return;
        }

        ITextComponent playerName = event.getPlayer().getName();

        ITextComponent title = advancement.getDisplayText();

        ITextComponent description = displayInfo.getDescription();

        getMod().getChatManager()
                .notifyDiscord(new FormatTextComponent(getMod().getConfigManager().getPlayerAdvancementMessage())
                        .put("d", description).put("p", playerName).put("t", title).build().getString());
    }

    public void onScreenshot(ScreenshotEvent event) {
        try {
            getMod().getDiscordManager().getChannelByName(getMod().getConfigManager().getScreenshotChannel()).get()
                    .sendFile(event.getImage().getBytes(), event.getScreenshotFile().getName()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }

        event.setCanceled(false);
    }
}
