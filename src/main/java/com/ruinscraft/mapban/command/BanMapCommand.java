package com.ruinscraft.mapban.command;

import com.ruinscraft.mapban.MapBanManager;
import com.ruinscraft.mapban.storage.BannedMap;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class BanMapCommand implements CommandExecutor {

    private MapBanManager mapBanManager;

    public BanMapCommand(MapBanManager mapBanManager) {
        this.mapBanManager = mapBanManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        ItemStack mainHandItem = player.getInventory().getItemInMainHand();

        if (mainHandItem.getType() != Material.FILLED_MAP) {
            player.sendMessage(ChatColor.RED + "You must be holding a filled map.");
            return true;
        }

        if (!(mainHandItem.getItemMeta() instanceof MapMeta)) {
            player.sendMessage(ChatColor.RED + "Could not get meta for this map.");
            return true;
        }

        MapMeta mapMeta = (MapMeta) mainHandItem.getItemMeta();
        int mapId = mapMeta.getMapId();

        if (mapBanManager.isBanned(mapId)) {
            player.sendMessage(ChatColor.RED + "This map is already banned.");
        } else {
            BannedMap bannedMap = new BannedMap(mapId, System.currentTimeMillis(), player.getUniqueId());
            mapBanManager.banMap(bannedMap);
            player.sendMessage(ChatColor.GOLD + "Banned map with ID: " + mapId);
        }

        return true;
    }

}
