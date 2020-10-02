package woolyung.main.plot;

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

    public boolean buyPlot(Player player, int x, int z) // 플롯 구매
    {
        PlotDataEx plotDataEx = database.getPlotDataEx(x, z);

        if (plotDataEx != null) return false; // 주인이 있는 플롯

        MineplanetPlot.instance.getPlotDatabase().insertPlotData(x, z);
        MineplanetPlot.instance.getPlotDatabase().insertPlayerPlotData(x, z, player.getUniqueId().toString(), "owner");
        return true;
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

    public boolean mergePlot2(int x1, int z1, int x2, int z2) // 두 플롯 병합
    {
        PlotDataEx plotData1 = database.getPlotDataEx(x1, z1);
        PlotDataEx plotData2 = database.getPlotDataEx(x2, z2);

        if (plotData1 == null || plotData2 == null) return false; // 주인이 없는 플롯
        if (plotData1.owner.compareTo(plotData2.owner) != 0) return false; // 주인이 다른 플롯

        int distance = Math.abs(x1 - x2) + Math.abs(z1 - z2);
        if (distance != 1) return false; // 인접한 플롯이 아님

        return true;
    }

    public void mergePlot4(int x1, int z1, int x2, int z2)
    {
        // 네 플롯 병합
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