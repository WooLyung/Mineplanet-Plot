package woolyung.main.plot;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.PlotDatabase;
import woolyung.main.plot.Data.PlotDataEx;

public class PlotManager
{
    MineplanetPlot plugin;
    PlotDatabase database;
    PlotWorld world;

    public PlotManager(MineplanetPlot plugin, PlotDatabase database, PlotWorld world)
    {
        this.plugin = plugin;
        this.database = database;
        this.world = world;
    }

    public int buyPlot(Player player, int x, int z) // 플롯 구매
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);

        if (plotDataEx != null) return 1; // 주인이 있는 플롯

        MineplanetPlot.instance.getPlotDatabase().insertPlotData(x, z);
        MineplanetPlot.instance.getPlotDatabase().insertPlayerPlotData(x, z, player.getUniqueId().toString(), "owner");
        return 0;
    }

    public void clearPlot(int x, int z)
    {
        // 플롯 초기화
    }

    public void movePlot(int fromX, int fromZ, int toX, int toZ)
    {
        // 플롯 이사
    }

    public void deletePlot(int x, int z)
    {
        // 플롯 삭제
    }

    public void deleteSkin(int x, int z)
    {
        // 스킨 제거
    }

    public int mergePlot2(int x1, int z1, int x2, int z2) // 두 플롯 병합
    {
        PlotDataEx plotData1 = database.getPlotDataEx(x1, z1);
        PlotDataEx plotData2 = database.getPlotDataEx(x2, z2);

        if (plotData1 == null || plotData2 == null) return 1; // 주인이 없는 플롯
        if (plotData1.owner.compareTo(plotData2.owner) != 0) return 2; // 주인이 다른 플롯
        int distance = Math.abs(x1 - x2) + Math.abs(z1 - z2);
        if (distance != 1) return 3; // 인접한 플롯이 아님
        if (database.getIsExtended(x1, z1, x2, z2)) return 4; // 이미 병합된 플롯

        // 스킨 제거
        deleteSkin(x1, z1);
        deleteSkin(x2, z2);

        if (plotData1.extend.compareTo(plotData2.extend) == 0) // 확장 기준 플롯이 같을 경우
        {
            database.insertExtend2(x1, z1, x2, z2); // 확장-2 데이터베이스에 추가
        }
        else // 확장 기준 플롯이 다를 경우
        {
            String[] extend1 = plotData1.extend.split(":");
            String[] extend2 = plotData2.extend.split(":");
            int extend1_x = Integer.parseInt(extend1[0]);
            int extend1_z = Integer.parseInt(extend1[1]);
            int extend2_x = Integer.parseInt(extend2[0]);
            int extend2_z = Integer.parseInt(extend2[1]);

            // 확장 기준 플롯 택1
            String extendPlot, deletePlot;
            if (extend1_x > extend2_x)
            {
                extendPlot = plotData1.extend;
                deletePlot = plotData2.extend;
            }
            else if (extend1_x < extend2_x)
            {
                extendPlot = plotData2.extend;
                deletePlot = plotData1.extend;
            }
            else
            {
                if (extend1_z > extend2_z)
                {
                    extendPlot = plotData1.extend;
                    deletePlot = plotData2.extend;
                }
                else
                {
                    extendPlot = plotData2.extend;
                    deletePlot = plotData1.extend;
                }
            }

            String[] extendPlotSplit = extendPlot.split(":");
            String[] deletePlotSplit = deletePlot.split(":");
            int extendPlotX = Integer.parseInt(extendPlotSplit[0]), extendPlotZ = Integer.parseInt(extendPlotSplit[1]);
            int deletePlotX = Integer.parseInt(deletePlotSplit[0]), deletePlotZ = Integer.parseInt(deletePlotSplit[1]);

            database.insertExtend2(x1, z1, x2, z2); // 확장-2 데이터베이스에 추가
            database.changeExtends(deletePlotX, deletePlotZ, extendPlotX, extendPlotZ); // 확장 기준 플롯을 모두 변경
            database.deletePlotData(deletePlotX, deletePlotZ); // 필요가 없어진 플롯 데이터 삭제
        }

        // 블럭 채우기
        int centerX = x1 * 44;
        int centerZ = z1 * 44;

        BlockData west = Bukkit.createBlockData(Material.BIRCH_STAIRS);
        ((Directional)west).setFacing(BlockFace.WEST);
        BlockData east = Bukkit.createBlockData(Material.BIRCH_STAIRS);
        ((Directional)east).setFacing(BlockFace.EAST);
        BlockData north = Bukkit.createBlockData(Material.BIRCH_STAIRS);
        ((Directional)north).setFacing(BlockFace.NORTH);
        BlockData south = Bukkit.createBlockData(Material.BIRCH_STAIRS);
        ((Directional)south).setFacing(BlockFace.SOUTH);

        if (z1 > z2) // 아래
        {
            for (int z = centerZ - 13; z >= centerZ - 31; z--)
            {
                for (int x = centerX - 12; x <= centerX + 12; x++)
                {
                    for (int y = plugin.getConfig().getInt("height") - 3; y <= plugin.getConfig().getInt("height") - 1; y++)
                    {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }

                if (z != centerZ - 13 && z != centerZ - 31)
                {
                    world.getWorld().getBlockAt(centerX - 13, plugin.getConfig().getInt("height"), z).setBlockData(east);
                    world.getWorld().getBlockAt(centerX + 13, plugin.getConfig().getInt("height"), z).setBlockData(west);
                    world.getWorld().getBlockAt(centerX - 13, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);
                    world.getWorld().getBlockAt(centerX + 13, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);

                    if (z >= centerZ - 24 && z <= centerZ - 20)
                    {
                        world.getWorld().getBlockAt(centerX - 19, plugin.getConfig().getInt("height"), z).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(centerX + 19, plugin.getConfig().getInt("height"), z).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(centerX - 19, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);
                        world.getWorld().getBlockAt(centerX + 19, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);
                    }
                }
            }
            for (int z = centerZ - 14; z >= centerZ - 30; z--)
            {
                for (int x = centerX - 18; x <= centerX - 14; x++)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }

                for (int x = centerX + 18; x >= centerX + 14; x--)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }
            }
        }
        else if (z1 < z2) // 위
        {
            for (int z = centerZ + 13; z <= centerZ + 31; z++)
            {
                for (int x = centerX - 12; x <= centerX + 12; x++)
                {
                    for (int y = plugin.getConfig().getInt("height") - 3; y <= plugin.getConfig().getInt("height") - 1; y++)
                    {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }

                if (z != centerZ + 13 && z != centerZ + 31)
                {
                    world.getWorld().getBlockAt(centerX - 13, plugin.getConfig().getInt("height"), z).setBlockData(east);
                    world.getWorld().getBlockAt(centerX + 13, plugin.getConfig().getInt("height"), z).setBlockData(west);
                    world.getWorld().getBlockAt(centerX - 13, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);
                    world.getWorld().getBlockAt(centerX + 13, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);
                    if (z <= centerZ + 24 && z >= centerZ + 20)
                    {
                        world.getWorld().getBlockAt(centerX - 19, plugin.getConfig().getInt("height"), z).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(centerX + 19, plugin.getConfig().getInt("height"), z).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(centerX - 19, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);
                        world.getWorld().getBlockAt(centerX + 19, plugin.getConfig().getInt("height") - 1, z).setType(Material.DIRT);
                    }
                }
            }
            for (int z = centerZ + 14; z <= centerZ + 30; z++)
            {
                for (int x = centerX - 18; x <= centerX - 14; x++)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }

                for (int x = centerX + 18; x >= centerX + 14; x--)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }
            }
        }
        else if (x1 > x2) // 오른쪽
        {
            for (int x = centerX - 13; x >= centerX - 31; x--)
            {
                for (int z = centerZ - 12; z <= centerZ + 12; z++)
                {
                    for (int y = plugin.getConfig().getInt("height") - 3; y <= plugin.getConfig().getInt("height") - 1; y++)
                    {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }

                if (x != centerX - 13 && x != centerX - 31)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ - 13).setBlockData(south);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ + 13).setBlockData(north);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ - 13).setType(Material.DIRT);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ + 13).setType(Material.DIRT);
                    if (x >= centerX - 24 && x <= centerX - 20)
                    {
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ - 19).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ + 19).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ - 19).setType(Material.DIRT);
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ + 19).setType(Material.DIRT);
                    }
                }
            }
            for (int x = centerX - 14; x >= centerX - 30; x--)
            {
                for (int z = centerZ - 18; z <= centerZ - 14; z++)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }

                for (int z = centerZ + 18; z >= centerZ + 14; z--)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }
            }
        }
        else if (x1 < x2) // 왼쪽
        {
            for (int x = centerX + 13; x <= centerX + 31; x++)
            {
                for (int z = centerZ - 12; z <= centerZ + 12; z++)
                {
                    for (int y = plugin.getConfig().getInt("height") - 3; y <= plugin.getConfig().getInt("height") - 1; y++)
                    {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }

                if (x != centerX + 13 && x != centerX + 31)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ - 13).setBlockData(south);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ + 13).setBlockData(north);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ - 13).setType(Material.DIRT);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ + 13).setType(Material.DIRT);
                    if (x <= centerX + 24 && x >= centerX + 20)
                    {
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ - 19).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), centerZ + 19).setType(Material.BIRCH_SLAB);
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ - 19).setType(Material.DIRT);
                        world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, centerZ + 19).setType(Material.DIRT);
                    }
                }
            }
            for (int x = centerX + 14; x <= centerX + 30; x++)
            {
                for (int z = centerZ - 18; z <= centerZ - 14; z++)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }

                for (int z = centerZ + 18; z >= centerZ + 14; z--)
                {
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.AIR);
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height") - 1, z).setType(Material.COARSE_DIRT);
                }
            }
        }

        return 0;
    }

    public int mergePlot4(int x1, int z1, int x2, int z2)
    {
        PlotDataEx plotData1 = database.getPlotDataEx(x1, z1);
        PlotDataEx plotData2 = database.getPlotDataEx(x1, z2);
        PlotDataEx plotData3 = database.getPlotDataEx(x2, z1);
        PlotDataEx plotData4 = database.getPlotDataEx(x2, z2);

        if (plotData1 == null || plotData2 == null || plotData3 == null || plotData4 == null) return 1; // 주인이 없는 플롯
        if (plotData1.owner.compareTo(plotData2.owner) != 0 || plotData1.owner.compareTo(plotData3.owner) != 0 || plotData1.owner.compareTo(plotData4.owner) != 0) return 2; // 주인이 다른 플롯
        int distance = Math.abs(x1 - x2) * Math.abs(z1 - z2);
        if (distance != 1) return 3; // 인접한 플롯이 아님
        if (database.getIsExtended4(x1, z1, x2, z2)) return 4; // 이미 병합된 플롯
        if (!database.getIsExtended(x1, z1, x1, z2) || !database.getIsExtended(x1, z1, x2, z1) || !database.getIsExtended(x2, z2, x1, z2) || !database.getIsExtended(x2, z2, x2, z1)) return 5; // 확장이 다 안된 플롯

        // 스킨 제거
        deleteSkin(x1, z1);

        // 데이터 입력
        database.insertExtend4(x1, z1, x2, z2);

        int dirX = x2 - x1;
        int dirZ = z2 - z1;
        int centerX = x1 * 44 + dirX * 22, centerZ = z1 * 44 + dirZ * 22;

        for (int x = centerX - 9; x <= centerX + 9; x++)
        {
            for (int z = centerZ - 9; z <= centerZ + 9; z++)
            {
                for (int y = plugin.getConfig().getInt("height") - 3; y < plugin.getConfig().getInt("height"); y++)
                {
                    world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                }
                world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
            }
        }

        return 0;
    }

    public void detachPlot2(int x1, int z1, int x2, int z2)
    {
        // 두 플롯 분리
    }

    public void detachPlot4(int x1, int z1, int x2, int z2)
    {
        // 네 플롯 분리
    }

    public void changeSkin(int x, int z, int slot, int skin)
    {
        // 스킨 설정
    }

    public void settingPlotInt(int x, int z, String setting, int value)
    {
        // 정수 설정
    }

    public void settingPlotString(int x, int z, String setting, String value)
    {
        // 문자열 설정
    }
}