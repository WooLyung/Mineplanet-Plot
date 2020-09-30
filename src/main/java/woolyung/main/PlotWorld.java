package woolyung.main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class PlotWorld
{
    private World world;

    public PlotWorld(String name)
    {
        WorldCreator wc = new WorldCreator(name);
        wc.generator(new PlotGenerator());

        world = Bukkit.getServer().createWorld(wc);
        Bukkit.getLogger().info("월드 생성!@#!@#");
    }
}