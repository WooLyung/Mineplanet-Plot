package woolyung.main.plot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.plugin.java.JavaPlugin;
import woolyung.main.MineplanetPlot;

public class PlotWorld
{
    private World world;
    private PlotGenerator generator;

    public PlotWorld(String name)
    {
        generator = new PlotGenerator(this);
        WorldCreator wc = new WorldCreator(name);
        wc.generator(generator);
        world = Bukkit.getServer().createWorld(wc);
    }

    public int getHeight()
    {
        return JavaPlugin.getProvidingPlugin(MineplanetPlot.class).getConfig().getInt("height");
    }

    public BlockData getDefaultWorldBlock(int x, int y, int z)
    {
        // 0 ~ 43까지의 좌표로 변환
        int x2 = x % 44;
        if (x2 < 0) x2 += 44;
        int z2 = z % 44;
        if (z2 < 0) z2 += 44;

        if (y == 0) // 기반암 층
            return Bukkit.createBlockData(Material.BEDROCK);
        else if (y <= getHeight() - 2) // 흙 층
            return Bukkit.createBlockData(Material.DIRT);
        else if (y == getHeight()) // 플롯 높이
        {
            if ((x2 <= 12 || x2 >= 32) && (z2 <= 12 || z2 >= 32)) // 플롯 잔디
                return Bukkit.createBlockData(Material.GRASS_BLOCK);
            else if ((x2 == 19 || x2 == 25) && (z2 <= 19 || z2 >= 25)
                    || (x2 <= 19 || x2 >= 25) && (z2 == 19 || z2 == 25))
                return Bukkit.createBlockData(Material.BIRCH_SLAB);
            else if (x2 == 13 && (z2 <= 12 || z2 >= 32)) // 서쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.WEST);
                return data;
            }
            else if (x2 == 31 && (z2 <= 12 || z2 >= 32)) // 동쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.EAST);
                return data;
            }
            else if (z2 == 13 && (x2 <= 12 || x2 >= 32)) // 북쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.NORTH);
                return data;
            }
            else if (z2 == 31 && (x2 <= 12 || x2 >= 32)) // 남쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.SOUTH);
                return data;
            }
            else if (x2 == 31 && z2 == 31) // 북동쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.SOUTH);
                ((Stairs)data).setShape(Stairs.Shape.OUTER_LEFT);
                return data;
            }
            else if (x2 == 13 && z2 == 31) // 북서쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.WEST);
                ((Stairs)data).setShape(Stairs.Shape.OUTER_LEFT);
                return data;
            }
            else if (x2 == 31 && z2 == 13) // 남동쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.EAST);
                ((Stairs)data).setShape(Stairs.Shape.OUTER_LEFT);
                return data;
            }
            else if (x2 == 13 && z2 == 13) // 남서쪽
            {
                BlockData data = Bukkit.createBlockData(Material.BIRCH_STAIRS);
                ((Directional)data).setFacing(BlockFace.NORTH);
                ((Stairs)data).setShape(Stairs.Shape.OUTER_LEFT);
                return data;
            }
        }
        else if (y == getHeight() - 1) // 길 높이
        {
            if ((x2 <= 13 || x2 >= 31) && (z2 <= 13 || z2 >= 31)) // 플롯 잔디 아래
                return Bukkit.createBlockData(Material.DIRT);
            else if ((x2 <= 19 || x2 >= 25) && (z2 <= 19 || z2 >= 25)) // 스킨 공간
                return Bukkit.createBlockData(Material.COARSE_DIRT);
            else if ((x2 <= 20 || x2 >= 24) && (z2 <= 20 || z2 >= 24)) // 가문비나무
                return Bukkit.createBlockData(Material.SPRUCE_PLANKS);
            else // 짙은 참나무
                return Bukkit.createBlockData(Material.DARK_OAK_PLANKS);
        }

        return Bukkit.createBlockData(Material.AIR);
    }
}