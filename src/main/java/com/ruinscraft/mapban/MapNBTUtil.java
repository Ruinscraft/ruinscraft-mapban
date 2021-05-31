package com.ruinscraft.mapban;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.ByteArrayTag;
import net.querz.nbt.tag.CompoundTag;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class MapNBTUtil {

    public static CompletableFuture<ByteArrayTag> readColors(String worldName, int mapId) {
        return CompletableFuture.supplyAsync(() -> {
            String mapFileName = "map_" + mapId + ".dat";
            File mapFile = new File(worldName + "/data", mapFileName);

            if (!mapFile.exists()) {
                return null;
            }

            NamedTag namedTag;

            try {
                namedTag = NBTUtil.read(mapFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            if (namedTag.getTag() instanceof CompoundTag) {
                CompoundTag root = (CompoundTag) namedTag.getTag();
                if (root.get("data") instanceof CompoundTag) {
                    CompoundTag data = (CompoundTag) root.get("data");
                    if (data.get("colors") instanceof ByteArrayTag) {
                        return (ByteArrayTag) data.get("colors");
                    }
                }
            }

            return null;
        });
    }

}
