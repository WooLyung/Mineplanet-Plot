package woolyung.main;

import org.bukkit.plugin.java.JavaPlugin;
import woolyung.main.Events.*;
import woolyung.main.commands.PlotCommand;
import woolyung.main.plot.PlotManager;
import woolyung.main.plot.PlotWorld;

import java.io.File;

public final class MineplanetPlot extends JavaPlugin
{
    private PlotWorld plotWorld;
    private PlotManager plotManager;
    private SkinManager skinManager;
    private PlotDatabase plotDatabase;
    private SkinDatabase skinDatabase;

    public static MineplanetPlot instance;

    public MineplanetPlot()
    {
        instance = this;
    }

    @Override
    public void onEnable()
    {
        if (createPluginDirectory());
        {
            createDataDirectory();
            createSkinDirectory();
        }

        createConfig();

        init();
    }

    @Override
    public void onDisable()
    {
    }

    public SkinDatabase getSkinDatabase() {
        return skinDatabase;
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }

    public PlotManager getPlotManager() { return plotManager; }

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
        skinDatabase = new SkinDatabase();
        plotManager = new PlotManager(this, plotDatabase, plotWorld);
        skinManager = new SkinManager(this, skinDatabase, plotWorld);

        getCommand("plot").setExecutor(new PlotCommand());

        getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakEventListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBucketEmptyEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerBucketFillEventListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityEventListener(), this);
        getServer().getPluginManager().registerEvents(new CreatureSpawnEventListener(), this);
        getServer().getPluginManager().registerEvents(new BlockFromToEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEntityEventListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractEventListener(), this);
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
        File folder = new File(getDataFolder().getPath() + "/datas");

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

    private boolean createSkinDirectory()
    {
        File folder = new File(getDataFolder().getPath() + "/skins");

        if (!folder.exists())
        {
            getLogger().info("스킨 폴더가 존재하지 않습니다. 생성을 시도합니다.");

            try
            {
                folder.mkdir();
                getLogger().info("스킨 폴더 생성을 성공했습니다.");
                return true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                getLogger().info("스킨 폴더 생성에 실패했습니다. 플러그인을 비활성화합니다.");
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
