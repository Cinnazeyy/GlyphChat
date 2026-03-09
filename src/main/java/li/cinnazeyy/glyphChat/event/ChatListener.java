package li.cinnazeyy.glyphChat.event;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.config.Emoji;
import li.cinnazeyy.glyphChat.core.GlyphManager;
import li.cinnazeyy.glyphChat.utils.ChatUtils;
import li.cinnazeyy.glyphChat.utils.SoundUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public class ChatListener implements Listener {
    private static final String EMOJI_INDICATOR = ConfigUtil.getMainConfig().emojiIndicator();
    private static final Pattern EMOJI_PATTERN = Pattern.compile(EMOJI_INDICATOR + "(\\w+)" + EMOJI_INDICATOR);

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        String serialized = PlainTextComponentSerializer.plainText().serialize(event.message());
        for (String glyph : GlyphManager.GLYPH_SET) {
            if (!serialized.contains(glyph)) continue;
            event.getPlayer().sendMessage(ChatUtils.getAlertMessageFormat("Cannot send message! Your message contained unallowed characters!"));
            event.getPlayer().playSound(event.getPlayer(), SoundUtils.ERROR_SOUND, 1f, 1f);
            event.setCancelled(true);
            return;
        }

        Map<Character, Emoji> substitutedGlyphs = new HashMap<>();

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

                substitutedGlyphs.put(emoji.symbol(), emoji);
                matcher.appendReplacement(result, Matcher.quoteReplacement(emoji.symbol() + ""));
            }
            matcher.appendTail(result);
            return text(result.toString());
        });

        if (substitutedGlyphs.isEmpty()) return;

        ChatRenderer renderer = event.renderer();
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            Component rewrittenMessage = rewriteComponent(replaced, substitutedGlyphs);
            return renderer.render(source, sourceDisplayName, rewrittenMessage, viewer);
        });
    }

    /**
     * Recursively walks a component tree and splits and formats any text nodes that contain a substituted emoji glyph
     */
    private static Component rewriteComponent(Component component, Map<Character, Emoji> glyphMap) {
        if (component instanceof TextComponent tc) {
            String content = tc.content();

            boolean hasGlyph = false;
            for (char c : content.toCharArray()) {
                if (glyphMap.containsKey(c)) { hasGlyph = true; break; }
            }

            List<Component> newChildren = new ArrayList<>();
            for (Component child : tc.children()) {
                newChildren.add(rewriteComponent(child, glyphMap));
            }

            if (!hasGlyph) return tc.children(newChildren);

            List<Component> parts = new ArrayList<>();
            int start = 0;
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                Emoji emoji = glyphMap.get(c);
                if (emoji == null) continue;

                if (i > start) {
                    parts.add(tc.content(content.substring(start, i)).children(List.of()));
                }

                String emojiName = emoji.keywords().getFirst();
                parts.add(text(String.valueOf(c))
                        .color(WHITE)
                        .hoverEvent(HoverEvent.showText(text(":" + emojiName + ":").color(GRAY))));

                start = i + 1;
            }
            if (start < content.length()) {
                parts.add(tc.content(content.substring(start)).children(List.of()));
            }

            parts.addAll(newChildren);

            return tc.content("").children(parts);
        }

        List<Component> newChildren = new ArrayList<>();
        for (Component child : component.children()) {
            newChildren.add(rewriteComponent(child, glyphMap));
        }
        return component.children(newChildren);
    }
}
