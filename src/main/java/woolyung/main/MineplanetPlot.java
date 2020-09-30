package woolyung.main;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MineplanetPlot extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getLogger().info("플러그인이 활성화되었습니다.");

        createConfig();
        createPluginDirectory();
        createDataDirectory();
    }

    @Override
    public void onDisable()
    {
        getLogger().info("플러그인이 비활성화되었습니다.");
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
                getLogger().info("[마인플래닛플롯] 플러그인 폴더 생성에 실패했습니다. 플러그인을 비활성화합니다.");
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
                getLogger().info("[마인플래닛플롯] 데이터 폴더 생성에 실패했습니다. 플러그인을 비활성화합니다.");
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
