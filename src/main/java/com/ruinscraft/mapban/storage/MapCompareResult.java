package com.ruinscraft.mapban.storage;

public class MapCompareResult {

    private int mapIdA;
    private int mapIdB;
    private boolean similar;

    public MapCompareResult(int mapIdA, int mapIdB, boolean similar) {
        this.mapIdA = mapIdA;
        this.mapIdB = mapIdB;
        this.similar = similar;
    }

    public int getMapIdA() {
        return mapIdA;
    }

    public int getMapIdB() {
        return mapIdB;
    }

    public boolean isSimilar() {
        return similar;
    }

}
