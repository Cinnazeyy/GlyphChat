package li.cinnazeyy.glyphChat;

import li.cinnazeyy.glyphChat.commands.CMD_EmojiList;
import li.cinnazeyy.glyphChat.commands.CMD_ReloadGlyphs;
import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.core.GlyphManager;
import li.cinnazeyy.glyphChat.event.ChatListener;
import li.cinnazeyy.glyphChat.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.io.File;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;

public final class GlyphChat extends JavaPlugin {
    private static GlyphChat instance;

    @Override
    public void onEnable() {
        instance = this;

        createConfig("config.yml");
        createConfig("glyphs.yml");
        try {
            ConfigUtil.init();
        } catch (ConfigurateException e) {
            getComponentLogger().error(text("Could not load configuration files!"), e);
            Bukkit.getConsoleSender().sendMessage(text("The config files must be configured!", YELLOW));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        GlyphManager.loadEmojis();
        ChatUtils.updateChatFormat();

        // Register Commands
        final CommandMap commandMap = Bukkit.getServer().getCommandMap();
        commandMap.register("glyphchat", new CMD_EmojiList("emojis"));
        commandMap.register("glyphchat", new CMD_ReloadGlyphs("reloadglyphs"));

        // Register events
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }

    public void createConfig(String configFileName) {
        File file = getDataPath().resolve(configFileName).toFile();
        if (!file.exists()) saveResource(configFileName, false);
    }

    public static GlyphChat getInstance() {
        return instance;
    }
}
