package me.brynner.ascendiaPrisonCore.data;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public PlayerData getData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), PlayerData::new);
    }

    public void removeData(Player player) {
        playerDataMap.remove(player.getUniqueId());
    }

    public boolean hasData(Player player) {
        return playerDataMap.containsKey(player.getUniqueId());
    }
}
