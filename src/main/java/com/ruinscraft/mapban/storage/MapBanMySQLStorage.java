package com.ruinscraft.mapban.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MapBanMySQLStorage {

    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public MapBanMySQLStorage(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    private Connection createConnection() throws SQLException {
        String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public CompletableFuture<Void> createTables() {
        return CompletableFuture.runAsync(() -> {

        });
    }

    public CompletableFuture<List<BannedMap>> queryBannedMaps() {
        return CompletableFuture.supplyAsync(() -> {
            List<BannedMap> bannedMaps = new ArrayList<>();


            return bannedMaps;
        });
    }

    public CompletableFuture<Void> deleteBannedMap(int mapId) {
        return CompletableFuture.runAsync(() -> {

        });
    }

    public CompletableFuture<Void> insertBannedMap(int mapId) {
        return CompletableFuture.runAsync(() -> {

        });
    }

    public CompletableFuture<MapCompareResult> queryMapCompareResult(int mapIdA, int mapIdB) {
        return CompletableFuture.supplyAsync(() -> {
            return null;
        });
    }

}
