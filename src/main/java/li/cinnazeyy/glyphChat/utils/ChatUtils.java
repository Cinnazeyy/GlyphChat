package li.cinnazeyy.glyphChat.utils;

import li.cinnazeyy.glyphChat.config.ChatSection;
import li.cinnazeyy.glyphChat.config.ConfigUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static net.kyori.adventure.text.format.NamedTextColor.GREEN;
import static net.kyori.adventure.text.format.NamedTextColor.RED;

public class ChatUtils {
    private ChatUtils() {}

    public static void updateChatFormat() {
        ChatSection chatConfig = ConfigUtil.getMainConfig().chatSection();
        infoPrefix = MiniMessage.miniMessage().deserialize(chatConfig.infoPrefix());
        alertPrefix = MiniMessage.miniMessage().deserialize(chatConfig.alertPrefix());
    }

    private static Component infoPrefix, alertPrefix;

    public static Component getInfoMessageFormat(String info) {
        return infoPrefix.append(MiniMessage.miniMessage().deserialize(info).color(GREEN));
    }

    public static Component getInfoMessageFormat(Component info) {
        return infoPrefix.append(info);
    }

    public static Component getAlertMessageFormat(String alert) {
        return alertPrefix.append(MiniMessage.miniMessage().deserialize(alert).color(RED));
    }
}
