package woolyung.main.plot;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.PlotDatabase;
import woolyung.main.plot.Data.PlotDataEx;

import java.util.ArrayList;
import java.util.HashSet;

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

    public int movePlot(int fromX, int fromZ, int toX, int toZ)
    {
        PlotDataEx plotDataFrom = database.getPlotDataEx(fromX, fromZ);
        PlotDataEx plotDataTo = database.getPlotDataEx(toX, toZ);

        if (plotDataFrom == null) return 1; // 주인이 없는 플롯
        if (plotDataTo != null) return 2; // 이동 위치에 플롯이 있음

        // 데이터 생성
        database.insertPlotData(toX + ":" + toZ, plotDataFrom);
        database.insertPlot(toX, toZ);

        // 블럭 옮김
        int centerFromX = fromX * 44, centerFromZ = fromZ * 44;
        int centerToX = toX * 44, centerToZ = toZ * 44;
        for (int ix = -12; ix <= 12; ix++)
        {
            for (int iz = -12; iz <= 12; iz++)
            {
                for (int iy = 0; iy < 256; iy++)
                {
                    world.getWorld().getBlockAt(ix + centerToX, iy, iz + centerToZ).setBlockData(world.getWorld().getBlockAt(ix + centerFromX, iy, iz + centerFromZ).getBlockData());
                }
            }
        }

        // 데이터 삭제
        deletePlot(fromX, fromZ);

        return 0;
    }

    public int deletePlot(int x, int z)
    {
        PlotDataEx plotData = database.getPlotDataEx(x, z);

        if (plotData == null) return 1; // 주인이 없는 플롯

        detachPlot(x, z); // 플롯 병합 해제

        // 데이터 삭제
        database.deletePlot(x, z);

        int centerX = x * 44, centerZ = z * 44;
        for (int ix = centerX - 12; ix <= centerX + 12; ix++)
        {
            for (int iz = centerZ - 12; iz <= centerZ + 12; iz++)
            {
                for (int iy = 0; iy < 256; iy++)
                {
                    if (world.getWorld().getBlockAt(ix, iy, iz).getBlockData() != world.getDefaultWorldBlock(ix, iy, iz))
                        world.getWorld().getBlockAt(ix, iy, iz).setBlockData(world.getDefaultWorldBlock(ix, iy, iz));
                }
            }
        }

        return 0;
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

    public int mergePlot4(int x1, int z1, int x2, int z2) // 네 플롯 병합
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

    public int detachPlot(int x, int z)
    {
        PlotDataEx plotData = database.getPlotDataEx(x, z);

        if (plotData == null) return 1; // 주인이 없는 플롯
        if (!database.getIsExtended(x, z, x + 1, z) && !database.getIsExtended(x, z, x - 1, z) && !database.getIsExtended(x, z, x, z + 1) && !database.getIsExtended(x, z, x, z - 1)) return 2; // 확장이 안된 플롯

        deleteSkin(x, z); // 스킨 삭제
        database.detachExtend2(x, z);  // 두 플롯의 병합을 해제
        PlotDataEx preserveData = database.getPlotDataEx(x, z); // 데이터 보존
        database.deletePlotData(preserveData.extend); // 데이터 삭제

        // 집합에 각 플롯들을 추가
        ArrayList<String> plots = database.getPlotByExtendPlot(preserveData.extend);
        ArrayList<HashSet<String>> plotSet = new ArrayList<>();
        for (String plot : plots)
        {
            HashSet<String> set = new HashSet<String>();
            set.add(plot);
            plotSet.add(set);
        }

        // 인접한 플롯의 집합끼리 합침
        for (String plot : plots)
        {
            int iter = 0;
            for (int i = 0; i < plotSet.size(); i++)
                if (plotSet.get(i).contains(plot))
                    iter = i; // iter -> plot을 가지고 있는 plotSet의 인덱스

            for (String plot2 : plots)
            {
                if (plot.compareTo(plot2) != 0 && database.getIsExtended(plot, plot2)) // plot과 plot2가 서로 연결된 관계
                {
                    int iter2 = 0;
                    for (int i = 0; i < plotSet.size(); i++)
                        if (plotSet.get(i).contains(plot2))
                            iter2 = i; // iter2 -> plot2를 가지고 있는 plotSet의 인덱스

                    if (iter != iter2) // 서로 다른 집합이 존재할 경우
                    {
                        plotSet.get(iter).addAll(plotSet.get(iter2)); // 두 집합을 합침
                        plotSet.remove(iter2); // 필요가 없어진 집합 삭제
                    }
                }
            }
        }

        // 확장 기준 플롯 재설정
        String[] extendPlots = new String[plotSet.size()];
        for (int i = 0; i < plotSet.size(); i++)
        {
            int j = 0;
            for (String p : plotSet.get(i))
            {
                String[] ps = p.split(":");
                int psX = Integer.parseInt(ps[0]);
                int psZ = Integer.parseInt(ps[1]);

                if (j == 0)
                {
                    extendPlots[i] = p;
                }
                else
                {
                    String[] eps = extendPlots[i].split(":");
                    int epsX = Integer.parseInt(eps[0]);
                    int epsZ = Integer.parseInt(eps[0]);

                    if (epsX < psX) extendPlots[i] = p;
                    else if (epsX == psX && epsZ < psZ) extendPlots[i] = p;
                }

                j++;
            }
            database.insertPlotData(extendPlots[i], preserveData);

            // 네 플롯의 병합 해제
            database.detachExtend4(x, z);

            for (String p : plotSet.get(i))
            {
                database.changeExtend(p, extendPlots[i]);
            }

            BlockData west = Bukkit.createBlockData(Material.BIRCH_STAIRS);
            ((Directional)west).setFacing(BlockFace.WEST);
            BlockData east = Bukkit.createBlockData(Material.BIRCH_STAIRS);
            ((Directional)east).setFacing(BlockFace.EAST);
            BlockData north = Bukkit.createBlockData(Material.BIRCH_STAIRS);
            ((Directional)north).setFacing(BlockFace.NORTH);
            BlockData south = Bukkit.createBlockData(Material.BIRCH_STAIRS);
            ((Directional)south).setFacing(BlockFace.SOUTH);

            // 블럭 변경
            int centerX = x * 44, centerZ = z * 44;
            for (int ix = centerX - 31; ix <= centerX + 31; ix++)
            {
                for (int iz = centerZ - 31; iz <= centerZ + 31; iz++)
                {
                    if (ix <= centerX - 12 || ix >= centerX + 12 || iz <= centerZ - 12 || iz >= centerZ + 12)
                    {
                        for (int iy = 0; iy < 256; iy++)
                        {
                            if (world.getWorld().getBlockAt(ix, iy, iz).getBlockData() != world.getDefaultWorldBlock(ix, iy, iz))
                                world.getWorld().getBlockAt(ix, iy, iz).setBlockData(world.getDefaultWorldBlock(ix, iy, iz));
                        }
                    }
                }
            }

            // 블럭 추가
            if (database.getIsExtended(x + 1, z + 1, x + 1, z)) // 왼쪽 위 - 왼쪽
            {
                // 반블럭
                for (int iz = centerZ + 14; iz <= centerZ + 25; iz++)
                {
                    world.getWorld().getBlockAt(centerX + 25, plugin.getConfig().getInt("height"), iz).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(centerX + 25, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int ix = centerX + 26; ix <= centerX + 30; ix++)
                {
                    for (int iz = centerZ + 14; iz <= centerZ + 30; iz++)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int iz = centerZ + 14; iz <= centerZ + 30; iz++)
                {
                    world.getWorld().getBlockAt(centerX + 31, plugin.getConfig().getInt("height"), iz).setBlockData(east);
                    world.getWorld().getBlockAt(centerX + 31, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }
            }
            if (database.getIsExtended(x + 1, z - 1, x + 1, z)) // 왼쪽 아래 - 왼쪽
            {
                // 반블럭
                for (int iz = centerZ - 14; iz >= centerZ - 25; iz--)
                {
                    world.getWorld().getBlockAt(centerX + 25, plugin.getConfig().getInt("height"), iz).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(centerX + 25, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int ix = centerX + 26; ix <= centerX + 30; ix++)
                {
                    for (int iz = centerZ - 14; iz >= centerZ - 30; iz--)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int iz = centerZ - 14; iz >= centerZ - 30; iz--)
                {
                    world.getWorld().getBlockAt(centerX + 31, plugin.getConfig().getInt("height"), iz).setBlockData(east);
                    world.getWorld().getBlockAt(centerX + 31, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }
            }
            if (database.getIsExtended(x - 1, z + 1, x - 1, z)) // 오른쪽 위 - 오른쪽
            {
                // 반블럭
                for (int iz = centerZ + 14; iz <= centerZ + 25; iz++)
                {
                    world.getWorld().getBlockAt(centerX - 25, plugin.getConfig().getInt("height"), iz).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(centerX - 25, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int ix = centerX - 26; ix >= centerX - 30; ix--)
                {
                    for (int iz = centerZ + 14; iz <= centerZ + 30; iz++)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int iz = centerZ + 14; iz <= centerZ + 30; iz++)
                {
                    world.getWorld().getBlockAt(centerX - 31, plugin.getConfig().getInt("height"), iz).setBlockData(west);
                    world.getWorld().getBlockAt(centerX - 31, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }
            }
            if (database.getIsExtended(x - 1, z - 1, x - 1, z)) // 오른쪽 아래 - 오른쪽
            {
                // 반블럭
                for (int iz = centerZ - 14; iz >= centerZ - 25; iz--)
                {
                    world.getWorld().getBlockAt(centerX - 25, plugin.getConfig().getInt("height"), iz).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(centerX - 25, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int ix = centerX - 26; ix >= centerX - 30; ix--)
                {
                    for (int iz = centerZ - 14; iz >= centerZ - 30; iz--)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int iz = centerZ - 14; iz >= centerZ - 30; iz--)
                {
                    world.getWorld().getBlockAt(centerX - 31, plugin.getConfig().getInt("height"), iz).setBlockData(west);
                    world.getWorld().getBlockAt(centerX - 31, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                }
            }
            if (database.getIsExtended(x + 1, z + 1, x, z + 1)) // 왼쪽 위 - 위
            {
                // 반블럭
                for (int ix = centerX + 14; ix <= centerX + 25; ix++)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ + 25).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ + 25).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int iz = centerZ + 26; iz <= centerZ + 30; iz++)
                {
                    for (int ix = centerX + 14; ix <= centerX + 30; ix++)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int ix = centerX + 14; ix <= centerX + 30; ix++)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ + 31).setBlockData(south);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ + 31).setType(Material.COARSE_DIRT);
                }
            }
            if (database.getIsExtended(x - 1, z + 1, x, z + 1)) // 오른쪽 위 - 위
            {
                // 반블럭
                for (int ix = centerX - 14; ix >= centerX - 25; ix--)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ + 25).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ + 25).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int iz = centerZ + 26; iz <= centerZ + 30; iz++)
                {
                    for (int ix = centerX - 14; ix >= centerX - 30; ix--)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int ix = centerX - 14; ix >= centerX - 30; ix--)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ + 31).setBlockData(south);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ + 31).setType(Material.COARSE_DIRT);
                }
            }
            if (database.getIsExtended(x + 1, z - 1, x, z - 1)) // 왼쪽 아래 - 아래
            {
                // 반블럭
                for (int ix = centerX + 14; ix <= centerX + 25; ix++)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ - 25).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ - 25).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int iz = centerZ - 26; iz >= centerZ - 30; iz--)
                {
                    for (int ix = centerX + 14; ix <= centerX + 30; ix++)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int ix = centerX + 14; ix <= centerX + 30; ix++)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ - 31).setBlockData(north);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ - 31).setType(Material.COARSE_DIRT);
                }
            }
            if (database.getIsExtended(x - 1, z + 1, x, z + 1)) // 오른쪽 아래 - 아래
            {
                // 반블럭
                for (int ix = centerX - 14; ix >= centerX - 25; ix--)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ - 25).setType(Material.BIRCH_SLAB);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ - 25).setType(Material.COARSE_DIRT);
                }

                // 공기
                for (int iz = centerZ - 26; iz >= centerZ - 30; iz--)
                {
                    for (int ix = centerX - 14; ix >= centerX - 30; ix--)
                    {
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), iz).setType(Material.AIR);
                        world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, iz).setType(Material.COARSE_DIRT);
                    }
                }

                // 계단
                for (int ix = centerX - 14; ix >= centerX - 30; ix--)
                {
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height"), centerZ - 31).setBlockData(north);
                    world.getWorld().getBlockAt(ix, plugin.getConfig().getInt("height") - 1, centerZ - 31).setType(Material.COARSE_DIRT);
                }
            }
        }

        return 0;
    }

    public void setSkinPlot(int x, int z, int slot, int skin)
    {
        // 스킨 설정
    }

    public void setSkinPlots()
    {
        // 스킨 한번에 설정
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