package woolyung.main.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class BlockFromToEventListener implements Listener
{
    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent event)
    {
        Block from = event.getBlock();
        Block to = event.getToBlock();

        if (from.getWorld().getName().compareTo(MineplanetPlot.instance.getConfig().getString("world")) != 0) // 월드가 다름
            return;

        if (from.getType() == Material.LAVA || from.getType() == Material.WATER)
        {
            PlotDataEx toData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(to.getLocation().getBlockX(), to.getLocation().getBlockZ());
            PlotDataEx fromData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(from.getLocation().getBlockX(), from.getLocation().getBlockZ());

            if ((fromData == null && toData != null) || (fromData != null && toData == null))
            {
                event.setCancelled(true);
            }
            else if (fromData != null && toData != null)
            {
                if (!fromData.flow)
                {
                    event.setCancelled(true);
                }
            }
        }
    }
}
