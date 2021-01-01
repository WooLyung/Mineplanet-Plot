package woolyung.main.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.PlotDatabase;

public class PlayerJoinEventListener implements Listener
{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        PlotDatabase database = MineplanetPlot.instance.getPlotDatabase();
        database.initPlayerdata(event.getPlayer());
    }
}