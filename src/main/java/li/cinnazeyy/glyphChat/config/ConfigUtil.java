package li.cinnazeyy.glyphChat.config;

import li.cinnazeyy.glyphChat.GlyphChat;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class ConfigUtil {
    private ConfigUtil() {
    }

    private static MainConfig mainConfig = null;
    private static GlyphConfig glyphConfig = null;

    private static CommentedConfigurationNode mainRoot = null;
    private static CommentedConfigurationNode glyphRoot = null;

    private static YamlConfigurationLoader mainLoader = null;
    private static YamlConfigurationLoader glyphLoader = null;

    public static void init() throws ConfigurateException {
        mainLoader = YamlConfigurationLoader.builder()
                .path(GlyphChat.getInstance().getDataFolder().toPath().resolve("config.yml"))
                .build();

        glyphLoader = YamlConfigurationLoader.builder()
                .path(GlyphChat.getInstance().getDataFolder().toPath().resolve("glyphs.yml"))
                .build();

        loadConfigFiles();
    }

    public static void saveConfig() throws ConfigurateException {
        mainLoader.save(mainRoot);
        glyphLoader.save(glyphRoot);
    }

    private static void loadConfigFiles() throws ConfigurateException {
        mainRoot = mainLoader.load();
        mainConfig = mainRoot.get(MainConfig.class);

        glyphRoot = glyphLoader.load();
        glyphConfig = glyphRoot.get(GlyphConfig.class);
    }

    public static MainConfig getMainConfig() {
        return mainConfig;
    }

    public static GlyphConfig getGlyphConfig() {
        return glyphConfig;
    }

    public static void reloadConfig() throws ConfigurateException {
        saveConfig();
        loadConfigFiles();
    }
}
