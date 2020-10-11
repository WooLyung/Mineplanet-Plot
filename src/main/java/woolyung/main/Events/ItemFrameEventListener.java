package woolyung.main.Events;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class ItemFrameEventListener implements Listener
{
    @EventHandler
    public void onFrameBreak(HangingBreakByEntityEvent event)
    {
        if (!event.getEntity().getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
            return;

        if(event.getRemover() instanceof Player)
        {
            Player player = (Player) event.getRemover();

            if (player.hasPermission("mcplanetplot.permission.hanging")) // 권한이 있음
                return;

            if(event.getEntity() instanceof ItemFrame || event.getEntity() instanceof Painting)
            {
                PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(event.getEntity().getLocation().getBlockX(), event.getEntity().getLocation().getBlockZ());
                if (plotData != null)
                {
                    if (plotData.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                            || plotData.helpers.contains(player.getUniqueId().toString()))
                        return;
                }

                event.setCancelled(true);
            }
        }
        else
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFramePlace(HangingPlaceEvent event)
    {
        Player player = event.getPlayer();

        if (!player.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
            return;

        if (player.hasPermission("mcplanetplot.permission.hanging")) // 권한이 있음
            return;

        if(event.getEntity() instanceof ItemFrame || event.getEntity() instanceof Painting)
        {
            PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(event.getEntity().getLocation().getBlockX(), event.getEntity().getLocation().getBlockZ());
            if (plotData != null)
            {
                if (plotData.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                        || plotData.helpers.contains(player.getUniqueId().toString()))
                    return;
            }

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFrameRightClick(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();

        if (!player.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
            return;

        if (player.hasPermission("mcplanetplot.permission.hanging")) // 권한이 있음
            return;

        PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(event.getRightClicked().getLocation().getBlockX(), event.getRightClicked().getLocation().getBlockZ());
        if (plotData != null)
        {
            if (plotData.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                    || plotData.helpers.contains(player.getUniqueId().toString()))
                return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onFrameLeftClick (EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player)
        {
            Player player = (Player) event.getDamager();

            if (!player.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
                return;

            if (player.hasPermission("mcplanetplot.permission.hanging")) // 권한이 있음
                return;

            PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(event.getEntity().getLocation().getBlockX(), event.getEntity().getLocation().getBlockZ());
            if (plotData != null)
            {
                if (plotData.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                        || plotData.helpers.contains(player.getUniqueId().toString()))
                    return;
            }

            event.setCancelled(true);
        }
        else
        {
            event.setCancelled(true);
        }
    }
}
