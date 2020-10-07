package woolyung.main.plot;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.PlotDatabase;
import woolyung.main.plot.Data.PlayerData;
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

    public int buyPlot(String uuid, int x, int z) // 플롯 구매
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);

        if (plotDataEx != null) return 1; // 주인이 있는 플롯

        MineplanetPlot.instance.getPlotDatabase().insertPlotData(x, z);
        MineplanetPlot.instance.getPlotDatabase().insertPlayerPlotData(x, z, uuid, "owner");
        return 0;
    }

    public String getBiomeName(String biome)
    {
        if (biome.compareTo("plains") == 0) return "평야";
        if (biome.compareTo("badlands") == 0) return "악지";
        if (biome.compareTo("beach") == 0) return "해변";
        if (biome.compareTo("birch_forest") == 0) return "자작나무 숲";
        if (biome.compareTo("cold_ocean") == 0) return "추운 바다";
        if (biome.compareTo("dark_forest") == 0) return "어두운 숲";
        if (biome.compareTo("desert") == 0) return "사막";
        if (biome.compareTo("forest") == 0) return "숲";
        if (biome.compareTo("flower_forest") == 0) return "꽃 숲";
        if (biome.compareTo("frozen_ocean") == 0) return "얼어붙은 바다";
        if (biome.compareTo("frozen_river") == 0) return "얼어붙은 강";
        if (biome.compareTo("giant_spruce_taiga") == 0) return "거대한 가문비나무 타이가";
        if (biome.compareTo("giant_tree_taiga") == 0) return "거대한 나무 타이가";
        if (biome.compareTo("gravelly_mountains") == 0) return "자갈 산";
        if (biome.compareTo("ice_spikes") == 0) return "역고드름";
        if (biome.compareTo("jungle") == 0) return "정글";
        if (biome.compareTo("lukewarm_ocean") == 0) return "미지근한 바다";
        if (biome.compareTo("mountains") == 0) return "산";
        if (biome.compareTo("mushroom_fields") == 0) return "버섯 대지";
        if (biome.compareTo("bamboo_jungle") == 0) return "대나무 정글";
        if (biome.compareTo("ocean") == 0) return "바다";
        if (biome.compareTo("river") == 0) return "강";
        if (biome.compareTo("savanna") == 0) return "사바나";
        if (biome.compareTo("snowy_beach") == 0) return "눈덮인 해변";
        if (biome.compareTo("snowy_mountains") == 0) return "눈덮인 산";
        if (biome.compareTo("snowy_taiga") == 0) return "눈덮인 타이가";
        if (biome.compareTo("snowy_tundra") == 0) return "눈덮인 툰드라";
        if (biome.compareTo("stone_shore") == 0) return "바위 기슭";
        if (biome.compareTo("sunflower_plains") == 0) return "해바라기 평야";
        if (biome.compareTo("swamp") == 0) return "늪";
        if (biome.compareTo("taiga") == 0) return "타이가";
        if (biome.compareTo("warm_ocean") == 0) return "따뜻한 바다";
        return null;
    }

    public Biome getBiome(String biome)
    {
        if (biome.compareTo("plains") == 0) return Biome.PLAINS;
        if (biome.compareTo("badlands") == 0) return Biome.BADLANDS;
        if (biome.compareTo("beach") == 0) return Biome.BEACH;
        if (biome.compareTo("birch_forest") == 0) return Biome.BIRCH_FOREST;
        if (biome.compareTo("cold_ocean") == 0) return Biome.COLD_OCEAN;
        if (biome.compareTo("dark_forest") == 0) return Biome.DARK_FOREST;
        if (biome.compareTo("desert") == 0) return Biome.DESERT;
        if (biome.compareTo("forest") == 0) return Biome.FOREST;
        if (biome.compareTo("flower_forest") == 0) return Biome.FLOWER_FOREST;
        if (biome.compareTo("frozen_ocean") == 0) return Biome.FROZEN_OCEAN;
        if (biome.compareTo("frozen_river") == 0) return Biome.FROZEN_RIVER;
        if (biome.compareTo("giant_spruce_taiga") == 0) return Biome.GIANT_SPRUCE_TAIGA;
        if (biome.compareTo("giant_tree_taiga") == 0) return Biome.GIANT_TREE_TAIGA;
        if (biome.compareTo("gravelly_mountains") == 0) return Biome.GRAVELLY_MOUNTAINS;
        if (biome.compareTo("ice_spikes") == 0) return Biome.ICE_SPIKES;
        if (biome.compareTo("jungle") == 0) return Biome.JUNGLE;
        if (biome.compareTo("lukewarm_ocean") == 0) return Biome.LUKEWARM_OCEAN;
        if (biome.compareTo("mountains") == 0) return Biome.MOUNTAINS;
        if (biome.compareTo("mushroom_fields") == 0) return Biome.MUSHROOM_FIELDS;
        if (biome.compareTo("bamboo_jungle") == 0) return Biome.BAMBOO_JUNGLE;
        if (biome.compareTo("ocean") == 0) return Biome.OCEAN;
        if (biome.compareTo("river") == 0) return Biome.RIVER;
        if (biome.compareTo("savanna") == 0) return Biome.SAVANNA;
        if (biome.compareTo("snowy_beach") == 0) return Biome.SNOWY_BEACH;
        if (biome.compareTo("snowy_mountains") == 0) return Biome.SNOWY_MOUNTAINS;
        if (biome.compareTo("snowy_taiga") == 0) return Biome.SNOWY_TAIGA;
        if (biome.compareTo("snowy_tundra") == 0) return Biome.SNOWY_TUNDRA;
        if (biome.compareTo("stone_shore") == 0) return Biome.STONE_SHORE;
        if (biome.compareTo("sunflower_plains") == 0) return Biome.SUNFLOWER_PLAINS;
        if (biome.compareTo("swamp") == 0) return Biome.SWAMP;
        if (biome.compareTo("taiga") == 0) return Biome.TAIGA;
        if (biome.compareTo("warm_ocean") == 0) return Biome.WARM_OCEAN;
        return null;
    }

    public int setBiomes(int x, int z, String biomeS)
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 1;

        Biome biome = getBiome(biomeS);
        if (biome == null) return 2;

        database.setBiomeData(x, z, biomeS);

        for (String plot : database.getPlotByExtendPlot(plotDataEx.extend))
        {
            String[] coord = plot.split(":");
            setBiome(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), biome);
        }

        return 0;
    }

    public int setBiomes(int x, int z)
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 1;

        Biome biome = getBiome(plotDataEx.biome);

        for (String plot : database.getPlotByExtendPlot(plotDataEx.extend))
        {
            String[] coord = plot.split(":");
            setBiome(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), biome);
        }

        return 0;
    }

    public void setBiome(int x, int z, Biome biome)
    {
        int centerX = x * 44, centerZ = z * 44;
        for (int ix = -12; ix <= 12; ix++)
        {
            for (int iz = -12; iz <= 12; iz++)
            {
                world.getWorld().setBiome(ix + centerX, 0, iz + centerZ, biome);
            }
        }

        if (database.getIsExtended(x, z, x + 1, z))
        {
            for (int ix = 13; ix <= 31; ix++)
            {
                for (int iz = -12; iz <= 12; iz++)
                {
                    world.getWorld().setBiome(ix + centerX, 0, iz + centerZ, biome);
                }
            }
        }

        if (database.getIsExtended(x, z, x, z + 1))
        {
            for (int ix = -12; ix <= 12; ix++)
            {
                for (int iz = 13; iz <= 31; iz++)
                {
                    world.getWorld().setBiome(ix + centerX, 0, iz + centerZ, biome);
                }
            }
        }

        if (database.getIsExtended4(x, z, x + 1, z + 1))
        {
            for (int ix = 13; ix <= 31; ix++)
            {
                for (int iz = 13; iz <= 31; iz++)
                {
                    world.getWorld().setBiome(ix + centerX, 0, iz + centerZ, biome);
                }
            }
        }
    }

    public int clearPlots(int x, int z)
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 1;

        for (String plot : database.getPlotByExtendPlot(plotDataEx.extend))
        {
            String[] coord = plot.split(":");
            clearPlot(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
        }

        return 0;
    }

    public void clearPlot(int x, int z)
    {
        int centerX = x * 44, centerZ = z * 44;
        for (int ix = -12; ix <= 12; ix++)
        {
            for (int iz = -12; iz <= 12; iz++)
            {
                for (int iy = 0; iy < 256; iy++)
                {
                    Material mat;
                    if (iy == 0) mat = Material.BEDROCK;
                    else if (iy < world.getHeight()) mat = Material.DIRT;
                    else if (iy == world.getHeight()) mat = Material.GRASS_BLOCK;
                    else mat = Material.AIR;

                    world.getWorld().getBlockAt(ix + centerX, iy, iz + centerZ).setType(mat);
                }
            }
        }

        if (database.getIsExtended(x, z, x + 1, z))
        {
            for (int ix = 13; ix <= 31; ix++)
            {
                for (int iz = -12; iz <= 12; iz++)
                {
                    for (int iy = 0; iy < 256; iy++)
                    {
                        Material mat;
                        if (iy == 0) mat = Material.BEDROCK;
                        else if (iy < world.getHeight()) mat = Material.DIRT;
                        else if (iy == world.getHeight()) mat = Material.GRASS_BLOCK;
                        else mat = Material.AIR;

                        world.getWorld().getBlockAt(ix + centerX, iy, iz + centerZ).setType(mat);
                    }
                }
            }
        }

        if (database.getIsExtended(x, z, x, z + 1))
        {
            for (int ix = -12; ix <= 12; ix++)
            {
                for (int iz = 13; iz <= 31; iz++)
                {
                    for (int iy = 0; iy < 256; iy++)
                    {
                        Material mat;
                        if (iy == 0) mat = Material.BEDROCK;
                        else if (iy < world.getHeight()) mat = Material.DIRT;
                        else if (iy == world.getHeight()) mat = Material.GRASS_BLOCK;
                        else mat = Material.AIR;

                        world.getWorld().getBlockAt(ix + centerX, iy, iz + centerZ).setType(mat);
                    }
                }
            }
        }

        if (database.getIsExtended4(x, z, x + 1, z + 1))
        {
            for (int ix = 13; ix <= 31; ix++)
            {
                for (int iz = 13; iz <= 31; iz++)
                {
                    for (int iy = 0; iy < 256; iy++)
                    {
                        Material mat;
                        if (iy == 0) mat = Material.BEDROCK;
                        else if (iy < world.getHeight()) mat = Material.DIRT;
                        else if (iy == world.getHeight()) mat = Material.GRASS_BLOCK;
                        else mat = Material.AIR;

                        world.getWorld().getBlockAt(ix + centerX, iy, iz + centerZ).setType(mat);
                    }
                }
            }
        }
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

        CuboidRegion region = new CuboidRegion(new BukkitWorld(plugin.getPlotWorld().getWorld()), BlockVector3.at(centerFromX - 12, 0, centerFromZ - 12), BlockVector3.at(centerFromX + 12, 255, centerFromZ + 12));
        BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

        // 복사
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            ForwardExtentCopy forwardExtentCopy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );
            forwardExtentCopy.setCopyingEntities(true);
            Operations.complete(forwardExtentCopy);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // 붙여넣기
        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(centerToX - 12, 0, centerToZ - 12))
                    .copyEntities(true)
                    .build();
            Operations.complete(operation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // 데이터 삭제
        deletePlot(fromX, fromZ);

        return 0;
    }

    public int givePlot(String name, int x, int z)
    {
        PlayerData data = database.getPlayerDataByName(name);
        if (data == null) return 1;

        PlotDataEx plotData = database.getPlotDataEx(x, z);

        if (plotData != null) return 2; // 주인이 있는 플롯
        buyPlot(data.uuid, x, z);

        return 0;
    }

    public int deletePlot(int x, int z)
    {
        PlotDataEx plotData = database.getPlotDataEx(x, z);

        if (plotData == null) return 1; // 주인이 없는 플롯

        detachPlot(x, z); // 플롯 병합 해제

        // 데이터 삭제
        setSkinPlots(x, z, "default");
        setBiomes(x, z, "plains");
        database.deletePlot(x, z);
        setSkinPlot(x, z);

        int centerX = x * 44, centerZ = z * 44;
        for (int ix = centerX - 12; ix <= centerX + 12; ix++)
        {
            for (int iz = centerZ - 12; iz <= centerZ + 12; iz++)
            {
                for (int iy = 0; iy < 256; iy++)
                {
                    world.getWorld().getBlockAt(ix, iy, iz).setBlockData(world.getDefaultWorldBlock(ix, iy, iz));
                }
            }
        }

        // 엔티티 삭제
        for (Entity entity : world.getWorld().getEntities())
        {
            if (entity.getType() != EntityType.PLAYER)
            {
                if (entity.getLocation().getBlockX() >= centerX - 12 && entity.getLocation().getBlockX() <= centerX + 12)
                {
                    if (entity.getLocation().getBlockZ() >= centerZ - 12 && entity.getLocation().getBlockZ() <= centerZ + 12)
                    {
                        entity.remove();
                    }
                }
            }
        }

        return 0;
    }

    public void deleteSkin(int x, int z)
    {
        MineplanetPlot.instance.getSkinManager().setSkinData(x, z, "default");
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

        deleteSkin(x1, z1);
        setBiomes(x1, z1, "plains");

        // 블럭 채우기
        int centerX = x1 * 44;
        int centerZ = z1 * 44;

        if (z1 > z2) // 아래
        {
            for (int z = centerZ - 13; z >= centerZ - 31; z--)
            {
                for (int x = centerX - 12; x <= centerX + 12; x++)
                {
                    for (int y = 0; y <= plugin.getConfig().getInt("height") - 1; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    for (int y = plugin.getConfig().getInt("height") + 1; y <= plugin.getConfig().getInt("height") + 30; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }
            }
        }
        else if (z1 < z2) // 위
        {
            for (int z = centerZ + 13; z <= centerZ + 31; z++)
            {
                for (int x = centerX - 12; x <= centerX + 12; x++)
                {
                    for (int y = 0; y <= plugin.getConfig().getInt("height") - 1; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    for (int y = plugin.getConfig().getInt("height") + 1; y <= plugin.getConfig().getInt("height") + 30; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }
            }
        }
        else if (x1 > x2) // 오른쪽
        {
            for (int x = centerX - 13; x >= centerX - 31; x--)
            {
                for (int z = centerZ - 12; z <= centerZ + 12; z++)
                {
                    for (int y = 0; y <= plugin.getConfig().getInt("height") - 1; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    for (int y = plugin.getConfig().getInt("height") + 1; y <= plugin.getConfig().getInt("height") + 30; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }
            }
        }
        else if (x1 < x2) // 왼쪽
        {
            for (int x = centerX + 13; x <= centerX + 31; x++)
            {
                for (int z = centerZ - 12; z <= centerZ + 12; z++)
                {
                    for (int y = 0; y <= plugin.getConfig().getInt("height") - 1; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                    }
                    for (int y = plugin.getConfig().getInt("height") + 1; y <= plugin.getConfig().getInt("height") + 30; y++) {
                        world.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }
                    world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
                }
            }
        }

        setSkinPlots(x1, z1);

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
        setBiomes(x1, z1, "plains");

        // 데이터 입력
        database.insertExtend4(x1, z1, x2, z2);

        int dirX = x2 - x1;
        int dirZ = z2 - z1;
        int centerX = x1 * 44 + dirX * 22, centerZ = z1 * 44 + dirZ * 22;

        for (int x = centerX - 9; x <= centerX + 9; x++)
        {
            for (int z = centerZ - 9; z <= centerZ + 9; z++)
            {
                for (int y = 0; y < plugin.getConfig().getInt("height"); y++)
                {
                    world.getWorld().getBlockAt(x, y, z).setType(Material.DIRT);
                }
                for (int y = plugin.getConfig().getInt("height") + 1; y <= plugin.getConfig().getInt("height") + 20; y++)
                {
                    world.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                }
                world.getWorld().getBlockAt(x, plugin.getConfig().getInt("height"), z).setType(Material.GRASS_BLOCK);
            }
        }

        setSkinPlots(x1, z1);

        return 0;
    }

    public int setHelper(int x, int z, String name, Player user)
    {
        PlayerData playerData = database.getPlayerDataByName(name);
        if (playerData == null) return 2; // 데이터가 없는 플레이어

        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 3; // 주인이 없는 플롯
        if (plotDataEx.owner.compareTo(user.getUniqueId().toString()) != 0) return 4; // 그 플롯의 주인이 아님
        if (plotDataEx.denies.contains(playerData.uuid)) return 5; // 차단 플레이어

        if (plotDataEx.helpers.contains(playerData.uuid)) // 이미 존재할 경우 삭제
        {
            String[] st = plotDataEx.extend.split(":");
            database.deletePlayerPlotData(Integer.parseInt(st[0]), Integer.parseInt(st[1]), playerData.uuid, "helper");
            return 1;
        }
        else // 존재하지 않을 경우 추가
        {
            database.insertPlayerPlotData(plotDataEx.extend, playerData.uuid, "helper");
            return 0;
        }
    }

    public int setDeny(int x, int z, String name, Player user)
    {
        PlayerData playerData = database.getPlayerDataByName(name);
        if (playerData == null) return 2; // 데이터가 없는 플레이어

        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 3; // 주인이 없는 플롯
        if (plotDataEx.owner.compareTo(user.getUniqueId().toString()) != 0) return 4; // 그 플롯의 주인이 아님
        if (plotDataEx.helpers.contains(playerData.uuid)) return 5; // 헬퍼 플레이어

        if (plotDataEx.denies.contains(playerData.uuid)) // 이미 존재할 경우 삭제
        {
            String[] st = plotDataEx.extend.split(":");
            database.deletePlayerPlotData(Integer.parseInt(st[0]), Integer.parseInt(st[1]), playerData.uuid, "deny");
            return 1;
        }
        else // 존재하지 않을 경우 추가
        {
            database.insertPlayerPlotData(plotDataEx.extend, playerData.uuid, "deny");
            return 0;
        }
    }

    public int detachPlot(int x, int z)
    {
        PlotDataEx plotData = database.getPlotDataEx(x, z);

        if (plotData == null) return 1; // 주인이 없는 플롯
        if (!database.getIsExtended(x, z, x + 1, z) && !database.getIsExtended(x, z, x - 1, z) && !database.getIsExtended(x, z, x, z + 1) && !database.getIsExtended(x, z, x, z - 1)) return 2; // 확장이 안된 플롯

        deleteSkin(x, z); // 스킨 삭제
        setBiomes(x, z, "plains"); // 바이옴 초기화
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

                        world.getWorld().setBiome(ix, 0, iz, Biome.PLAINS);
                    }
                }
            }

            // 엔티티 삭제
            for (Entity entity : world.getWorld().getEntities())
            {
                if (entity.getType() != EntityType.PLAYER)
                {
                    if (entity.getLocation().getBlockX() >= centerX - 31 && entity.getLocation().getBlockX() <= centerX + 31
                            && !(entity.getLocation().getBlockX() >= centerX - 12 && entity.getLocation().getBlockX() <= centerX + 12))
                    {
                        if (entity.getLocation().getBlockZ() >= centerZ - 31 && entity.getLocation().getBlockZ() <= centerZ + 31
                                && !(entity.getLocation().getBlockZ() >= centerZ - 12 && entity.getLocation().getBlockZ() <= centerZ + 12))
                        {
                            entity.remove();
                        }
                    }
                }
            }
        }

        setSkinPlots(x, z);
        setSkinPlots(x + 1, z);
        setSkinPlots(x, z + 1);
        setSkinPlots(x, z - 1);
        setSkinPlots(x - 1, z);
        setSkinPlots(x + 1, z + 1);
        setSkinPlots(x + 1, z - 1);
        setSkinPlots(x - 1, z - 1);
        setSkinPlots(x - 1, z + 1);

        setBiomes(x, z);
        setBiomes(x + 1, z);
        setBiomes(x, z + 1);
        setBiomes(x, z - 1);
        setBiomes(x - 1, z);
        setBiomes(x + 1, z + 1);
        setBiomes(x + 1, z - 1);
        setBiomes(x - 1, z - 1);
        setBiomes(x - 1, z + 1);

        return 0;
    }

    public int setSkinPlots(int x, int z, String skin)
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 1;
        int result = MineplanetPlot.instance.getSkinManager().setSkinData(x, z, skin);

        if (result == 1) return 2;

        for (String plot : database.getPlotByExtendPlot(plotDataEx.extend))
        {
            String[] coord = plot.split(":");
            MineplanetPlot.instance.getSkinManager().updateSkin(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
        }

        return 0;
    }

    public int setSkinPlots(int x, int z)
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 1;

        for (String plot : database.getPlotByExtendPlot(plotDataEx.extend))
        {
            String[] coord = plot.split(":");
            MineplanetPlot.instance.getSkinManager().updateSkin(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]));
        }

        return 0;
    }

    public int setSkinPlot(int x, int z)
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 1;

        MineplanetPlot.instance.getSkinManager().updateSkin(x, z);

        return 0;
    }

    public int settingPlotInt(int x, int z, String setting, int value)
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);
        if (plotDataEx == null) return 1;

        database.updatePlotSettingInt(plotDataEx.extend, setting, value);

        return 0;
    }

    public void settingPlotString(int x, int z, String setting, String value)
    {
        // 문자열 설정
    }
}