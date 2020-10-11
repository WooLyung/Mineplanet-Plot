package woolyung.main.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class BlockEventListener implements Listener
{
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();

        if (!player.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
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

    @EventHandler
    public void onBlockFormEvent(BlockFormEvent event) {
        if (!event.getBlock().getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
            return;

        if (event.getNewState().getType() == Material.STONE || event.getNewState().getType() == Material.OBSIDIAN || event.getNewState().getType() == Material.COBBLESTONE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromToEvent(BlockFromToEvent event)
    {
        Block from = event.getBlock();
        Block to = event.getToBlock();

        if (!from.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
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

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event)
    {
        Player player = event.getPlayer();

        if (!player.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
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
