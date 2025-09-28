package li.cinnazeyy.glyphChat;

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.kyori.adventure.text.Component.text;

public class ChatListener implements Listener {
    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Component message = event.originalMessage();
        String messageContent = message.insertion();
        if (messageContent == null) return;

        Pattern pattern = Pattern.compile(":(\\w+):");

        final Component replaced = message.replaceText(pattern, builder -> {
            final Matcher matcher = pattern.matcher(builder.content());
            // Use StringBuffer for efficient replacement
            StringBuffer result = new StringBuffer();
            while (matcher.find()) {
                String placeholder = matcher.group(1); // e.g., "skull"
                Emoji emoji = GlyphChat.EMOJI_MAP.get(placeholder);

                if (emoji != null) {
                    matcher.appendReplacement(result, Matcher.quoteReplacement(emoji.character() + ""));
                } else {
                    // If emoji not found, keep the original text
                    matcher.appendReplacement(result, Matcher.quoteReplacement(matcher.group()));
                }
            }
            matcher.appendTail(result);
            return text(result.toString());
        });
        event.message(message);
    }
}
