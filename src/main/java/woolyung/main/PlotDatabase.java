package woolyung.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
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
        path = JavaPlugin.getProvidingPlugin(MineplanetPlot.class).getDataFolder() + "/Datas/";
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
            JavaPlugin.getProvidingPlugin(MineplanetPlot.class).getPluginLoader().disablePlugin(JavaPlugin.getProvidingPlugin(MineplanetPlot.class));
            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Bukkit.getLogger().info("데이터베이스 예외가 발생했습니다. 플러그인을 비활성화합니다.");
            JavaPlugin.getProvidingPlugin(MineplanetPlot.class).getPluginLoader().disablePlugin(JavaPlugin.getProvidingPlugin(MineplanetPlot.class));
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
                statement.execute("CREATE TABLE player (uuid TEXT PRIMARY KEY, max_plot INTEGER)");
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'player_plot'").getInt(1) == 0)
                statement.execute("CREATE TABLE player_plot (uuid TEXT, authority TEXT, pos TEXT, FOREIGN KEY(pos) REFERENCES plot(pos) ON DELETE CASCADE)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Bukkit.getLogger().info("데이터베이스 예외가 발생했습니다. 플러그인을 비활성화합니다.");
            JavaPlugin.getProvidingPlugin(MineplanetPlot.class).getPluginLoader().disablePlugin(JavaPlugin.getProvidingPlugin(MineplanetPlot.class));
            return;
        }
    }
}
