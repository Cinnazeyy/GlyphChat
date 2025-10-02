package li.cinnazeyy.glyphChat.commands;

import li.cinnazeyy.glyphChat.GlyphChat;
import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.core.GlyphManager;
import li.cinnazeyy.glyphChat.utils.ChatUtils;
import li.cinnazeyy.glyphChat.utils.SoundUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;

public class CMD_ReloadGlyphs extends BukkitCommand {
    public CMD_ReloadGlyphs(@NotNull String name) {
        super(name);
        this.description = "Reload configured glyphs.";
        this.usageMessage = "/reloadglyphs";
        this.setPermission("glyphchat.reload");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        CompletableFuture.runAsync(() -> {
            if (!sender.hasPermission("glyphchat.reload")) {
                sender.sendMessage(ChatUtils.getAlertMessageFormat("You do not have permission to use this command!"));
                if (sender instanceof Player p ) p.playSound(p, SoundUtils.ERROR_SOUND, 1f, 1f);
                return;
            }

            try {
                ConfigUtil.reloadConfig();
            } catch (ConfigurateException e) {
                GlyphChat.getInstance().getComponentLogger().error(text("Could not load configuration files!"), e);
                sender.sendMessage(ChatUtils.getAlertMessageFormat("An error occurred while reloading configuration files!"));
                if (sender instanceof Player p) p.playSound(p, SoundUtils.ERROR_SOUND, 1f, 1f);
                return;
            }

            GlyphManager.loadEmojis();

            sender.sendMessage(ChatUtils.getInfoMessageFormat("Successfully reloaded the configuration file!"));
            if (sender instanceof Player p) p.playSound(p, SoundUtils.SUCCESS_SOUND, 1f, 1f);
        });
        return false;
    }
}
