package me.brynner.ascendiaPrisonCore.data;

import me.brynner.ascendiaPrisonCore.AscendiaPrisonCore;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final File dataFolder;

    public PlayerDataManager() {
        dataFolder = new File(AscendiaPrisonCore.getInstance().getDataFolder(), "data");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public PlayerData getData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), uuid -> {
            PlayerData data = new PlayerData(player);
            File file = new File(dataFolder, uuid + ".yml");
            data.loadFromFile(file);
            return data;
        });
    }

    public void removeData(Player player) {
        PlayerData data = playerDataMap.remove(player.getUniqueId());
        if (data != null) {
            File file = new File(dataFolder, player.getUniqueId() + ".yml");
            data.saveToFile(file);
        }
    }

    public void saveAll() {
        for (Map.Entry<UUID, PlayerData> entry : playerDataMap.entrySet()) {
            File file = new File(dataFolder, entry.getKey() + ".yml");
            entry.getValue().saveToFile(file);
        }
    }

    public void clearAll() {
        playerDataMap.clear();
    }
}
