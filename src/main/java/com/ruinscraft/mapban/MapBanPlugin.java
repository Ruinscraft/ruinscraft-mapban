package com.ruinscraft.mapban;

import com.ruinscraft.mapban.command.BanMapCommand;
import com.ruinscraft.mapban.command.SimilarMapsCommand;
import com.ruinscraft.mapban.command.TPMapCommand;
import com.ruinscraft.mapban.command.UnbanMapCommand;
import com.ruinscraft.mapban.listener.ChunkLoadListener;
import com.ruinscraft.mapban.listener.PlayerListener;
import com.ruinscraft.mapban.storage.MapBanMySQLStorage;
import org.bukkit.plugin.java.JavaPlugin;

public class MapBanPlugin extends JavaPlugin {

    private MapBanManager mapBanManager;

    public MapBanManager getMapBanManager() {
        return mapBanManager;
    }

    @Override
    public void onEnable() {
        String mysqlHost = getConfig().getString("mysql.host");
        int mysqlPort = getConfig().getInt("mysql.port");
        String mysqlDatabase = getConfig().getString("mysql.database");
        String mysqlUsername = getConfig().getString("mysql.username");
        String mysqlPassword = getConfig().getString("mysql.password");
        MapBanMySQLStorage mapBanStorage = new MapBanMySQLStorage(mysqlHost, mysqlPort, mysqlDatabase, mysqlUsername, mysqlPassword);
        mapBanStorage.createTables().join();

        mapBanManager = new MapBanManager(this, mapBanStorage);
        mapBanManager.initCache().join();
        getLogger().info("Loaded " + mapBanManager.getBanCacheSize() + " banned maps into memory.");

        getServer().getPluginManager().registerEvents(new ChunkLoadListener(mapBanManager), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getCommand("banmap").setExecutor(new BanMapCommand(mapBanManager));
        getCommand("unbanmap").setExecutor(new UnbanMapCommand(this));
        getCommand("tpmap").setExecutor(new TPMapCommand(this));
        getCommand("similarmaps").setExecutor(new SimilarMapsCommand(this));
    }

}
