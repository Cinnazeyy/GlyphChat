package li.cinnazeyy.glyphChat.commands;

import li.cinnazeyy.glyphChat.config.ConfigUtil;
import li.cinnazeyy.glyphChat.config.Emoji;
import li.cinnazeyy.glyphChat.core.GlyphManager;
import li.cinnazeyy.glyphChat.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class CMD_EmojiList extends BukkitCommand {
    public CMD_EmojiList(@NotNull String name) {
        super(name);
        this.description = "Lists all available emojis";
        this.usageMessage = "/emojis";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        CompletableFuture.runAsync(() -> {
            List<Emoji> emojis = GlyphManager.EMOJI_MAP.values().stream()
                    .distinct()
                    .filter(e -> e.permission() == null || sender.hasPermission(e.permission()))
                    .toList();
            String emojiIndicator = ConfigUtil.getMainConfig().emojiIndicator();

            sender.sendMessage(ChatUtils.getInfoMessageFormat("Available emojis:"));
            for (Emoji emoji : emojis) {
                sender.sendMessage(text("- ", DARK_GRAY)
                        .append(text(emoji.symbol(), WHITE))
                        .append(text(" " + emojiIndicator + emoji.keywords().getFirst() + emojiIndicator, GRAY)));
            }
        });
        return false;
    }
}
