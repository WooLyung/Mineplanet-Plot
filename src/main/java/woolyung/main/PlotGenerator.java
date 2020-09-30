package woolyung.main;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class PlotGenerator extends ChunkGenerator
{
    public PlotGenerator()
    {
    }

    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome)
    {
        ChunkData chunk = createChunkData(world);

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                chunk.setBlock(x, 1, z, Material.BEDROCK);
            }
        }

        return chunk;
    }
}