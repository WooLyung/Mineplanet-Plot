package woolyung.main;

import org.bukkit.plugin.java.JavaPlugin;
import woolyung.main.commands.PlotCommand;
import woolyung.main.plot.PlotWorld;

import java.io.File;

public final class MineplanetPlot extends JavaPlugin
{
    private PlotWorld plotWorld;
    private PlotDatabase plotDatabase;

    public static MineplanetPlot instance;

    public MineplanetPlot()
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        createConfig();
        createPluginDirectory();
        createDataDirectory();

        init();
    }

    @Override
    public void onDisable()
    {
    }

    public PlotDatabase getPlotDatabase()
    {
        return plotDatabase;
    }

    public PlotWorld getPlotWorld()
    {
        return plotWorld;
    }

    private void init()
    {
        plotWorld = new PlotWorld(getConfig().getString("world"));
        plotDatabase = new PlotDatabase();

        this.getCommand("plot").setExecutor(new PlotCommand());
    }

    private void createConfig()
    {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private boolean createPluginDirectory()
    {
        File folder = getDataFolder();

        if (!folder.exists())
        {
            getLogger().info("플러그인 폴더가 존재하지 않습니다. 생성을 시도합니다.");

            try
            {
                folder.mkdir();
                getLogger().info("플러그인 폴더 생성을 성공했습니다.");
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                getLogger().info("플러그인 폴더 생성에 실패했습니다. 플러그인을 비활성화합니다.");
                getPluginLoader().disablePlugin(this);
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    private boolean createDataDirectory()
    {
        File folder = new File(getDataFolder().getPath() + "/Datas");

        if (!folder.exists())
        {
            getLogger().info("데이터 폴더가 존재하지 않습니다. 생성을 시도합니다.");

            try
            {
                folder.mkdir();
                getLogger().info("데이터 폴더 생성을 성공했습니다.");
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                getLogger().info("데이터 폴더 생성에 실패했습니다. 플러그인을 비활성화합니다.");
                getPluginLoader().disablePlugin(this);
                return false;
            }
        }
        else
        {
            return true;
        }
    }
}
