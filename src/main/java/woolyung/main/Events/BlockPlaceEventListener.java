package woolyung.main.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class BlockPlaceEventListener implements Listener
{
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();

        if (player.getWorld().getName().compareTo(MineplanetPlot.instance.getConfig().getString("world")) != 0) // 월드가 다름
            return;

        if (player.hasPermission("mcplanetplot.permission.place")) // 권한이 있음
            return;

        PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockZ());
        if (plotData != null)
        {
            if (plotData.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                    || plotData.helpers.contains(player.getUniqueId().toString()))
                return;
        }

        event.setCancelled(true);
    }
}
