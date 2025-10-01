package li.cinnazeyy.glyphChat;

import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.config.Emoji;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public final class GlyphChat extends JavaPlugin {
    public static final HashMap<String, Emoji> EMOJI_MAP = new HashMap<>();
    public static final HashSet<String> GLYPH_SET = new HashSet<>();

    private static GlyphChat instance;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic

        createConfig("glyphs.yml");
        try {
            ConfigUtil.init();
            //ConfigUtil.saveConfig();
        } catch (ConfigurateException e) {
            getComponentLogger().error(text("Could not load configuration files!"), e);
            Bukkit.getConsoleSender().sendMessage(text("The config files must be configured!", YELLOW));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        loadEmojis();

        // Register Commands
        final CommandMap commandMap = Bukkit.getServer().getCommandMap();
        commandMap.register("emojis", new CMD_EmojiList("emojis"));

        // Register events
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void createConfig(String configFileName) {
        File file = getDataPath().resolve(configFileName).toFile();
        if (!file.exists()) saveResource(configFileName, false);
    }

    private void loadEmojis() {
        List<Emoji> emojis = ConfigUtil.getGlyphConfig().emojis();
        getComponentLogger().info("Found {} configured emojis.", emojis.size());

        for (Emoji emoji : emojis) {
            getComponentLogger().info(text("loading " + emoji.toString()));
            for (String keyword : emoji.keywords()) {
                if (EMOJI_MAP.containsKey(keyword)) {
                    getComponentLogger().warn(text("Duplicate emoji keyword found! '{}' already exists! Skipping this entry..."));
                    continue;
                }
                EMOJI_MAP.put(keyword, emoji);
                GLYPH_SET.add(String.valueOf(emoji.symbol()));
            }
        }
        getComponentLogger().info("Loaded {} emojis.", EMOJI_MAP.size());
    }

    public static GlyphChat getInstance() {
        return instance;
    }
}
