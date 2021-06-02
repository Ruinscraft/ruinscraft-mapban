package com.ruinscraft.mapban.listener;

import com.ruinscraft.mapban.MapBanManager;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

public class ChunkLoadListener implements Listener {

    private MapBanManager mapBanManager;

    public ChunkLoadListener(MapBanManager mapBanManager) {
        this.mapBanManager = mapBanManager;
    }

    @EventHandler
    public void onChunkLoadCheckForBannedMapsInItemFrames(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        for (Entity entity : chunk.getEntities()) {
            if (entity instanceof ItemFrame itemFrame) {
                ItemStack itemStack = itemFrame.getItem();

                if (itemStack.getItemMeta() instanceof MapMeta mapMeta) {
                    if (mapBanManager.isBanned(mapMeta.getMapId())) {
                        itemFrame.setItem(null);
                        mapBanManager.logRemovedMap(mapMeta.getMapId(), itemFrame.getLocation());
                    }
                }
            }
        }
    }

}
