package li.cinnazeyy.glyphChat.core;

import li.cinnazeyy.glyphChat.GlyphChat;
import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.config.Emoji;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class GlyphManager {
    public static final HashMap<String, Emoji> EMOJI_MAP = new HashMap<>();
    public static final HashSet<String> GLYPH_SET = new HashSet<>();

    public static void loadEmojis() {
        EMOJI_MAP.clear();
        GLYPH_SET.clear();

        List<Emoji> emojis = ConfigUtil.getGlyphConfig().emojis();
        GlyphChat.getInstance().getComponentLogger().info("Found {} configured emojis.", emojis.size());

        for (Emoji emoji : emojis) {
            for (String keyword : emoji.keywords()) {
                if (EMOJI_MAP.containsKey(keyword)) {
                    GlyphChat.getInstance().getComponentLogger().warn(text("Duplicate emoji keyword found! '{}' already exists! Skipping this entry..."));
                    continue;
                }
                EMOJI_MAP.put(keyword, emoji);
                GLYPH_SET.add(String.valueOf(emoji.symbol()));
            }
        }
    }
}
