package kiul.kiulsmputilitiesv2.antipieray;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class AntiPieRay implements Listener {

    Material[] pieChartMaterials =
            {
                    Material.ENDER_CHEST,
                    Material.ENCHANTING_TABLE,
                    Material.JUKEBOX,
                    Material.SHULKER_BOX,
                    Material.BELL

            };
    int pieChartChunkSpacing = 6;

    @org.bukkit.event.EventHandler
    public void onChunkLoad(ChunkLoadEvent event)
    {
        if (!event.isNewChunk()) {
            return;
        }


        int chunkX = event.getChunk().getX();
        int chunkZ = event.getChunk().getZ();
        if (chunkX % pieChartChunkSpacing != 0 || chunkZ % pieChartChunkSpacing != 0) {
            return;
        }

        World world = event.getWorld();

        if (world.getEnvironment() != World.Environment.NORMAL) {
            return;
        }

        BlockData bedrockBlockData = Material.BEDROCK.createBlockData();

        for (int i = 0; i < pieChartMaterials.length; i++)
        {
            Location loc = new Location(world, chunkX * 16 + i, -64, chunkZ * 16);
            BlockData pieChartBlockData = pieChartMaterials[i].createBlockData();
            world.setBlockData(loc, pieChartBlockData);

            loc = new Location(world, chunkX * 16 + i, -63, chunkZ * 16);
            world.setBlockData(loc, bedrockBlockData);
        }
    }
}
