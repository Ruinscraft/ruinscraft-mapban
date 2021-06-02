package com.ruinscraft.mapban.storage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
            try (Connection connection = createConnection()) {
                try (Statement statement = connection.createStatement()) {
                    statement.addBatch(
                            "CREATE TABLE IF NOT EXISTS map_bans (map_id INT NOT NULL, banned_at BIGINT NOT NULL, banned_by VARCHAR(36) NOT NULL, PRIMARY KEY (map_id));");
                    statement.addBatch(
                            "CREATE TABLE IF NOT EXISTS map_compare_results (map_id_a INT NOT NULL, map_id_b INT NOT NULL, similar BOOL, UNIQUE (map_id_a, map_id_b));");
                    statement.executeBatch();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<List<BannedMap>> queryBannedMaps() {
        return CompletableFuture.supplyAsync(() -> {
            List<BannedMap> bannedMaps = new ArrayList<>();

            try (Connection connection = createConnection()) {
                try (Statement statement = connection.createStatement()) {
                    try (ResultSet resultSet = statement.executeQuery("SELECT * FROM map_bans")) {
                        while (resultSet.next()) {
                            int mapId = resultSet.getInt("map_id");
                            long bannedAt = resultSet.getLong("banned_at");
                            UUID bannedBy;
                            try {
                                bannedBy = UUID.fromString(resultSet.getString("banned_by"));
                            } catch (Exception e) {
                                bannedBy = null;
                            }
                            BannedMap bannedMap = new BannedMap(mapId, bannedAt, bannedBy);
                            bannedMaps.add(bannedMap);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return bannedMaps;
        });
    }

    public CompletableFuture<Void> deleteBannedMap(int mapId) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = createConnection()) {
                try (PreparedStatement delete = connection.prepareStatement("DELETE FROM map_bans WHERE map_id = ?;")) {
                    delete.setInt(1, mapId);
                    delete.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<Void> insertBannedMap(BannedMap bannedMap) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = createConnection()) {
                try (PreparedStatement insert = connection.prepareStatement("INSERT INTO map_bans (map_id, banned_at, banned_by) VALUES (?, ?, ?);")) {
                    insert.setInt(1, bannedMap.getMapId());
                    insert.setLong(2, bannedMap.getBannedAt());
                    insert.setString(3, bannedMap.getBannedBy().toString());
                    insert.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<MapCompareResult> queryMapCompareResult(int mapIdA, int mapIdB) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = createConnection()) {
                try (PreparedStatement query = connection.prepareStatement("SELECT * FROM map_compare_results WHERE map_id_a = ? AND map_id_b = ?;")) {
                    query.setInt(1, mapIdA);
                    query.setInt(2, mapIdB);

                    try (ResultSet resultSet = query.executeQuery()) {
                        while (resultSet.next()) {
                            boolean similar = resultSet.getBoolean("similar");
                            return new MapCompareResult(mapIdA, mapIdB, similar);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    public CompletableFuture<Void> upsertMapCompareResult(MapCompareResult mapCompareResult) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = createConnection()) {
                try (PreparedStatement upsert = connection.prepareStatement(
                        "INSERT INTO map_compare_results (map_id_a, map_id_b, similar) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE similar = ?;")) {
                    upsert.setInt(1, mapCompareResult.getMapIdA());
                    upsert.setInt(2, mapCompareResult.getMapIdB());
                    upsert.setBoolean(3, mapCompareResult.isSimilar());
                    upsert.setBoolean(4, mapCompareResult.isSimilar());
                    upsert.execute();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
