package com.ruinscraft.mapban;

import com.ruinscraft.mapban.listener.ChunkLoadListener;
import com.ruinscraft.mapban.listener.PlayerListener;
import com.ruinscraft.mapban.storage.MapBanMySQLStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class MapBanPlugin extends JavaPlugin {

    private MapBanMySQLStorage mapBanStorage;

    public MapBanMySQLStorage getMapBanStorage() {
        return mapBanStorage;
    }

    @Override
    public void onEnable() {
        String mysqlHost = getConfig().getString("mysql.host");
        int mysqlPort = getConfig().getInt("mysql.port");
        String mysqlDatabase = getConfig().getString("mysql.database");
        String mysqlUsername = getConfig().getString("mysql.username");
        String mysqlPassword = getConfig().getString("mysql.password");
        mapBanStorage = new MapBanMySQLStorage(mysqlHost, mysqlPort, mysqlDatabase, mysqlUsername, mysqlPassword);
        mapBanStorage.createTables().join();

        getServer().getPluginManager().registerEvents(new ChunkLoadListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getCommand("banmap").setExecutor(new BanMapCommand(this));
    }

}
