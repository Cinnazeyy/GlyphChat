package li.cinnazeyy.glyphChat.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.config.Emoji;
import li.cinnazeyy.glyphChat.core.GlyphManager;
import li.cinnazeyy.glyphChat.utils.ChatUtils;
import li.cinnazeyy.glyphChat.utils.SoundUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public class ChatListener implements Listener {
    private static final String EMOJI_INDICATOR = ConfigUtil.getMainConfig().emojiIndicator();
    private static final Pattern EMOJI_PATTERN = Pattern.compile(EMOJI_INDICATOR + "(\\w+)" + EMOJI_INDICATOR);

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        String serialized = PlainTextComponentSerializer.plainText().serialize(event.message());
        for (String glyph : GlyphManager.GLYPH_SET) {
            if (!serialized.contains(glyph)) continue;
            event.getPlayer().sendMessage(ChatUtils.getAlertMessageFormat("Cannot send message! Your message contained unallowed characters!"));
            event.getPlayer().playSound(event.getPlayer(), SoundUtils.ERROR_SOUND, 1f, 1f);
            event.setCancelled(true);
            return;
        }

        Component replaced = event.message().replaceText(EMOJI_PATTERN, builder -> {
            Matcher matcher = EMOJI_PATTERN.matcher(builder.content());
            StringBuilder result = new StringBuilder();
            while (matcher.find()) {
                String placeholder = matcher.group(1);
                Emoji emoji = GlyphManager.EMOJI_MAP.get(placeholder);

                if (emoji == null) {
                    // keep original text
                    matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group()));
                    continue;
                }

                if (emoji.permission() != null && !event.getPlayer().hasPermission(emoji.permission())) {
                    event.getPlayer().sendMessage(ChatUtils.getAlertMessageFormat("You do not have permission to use this emoji!"));
                    event.getPlayer().playSound(event.getPlayer(), SoundUtils.ERROR_SOUND, 1f, 1f);
                    matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group()));
                    continue;
                }

                matcher.appendReplacement(result, Matcher.quoteReplacement(emoji.symbol() + ""));
            }
            matcher.appendTail(result);
            return text(result.toString(), YELLOW);
        });
        event.message(replaced);
    }
}
