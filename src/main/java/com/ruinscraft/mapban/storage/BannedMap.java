package com.ruinscraft.mapban.storage;

import java.util.UUID;

public class BannedMap {

    private int mapId;
    private long bannedAt;
    private UUID bannedBy;

    public BannedMap(int mapId, long bannedAt, UUID bannedBy) {
        this.mapId = mapId;
        this.bannedAt = bannedAt;
        this.bannedBy = bannedBy;
    }

    public int getMapId() {
        return mapId;
    }

    public long getBannedAt() {
        return bannedAt;
    }

    public UUID getBannedBy() {
        return bannedBy;
    }

}
