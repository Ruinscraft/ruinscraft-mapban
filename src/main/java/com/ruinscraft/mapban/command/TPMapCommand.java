package com.ruinscraft.mapban.command;

import com.ruinscraft.mapban.MapBanPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPMapCommand implements CommandExecutor {

    private MapBanPlugin mapBanPlugin;

    public TPMapCommand(MapBanPlugin mapBanPlugin) {
        this.mapBanPlugin = mapBanPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        return true;
    }

}
