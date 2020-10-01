package woolyung.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import woolyung.main.plot.Data.PlayerData;
import woolyung.main.plot.Data.PlayerDataEx;
import woolyung.main.plot.Data.PlotData;
import woolyung.main.plot.Data.PlotDataEx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

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
        path = MineplanetPlot.instance.getDataFolder() + "/Datas/";
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
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'plot'").getInt(1) == 0)
                statement.execute("CREATE TABLE plot (pos TEXT PRIMARY KEY, skin1 INTEGER, skin2 INTEGER, skin3 INTEGER)");
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'player'").getInt(1) == 0)
                statement.execute("CREATE TABLE player (uuid TEXT PRIMARY KEY, name TEXT, max_plot INTEGER)");
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'player_plot'").getInt(1) == 0)
                statement.execute("CREATE TABLE player_plot (uuid TEXT, authority TEXT, pos TEXT, FOREIGN KEY(pos) REFERENCES plot(pos) ON DELETE CASCADE)");
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
            int skin1 = result.getInt("skin1");
            int skin2 = result.getInt("skin2");
            int skin3 = result.getInt("skin3");

            PlotData data = new PlotData();
            data.posX = posX;
            data.posZ = posZ;
            data.skin1 = skin1;
            data.skin2 = skin2;
            data.skin3 = skin3;

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
            plotDataEx.skin1 = plotData.skin1;
            plotDataEx.skin2 = plotData.skin2;
            plotDataEx.skin3 = plotData.skin3;

            ResultSet result = statement.executeQuery("SELECT * FROM player_plot WHERE pos = '" + posX + ":" + posZ + "'");
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

            ResultSet result = statement.executeQuery("SELECT * FROM player_plot WHERE uuid = '" + player.getUniqueId() + "'");
            while (result.next())
            {
                String authority = result.getString("authority");
                if (authority.compareTo("owner") == 0)
                    playerDataEx.plots.add(result.getString("pos"));
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

    public void initPlayerdata(Player player)
    {
        try
        {
            if (statement.executeQuery("SELECT count(*) FROM player WHERE uuid = '" + player.getUniqueId() +"'").getInt(1) == 0)
                statement.execute("INSERT INTO player VALUES ('" + player.getUniqueId() + "', '" + player.getName() + "', 1)");
            else
                statement.executeQuery("UPDATE player SET name = '" + player.getName() + "' WHERE uuid = '" + player.getUniqueId() + "'");
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
            statement.execute("INSERT INTO plot VALUES ('" + pos + "', 0, 0, 0)");
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
}
