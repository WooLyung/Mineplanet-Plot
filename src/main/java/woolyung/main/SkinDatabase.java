package woolyung.main;

import org.bukkit.Bukkit;
import woolyung.main.plot.Data.PlotData;
import woolyung.main.plot.Data.SkinData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SkinDatabase {
    private Connection connection;
    private Statement statement;
    private String path = "";
    private String file = "";

    public SkinDatabase() {
        sqliteSetup();
    }

    private void sqliteSetup() {
        path = MineplanetPlot.instance.getDataFolder() + "/datas/";
        file = "skin.db";

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path + file);
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getLogger().info("jdbc 클래스를 찾을 수 없습니다. 플러그인을 비활성화합니다.");
            MineplanetPlot.instance.getPluginLoader().disablePlugin(MineplanetPlot.instance);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getLogger().info("데이터베이스 예외가 발생했습니다. 플러그인을 비활성화합니다.");
            MineplanetPlot.instance.getPluginLoader().disablePlugin(MineplanetPlot.instance);
            return;
        }

        createTable();
    }

    private void createTable() {
        try {
            statement.execute("PRAGMA foreign_keys = ON");

            // 스킨 데이터 테이블
            if (statement.executeQuery("SELECT count(*) FROM sqlite_master WHERE Name = 'skin_data'").getInt(1) == 0)
                statement.execute("CREATE TABLE skin_data (name TEXT PRIMARY KEY, display_name TEXT, lore TEXT)");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Bukkit.getLogger().info("데이터베이스 예외가 발생했습니다. 플러그인을 비활성화합니다.");
            MineplanetPlot.instance.getPluginLoader().disablePlugin(MineplanetPlot.instance);
            return;
        }
    }

    public SkinData getSkinData(String name)
    {
        try
        {
            if (statement.executeQuery("SELECT count(*) FROM skin_data WHERE name = '" + name + "'").getInt(1) == 0)
                return null;
            ResultSet result = statement.executeQuery("SELECT * FROM skin_data WHERE name = '" + name  + "'");

            SkinData data = new SkinData();
            data.name = name;
            data.display_name = result.getString("display_name");
            data.lore = result.getString("lore");

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
