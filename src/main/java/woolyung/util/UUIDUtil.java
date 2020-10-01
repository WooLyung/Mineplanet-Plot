package woolyung.util;

import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlayerData;

public class UUIDUtil
{
    public static String getName(String uuid)
    {
        PlayerData playerData = MineplanetPlot.instance.getPlotDatabase().getPlayerData(uuid);
        if (playerData == null) return "null";
        else return playerData.name;
    }
}