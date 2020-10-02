package woolyung.main.plot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Stairs;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotLocData;

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

    public World getWorld() {
        return world;
    }

    public int getHeight()
    {
        return MineplanetPlot.instance.getConfig().getInt("height");
    }

    public PlotLocData getPlotLocData(int x, int z)
    {
        PlotLocData plotLocData = new PlotLocData();

        int plotX = 0;
        if (x + 19 >= 0) plotX = (x + 19) / 44;
        else plotX = ((x + 20) / 44) - 1;
        plotLocData.plotLocX = plotX;

        int plotZ = 0;
        if (z + 19 >= 0) plotZ = (z + 19) / 44;
        else plotZ = ((z + 20) / 44) - 1;
        plotLocData.plotLocZ = plotZ;

        int innerX = (x + 19) % 44;
        if (innerX < 0) innerX += 44;
        plotLocData.plotInnerLocX = innerX;

        int innerZ = (z + 19) % 44;
        if (innerZ < 0) innerZ += 44;
        plotLocData.plotInnerLocZ = innerZ;

        if (innerX >= 7 && innerX <= 31 && innerZ >= 7 && innerZ <= 31)
            plotLocData.plotSection = PlotLocData.PLOT_SECTION.PLOT;
        else if (innerX >= 6 && innerX <= 32 && innerZ >= 6 && innerZ <= 32)
            plotLocData.plotSection = PlotLocData.PLOT_SECTION.INNER_LINE;
        else if (innerX >= 1 && innerX <= 37 && innerZ >= 1 && innerZ <= 37)
            plotLocData.plotSection = PlotLocData.PLOT_SECTION.SKIN;
        else if (innerX <= 38 && innerZ <= 38)
            plotLocData.plotSection = PlotLocData.PLOT_SECTION.OUTER_LINE;
        else if (innerX >= 39 && innerZ <= 38)
            plotLocData.plotSection = PlotLocData.PLOT_SECTION.ROAD_LEFT;
        else if (innerZ >= 39 && innerX <= 38)
            plotLocData.plotSection = PlotLocData.PLOT_SECTION.ROAD_TOP;
        else
            plotLocData.plotSection = PlotLocData.PLOT_SECTION.ROAD_EDGE;

        if (innerX <= 6)
        {
            if (innerZ <= 6) plotLocData.extendSection = PlotLocData.EXTEND_SECTION.LEFT_BOTTOM;
            else if (innerZ <= 31) plotLocData.extendSection = PlotLocData.EXTEND_SECTION.BOTTOM;
            else plotLocData.extendSection = PlotLocData.EXTEND_SECTION.RIGHT_BOTTOM;
        }
        else if (innerX <= 31)
        {
            if (innerZ <= 6) plotLocData.extendSection = PlotLocData.EXTEND_SECTION.LEFT;
            else if (innerZ <= 31) plotLocData.extendSection = PlotLocData.EXTEND_SECTION.CENTER;
            else plotLocData.extendSection = PlotLocData.EXTEND_SECTION.RIGHT;
        }
        else
        {
            if (innerZ <= 6) plotLocData.extendSection = PlotLocData.EXTEND_SECTION.LEFT_TOP;
            else if (innerZ <= 31) plotLocData.extendSection = PlotLocData.EXTEND_SECTION.TOP;
            else plotLocData.extendSection = PlotLocData.EXTEND_SECTION.RIGHT_TOP;
        }

        return plotLocData;
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