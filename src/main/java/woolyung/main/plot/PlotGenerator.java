package woolyung.main.plot;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class PlotGenerator extends ChunkGenerator
{
    private PlotWorld plotWorld;
    public PlotGenerator(PlotWorld plotWorld)
    {
        this.plotWorld = plotWorld;
    }

    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome)
    {
        ChunkData chunk = createChunkData(world);

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                biome.setBiome(x, 0, z, Biome.PLAINS);

                for (int y = 0; y <= plotWorld.getHeight(); y++)
                {
                    int x2 = chunkX * 16 + x;
                    int z2 = chunkZ * 16 + z;

                    chunk.setBlock(x, y, z, plotWorld.getDefaultWorldBlock(x2, y, z2));
                }
            }
        }

        return chunk;
    }
}