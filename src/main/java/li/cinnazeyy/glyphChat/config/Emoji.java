package li.cinnazeyy.glyphChat.config;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ConfigSerializable
public record Emoji(
        @Setting("symbol")
        char symbol,
        @Setting("keywords")
        List<String> keywords,
        @Setting("permission")
        String permission) {
    Optional<String> getPermission() {
        return Optional.ofNullable(permission);
    }

    @Override
    public @NotNull String toString() {
        return "Symbol: '" + symbol + "' Permission: " + permission + " Keywords: " + Arrays.toString(keywords.toArray());
    }
}
