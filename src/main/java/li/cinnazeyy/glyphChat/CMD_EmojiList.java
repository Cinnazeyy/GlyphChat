package li.cinnazeyy.glyphChat;

import li.cinnazeyy.glyphChat.config.Emoji;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class CMD_EmojiList extends BukkitCommand {
    protected CMD_EmojiList(@NotNull String name) {
        super(name);
        this.description = "Lists all available emojis";
        this.usageMessage = "/emojis";
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        sender.sendMessage(text("Available emojis:", GREEN));
        List<Emoji> emojis = GlyphChat.EMOJI_MAP.values().stream()
                .distinct()
                .filter(e -> e.permission() == null || sender.hasPermission(e.permission()))
                .toList();

        for (Emoji emoji : emojis) {
            sender.sendMessage(text("- ", DARK_GRAY)
                    .append(text(emoji.symbol(), WHITE))
                    .append(text(" :" + emoji.keywords().getFirst() + ":", GRAY)));
        }
        return false;
    }
}
