package li.cinnazeyy.glyphChat.utils;

import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.config.Emoji;
import li.cinnazeyy.glyphChat.core.GlyphManager;
import org.bukkit.entity.Player;

import java.util.*;

public final class ChatCompletionUtil {

    private static final Map<UUID, Set<String>> REGISTERED_COMPLETIONS = new HashMap<>();

    private ChatCompletionUtil() {
    }

    public static void refreshPlayerCompletions(Player player) {
        String indicator = ConfigUtil.getMainConfig().emojiIndicator();

        Set<String> completions = new HashSet<>();
        for (Map.Entry<String, Emoji> entry : GlyphManager.EMOJI_MAP.entrySet()) {
            String keyword = entry.getKey();
            Emoji emoji = entry.getValue();

            if (emoji.permission() != null && !emoji.permission().isBlank() && !player.hasPermission(emoji.permission())) {
                continue;
            }

            completions.add(indicator + keyword + indicator);
        }

        Set<String> previousCompletions = REGISTERED_COMPLETIONS.get(player.getUniqueId());
        if (previousCompletions != null && !previousCompletions.isEmpty()) {
            player.removeCustomChatCompletions(previousCompletions);
        }

        if (!completions.isEmpty()) {
            player.addCustomChatCompletions(completions);
        }

        REGISTERED_COMPLETIONS.put(player.getUniqueId(), completions);
    }

    public static void clearPlayerCompletions(Player player) {
        Set<String> previousCompletions = REGISTERED_COMPLETIONS.remove(player.getUniqueId());
        if (previousCompletions != null && !previousCompletions.isEmpty()) {
            player.removeCustomChatCompletions(previousCompletions);
        }
    }
}