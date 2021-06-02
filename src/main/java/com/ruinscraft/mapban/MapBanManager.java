package com.ruinscraft.mapban;

import com.ruinscraft.mapban.storage.BannedMap;
import com.ruinscraft.mapban.storage.MapBanMySQLStorage;
import org.bukkit.Location;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class MapBanManager {

    private MapBanPlugin mapBanPlugin;
    private MapBanMySQLStorage mapBanStorage;
    private Map<Integer, BannedMap> bannedMapsCache;

    public MapBanManager(MapBanPlugin mapBanPlugin, MapBanMySQLStorage mapBanStorage) {
        this.mapBanPlugin = mapBanPlugin;
        this.mapBanStorage = mapBanStorage;
        bannedMapsCache = new ConcurrentHashMap<>();
    }

    public synchronized CompletableFuture<Void> initCache() {
        return mapBanStorage.queryBannedMaps().thenAccept(bannedMaps ->
                bannedMaps.forEach(bannedMap -> bannedMapsCache.put(bannedMap.getMapId(), bannedMap)));
    }

    public int getBanCacheSize() {
        return bannedMapsCache.size();
    }

    public boolean isBanned(int mapId) {
        return bannedMapsCache.containsKey(mapId);
    }

    public BannedMap getBannedMapInfo(int mapId) {
        return bannedMapsCache.get(mapId);
    }

    public void banMap(BannedMap bannedMap) {
        bannedMapsCache.put(bannedMap.getMapId(), bannedMap);
        mapBanStorage.insertBannedMap(bannedMap);
    }

    public void unbanMap(int mapId) {
        bannedMapsCache.remove(mapId);
        mapBanStorage.deleteBannedMap(mapId);
    }

    public void logRemovedMap(int mapId, Location location) {
        String locationString = location.getWorld().getName()
                + ":" + location.getBlockX()
                + "," + location.getBlockY()
                + "," + location.getBlockZ();
        mapBanPlugin.getLogger().info("Removed banned map [id=" + mapId + "] from world location [" + locationString + "]");
    }

}
