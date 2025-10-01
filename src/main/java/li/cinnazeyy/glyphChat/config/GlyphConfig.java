package li.cinnazeyy.glyphChat.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public record GlyphConfig(
        @Setting("emoji")
        List<Emoji> emojis
) {
}
