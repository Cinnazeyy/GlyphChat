package li.cinnazeyy.glyphChat.event;

import li.cinnazeyy.glyphChat.utils.ChatCompletionUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ChatCompletionUtil.refreshPlayerCompletions(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        ChatCompletionUtil.clearPlayerCompletions(event.getPlayer());
    }
}
