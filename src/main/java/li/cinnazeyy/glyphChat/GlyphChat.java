package li.cinnazeyy.glyphChat;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public final class GlyphChat extends JavaPlugin {

    public static final HashMap<String, Emoji> EMOJI_MAP = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        // TODO: initialize emojiMap

        Emoji test = new Emoji('1', List.of("skull"));
        EMOJI_MAP.put("skull", test);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
