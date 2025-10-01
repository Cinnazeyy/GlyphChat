package li.cinnazeyy.glyphChat.config;

import li.cinnazeyy.glyphChat.GlyphChat;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

public class ConfigUtil {
    private ConfigUtil() {
    }

    private static GlyphConfig glyphConfig = null;

    private static CommentedConfigurationNode glyphRoot = null;

    private static YamlConfigurationLoader glyphLoader = null;

    public static void init() throws ConfigurateException {
        glyphLoader = YamlConfigurationLoader.builder()
                .path(GlyphChat.getInstance().getDataFolder().toPath().resolve("glyphs.yml"))
                .build();

        loadConfigFiles();
    }

    public static void saveConfig() throws ConfigurateException {
        glyphLoader.save(glyphRoot);
    }

    private static void loadConfigFiles() throws ConfigurateException {
        glyphRoot = glyphLoader.load();
        glyphConfig = glyphRoot.get(GlyphConfig.class);
    }

    public static GlyphConfig getGlyphConfig() {
        return glyphConfig;
    }
}
