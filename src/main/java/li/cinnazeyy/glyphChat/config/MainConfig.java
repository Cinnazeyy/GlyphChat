package li.cinnazeyy.glyphChat.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public record MainConfig(
        @Setting("emoji-indicator")
        String emojiIndicator,

        @Setting("chat-format")
        ChatSection chatSection
) {
}
