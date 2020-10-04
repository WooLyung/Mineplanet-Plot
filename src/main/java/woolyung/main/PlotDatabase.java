package woolyung.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import woolyung.main.plot.Data.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PlotDatabase
{
    private Connection connection;
    private Statement statement;
    private String path = "";
    private String file = "";

    public PlotDatabase()
    {
        sqliteSetup();
    }

    private void sqliteSetup()
    {
        path = MineplanetPlot.instance.getDataFolder() + "/datas/";
        file = "plot.db";

        try
        {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path + file);
            statement = connection.createStatement();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            Bukkit.getLogger().info("jdbc 클래스를 찾을 수 없습니다. 플러그인을 비활성화합니다.");
            MineplanetPlot.instance.getPluginLoader().disablePlugin(MineplanetPlot.instance);
            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Bukkit.getLogger().info("데이터베이스 예외가 발생했습니다. 플러그인을 비활성화합니다.");
            MineplanetPlot.instance.getPluginLoader().disablePlugin(MineplanetPlot.instance);
            return;
        }

        createTable();
    }

    private void createTable()
    {
        try
        {
            statement.execute("PRAGMA foreign_keys = ON");

            // 플롯 테이블
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'plot'").getInt(1) == 0)
                statement.execute("CREATE TABLE plot (pos TEXT PRIMARY KEY, extend TEXT)");

            // 플레이어 테이블
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'player'").getInt(1) == 0)
                statement.execute("CREATE TABLE player (uuid TEXT PRIMARY KEY, name TEXT, max_plot INTEGER)");

            // 플롯-플레이어 테이블
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'player_plot'").getInt(1) == 0)
                statement.execute("CREATE TABLE player_plot (uuid TEXT, authority TEXT, pos TEXT, FOREIGN KEY(pos) REFERENCES plot_data(pos) ON DELETE CASCADE)");

            // 플롯 데이터 테이블
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'plot_data'").getInt(1) == 0)
                statement.execute("CREATE TABLE plot_data (pos TEXT PRIMARY KEY, skin1 INTEGER, skin2 INTEGER, skin3 INTEGER, biome TEXT, pvp INTEGER, click INTEGER, block_click INTEGER, item_clear INTEGER)");

            // 플롯 병합2 테이블
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'plot_extend2'").getInt(1) == 0)
                statement.execute("CREATE TABLE plot_extend2 (plot1 TEXT, plot2 TEXT, FOREIGN KEY(plot1) REFERENCES plot(pos) ON DELETE CASCADE, FOREIGN KEY(plot2) REFERENCES plot(pos) ON DELETE CASCADE)");

            // 플롯 병합4 테이블
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'plot_extend4'").getInt(1) == 0)
                statement.execute("CREATE TABLE plot_extend4 (plot1 TEXT, plot2 TEXT, FOREIGN KEY(plot1) REFERENCES plot(pos) ON DELETE CASCADE, FOREIGN KEY(plot2) REFERENCES plot(pos) ON DELETE CASCADE)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Bukkit.getLogger().info("데이터베이스 예외가 발생했습니다. 플러그인을 비활성화합니다.");
            MineplanetPlot.instance.getPluginLoader().disablePlugin(MineplanetPlot.instance);
            return;
        }
    }

    public PlotData getPlotData(int posX, int posZ)
    {
        try
        {
            if (statement.executeQuery("SELECT count(*) FROM plot WHERE pos = '" + posX + ":" + posZ + "'").getInt(1) == 0)
                return null;
            ResultSet result = statement.executeQuery("SELECT * FROM plot WHERE pos = '" + posX + ":" + posZ + "'");
            String extend = result.getString("extend");

            PlotData data = new PlotData();
            data.posX = posX;
            data.posZ = posZ;
            data.extend = extend;

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public PlotDataEx getPlotDataEx(int posX, int posZ)
    {
        PlotData plotData = getPlotData(posX, posZ);
        if (plotData == null) return null;

        try
        {
            PlotDataEx plotDataEx = new PlotDataEx();
            plotDataEx.posX = plotData.posX;
            plotDataEx.posZ = plotData.posZ;
            plotDataEx.extend = plotData.extend;

            ResultSet result2 = statement.executeQuery("SELECT * FROM plot_data WHERE pos = '" + plotData.extend + "'");
            plotDataEx.skin1 = result2.getInt("skin1");
            plotDataEx.skin2 = result2.getInt("skin2");
            plotDataEx.skin3 = result2.getInt("skin3");
            plotDataEx.biome = result2.getString("biome");
            plotDataEx.pvp = result2.getInt("pvp") == 1;
            plotDataEx.click = result2.getInt("click") == 1;
            plotDataEx.blockClick = result2.getInt("block_click") == 1;
            plotDataEx.itemClear = result2.getInt("item_clear") == 1;

            ArrayList<String> extendPlots = getPlotByExtendPlot(posX, posZ);
            plotDataEx.plotSize = extendPlots.size();

            ResultSet result = statement.executeQuery("SELECT * FROM player_plot WHERE pos = '" + plotDataEx.extend + "'");
            while (result.next())
            {
                String authority = result.getString("authority");
                if (authority.compareTo("owner") == 0)
                    plotDataEx.owner = result.getString("uuid");
                else if (authority.compareTo("helper") == 0)
                    plotDataEx.helpers.add(result.getString("uuid"));
                else if (authority.compareTo("deny") == 0)
                    plotDataEx.denies.add(result.getString("uuid"));
            }

            return plotDataEx;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public PlayerData getPlayerData(Player player)
    {
        try
        {
            if (statement.executeQuery("SELECT count(*) FROM player WHERE uuid = '" + player.getUniqueId() +"'").getInt(1) == 0)
                return null;
            ResultSet result = statement.executeQuery("SELECT * FROM player WHERE uuid = '" + player.getUniqueId() + "'");
            String uuid = result.getString("uuid");
            String name = result.getString("name");
            int maxPlot = result.getInt("max_plot");

            PlayerData data = new PlayerData();
            data.uuid = uuid;
            data.maxPlot = maxPlot;
            data.name = name;

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public PlayerData getPlayerData(String uuid)
    {
        try
        {
            if (statement.executeQuery("SELECT count(*) FROM player WHERE uuid = '" + uuid +"'").getInt(1) == 0)
                return null;
            ResultSet result = statement.executeQuery("SELECT * FROM player WHERE uuid = '" + uuid + "'");
            String name = result.getString("name");
            int maxPlot = result.getInt("max_plot");

            PlayerData data = new PlayerData();
            data.uuid = uuid;
            data.maxPlot = maxPlot;
            data.name = name;

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public PlayerDataEx getPlayerDataEx(Player player)
    {
        PlayerData playerData = getPlayerData(player);
        if (playerData == null) return null;

        try
        {
            PlayerDataEx playerDataEx = new PlayerDataEx();
            playerDataEx.maxPlot = playerData.maxPlot;
            playerDataEx.uuid = playerData.uuid;
            playerDataEx.name = playerData.name;

            ArrayList<String> plots = new ArrayList<>();

            ResultSet result = statement.executeQuery("SELECT * FROM player_plot WHERE uuid = '" + player.getUniqueId() + "'");
            while (result.next())
            {
                String authority = result.getString("authority");
                String pos = result.getString("pos");

                if (authority.compareTo("owner") == 0)
                {
                    plots.add(pos);
                }
            }

            for (String pos : plots)
            {
                ResultSet result2 = statement.executeQuery("SELECT * FROM plot WHERE extend = '" + pos + "'");
                while (result2.next())
                {
                    playerDataEx.plots.add(result2.getString("pos"));
                }
            }

            playerDataEx.plotCount = playerDataEx.plots.size();

            return playerDataEx;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public boolean getIsExtended(int x1, int z1, int x2, int z2)
    {
        try {
            String pos1 = x1 + ":" + z1;
            String pos2 = x2 + ":" + z2;

            if (statement.executeQuery("SELECT count(*) FROM plot_extend2 WHERE (plot1 = '" + pos1 + "' AND plot2 = '" + pos2 + "') OR (plot1 = '" + pos2 + "' AND plot2 = '" + pos1 + "')").getInt(1) == 0)
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getIsExtended(String pos1, String pos2)
    {
        try {
            if (statement.executeQuery("SELECT count(*) FROM plot_extend2 WHERE (plot1 = '" + pos1 + "' AND plot2 = '" + pos2 + "') OR (plot1 = '" + pos2 + "' AND plot2 = '" + pos1 + "')").getInt(1) == 0)
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void detachExtend2(int x, int z)
    {
        try {
            String pos = x + ":" + z;

            statement.execute("DELETE FROM plot_extend2 WHERE (plot1 = '" + pos + "' OR plot2 = '" + pos + "')");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void detachExtend4(int x, int z)
    {
        try
        {
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + x + ":" + z + "' AND plot2 = '" + (x + 1) + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + x + ":" + z + "' AND plot2 = '" + (x - 1) + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + x + ":" + z + "' AND plot2 = '" + (x + 1) + ":" + (z - 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + x + ":" + z + "' AND plot2 = '" + (x - 1) + ":" + (z - 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + (x + 1) + ":" + z + "' AND plot2 = '" + x + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + (x + 1) + ":" + z + "' AND plot2 = '" + x + ":" + (z - 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + (x - 1) + ":" + z + "' AND plot2 = '" + x + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot1 = '" + (x - 1) + ":" + z + "' AND plot2 = '" + x + ":" + (z - 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + x + ":" + z + "' AND plot1 = '" + (x + 1) + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + x + ":" + z + "' AND plot1 = '" + (x - 1) + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + x + ":" + z + "' AND plot1 = '" + (x + 1) + ":" + (z - 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + x + ":" + z + "' AND plot1 = '" + (x - 1) + ":" + (z - 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + (x + 1) + ":" + z + "' AND plot1 = '" + x + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + (x + 1) + ":" + z + "' AND plot1 = '" + x + ":" + (z - 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + (x - 1) + ":" + z + "' AND plot1 = '" + x + ":" + (z + 1) + "')");
            statement.execute("DELETE FROM plot_extend4 WHERE (plot2 = '" + (x - 1) + ":" + z + "' AND plot1 = '" + x + ":" + (z - 1) + "')");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean getIsExtended4(int x1, int z1, int x2, int z2)
    {
        try {
            String pos1 = x1 + ":" + z1;
            String pos2 = x2 + ":" + z2;
            String pos3 = x2 + ":" + z1;
            String pos4 = x1 + ":" + z2;

            if (statement.executeQuery("SELECT count(*) FROM plot_extend4 WHERE (plot1 = '" + pos1 + "' AND plot2 = '" + pos2 + "') OR (plot1 = '" + pos2 + "' AND plot2 = '" + pos1 + "')").getInt(1) == 0
                    && statement.executeQuery("SELECT count(*) FROM plot_extend4 WHERE (plot1 = '" + pos3 + "' AND plot2 = '" + pos4 + "') OR (plot1 = '" + pos4 + "' AND plot2 = '" + pos3 + "')").getInt(1) == 0)
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void initPlayerdata(Player player)
    {
        try
        {
            if (statement.executeQuery("SELECT count(*) FROM player WHERE uuid = '" + player.getUniqueId() +"'").getInt(1) == 0)
                statement.execute("INSERT INTO player VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', 100)");
            else
                statement.execute("UPDATE player SET name = '" + player.getName() + "' WHERE uuid = '" + player.getUniqueId() + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getPlotByExtendPlot(String pos)
    {
        ArrayList<String> returnValue = new ArrayList<>();

        try
        {
            ResultSet result = statement.executeQuery("SELECT * FROM plot WHERE extend = '" + pos + "'");
            while (result.next())
            {
                returnValue.add(result.getString("pos"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnValue;
    }

    public ArrayList<String> getPlotByExtendPlot(int x, int z)
    {
        ArrayList<String> returnValue = new ArrayList<>();
        String pos = x + ":" + z;

        try
        {
            ResultSet result = statement.executeQuery("SELECT * FROM plot WHERE extend = '" + pos + "'");
            while (result.next())
            {
                returnValue.add(result.getString("pos"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnValue;
    }

    public void deletePlotData(int x, int z)
    {
        try
        {
            String pos = x + ":" + z;

            statement.execute("DELETE FROM plot_data WHERE pos = '" + pos + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deletePlot(int x, int z)
    {
        try
        {
            deletePlotData(x, z);

            String pos = x + ":" + z;
            statement.execute("DELETE FROM plot WHERE pos = '" + pos + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void deletePlotData(String pos)
    {
        try
        {
            statement.execute("DELETE FROM plot_data WHERE pos = '" + pos + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeExtends(int x1, int z1, int x2, int z2) // (x1, z1)가 확장기준인 플롯들을 (x2, z2)로 바꾸는 함수
    {
        try
        {
            String pos1 = x1 + ":" + z1;
            ArrayList<String> plots = new ArrayList<>();

            ResultSet result = statement.executeQuery("SELECT * FROM plot WHERE extend = '" + pos1 + "'");
            while (result.next())
            {
                plots.add(result.getString("pos"));
            }

            for (String pos : plots)
            {
                String[] posSplit = pos.split(":");
                changeExtend(Integer.parseInt(posSplit[0]), Integer.parseInt(posSplit[1]), x2, z2);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeExtend(int x1, int z1, int x2, int z2)
    {
        try
        {
            String pos1 = x1 + ":" + z1;
            String pos2 = x2 + ":" + z2;

            statement.execute("UPDATE plot SET extend = '" + pos2 + "' WHERE pos = '" + pos1 + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void changeExtend(String pos1, String pos2)
    {
        try
        {
            statement.execute("UPDATE plot SET extend = '" + pos2 + "' WHERE pos = '" + pos1 + "'");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertExtend2(int x1, int z1, int x2, int z2)
    {
        try
        {
            String pos1 = x1 + ":" + z1;
            String pos2 = x2 + ":" + z2;

            statement.execute("INSERT INTO plot_extend2 VALUES ('" + pos1 + "', '" + pos2 + "')");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertExtend4(int x1, int z1, int x2, int z2)
    {
        try
        {
            String pos1 = x1 + ":" + z1;
            String pos2 = x2 + ":" + z2;

            statement.execute("INSERT INTO plot_extend4 VALUES ('" + pos1 + "', '" + pos2 + "')");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertPlotData(String pos, PlotDataEx data)
    {
        try
        {
            statement.execute("INSERT INTO plot_data VALUES ('" + pos + "', " + data.skin1 + ", " + data.skin2 + ", " + data.skin3 + ", '" + data.biome + "', " + data.pvp + ", " + data.click + ", " + data.blockClick + ", " + data.itemClear + ")");

            insertPlayerPlotData(pos, data.owner, "owner");
            for (String player : data.helpers)
                insertPlayerPlotData(pos, player, "helper");
            for (String player : data.denies)
                insertPlayerPlotData(pos, player, "deny");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertPlotData(int x, int z)
    {
        try
        {
            String pos = x + ":" + z;

            statement.execute("INSERT INTO plot_data VALUES ('" + pos + "', 0, 0, 0, 'PLAINS', 0, 1, 0, 1)");
            statement.execute("INSERT INTO plot VALUES ('" + pos + "', '" + pos + "')");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertPlayerPlotData(String pos, String uuid, String authority)
    {
        try
        {
            statement.execute("INSERT INTO player_plot VALUES ('" + uuid + "', '" + authority + "', '" + pos + "')");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void insertPlayerPlotData(int x, int z, String uuid, String authority)
    {
        try
        {
            String pos = x + ":" + z;
            statement.execute("INSERT INTO player_plot VALUES ('" + uuid + "', '" + authority + "', '" + pos + "')");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public PlotDataEx getPlotInnerData(int x, int z)
    {
        PlotLocData locData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(x, z);

        if (locData.extendSection == PlotLocData.EXTEND_SECTION.LEFT)
            if (!getIsExtended(locData.plotLocX, locData.plotLocZ, locData.plotLocX + 1, locData.plotLocZ)) return null;
        if (locData.extendSection == PlotLocData.EXTEND_SECTION.RIGHT)
            if (!getIsExtended(locData.plotLocX, locData.plotLocZ, locData.plotLocX - 1, locData.plotLocZ)) return null;
        if (locData.extendSection == PlotLocData.EXTEND_SECTION.TOP)
            if (!getIsExtended(locData.plotLocX, locData.plotLocZ, locData.plotLocX, locData.plotLocZ + 1)) return null;
        if (locData.extendSection == PlotLocData.EXTEND_SECTION.BOTTOM)
            if (!getIsExtended(locData.plotLocX, locData.plotLocZ, locData.plotLocX, locData.plotLocZ - 1)) return null;
        if (locData.extendSection == PlotLocData.EXTEND_SECTION.LEFT_TOP)
            if (!getIsExtended4(locData.plotLocX, locData.plotLocZ, locData.plotLocX + 1, locData.plotLocZ + 1)) return null;
        if (locData.extendSection == PlotLocData.EXTEND_SECTION.RIGHT_TOP)
            if (!getIsExtended4(locData.plotLocX, locData.plotLocZ, locData.plotLocX - 1, locData.plotLocZ + 1)) return null;
        if (locData.extendSection == PlotLocData.EXTEND_SECTION.LEFT_BOTTOM)
            if (!getIsExtended4(locData.plotLocX, locData.plotLocZ, locData.plotLocX + 1, locData.plotLocZ - 1)) return null;
        if (locData.extendSection == PlotLocData.EXTEND_SECTION.RIGHT_BOTTOM)
            if (!getIsExtended4(locData.plotLocX, locData.plotLocZ, locData.plotLocX - 1, locData.plotLocZ - 1)) return null;

            return getPlotDataEx(locData.plotLocX, locData.plotLocZ);
    }
}
