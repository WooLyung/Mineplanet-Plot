package woolyung.main.Events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class ClickEventListener implements Listener
{
    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event)
    {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (!player.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
            return;

        if (player.hasPermission("mcplanetplot.permission.click")) // 권한이 있음
            return;

        PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(entity.getLocation().getBlockX(), entity.getLocation().getBlockZ());
        if (plotData != null) {
            if (plotData.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                    || plotData.helpers.contains(player.getUniqueId().toString()))
                return;

            if (plotData.click)
                return;

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!player.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
            return;

        if (player.hasPermission("mcplanetplot.permission.click")) // 권한이 있음
            return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            PlotDataEx plotDataBlock = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(block.getLocation().getBlockX(), block.getLocation().getBlockZ());

            if (plotDataBlock != null) {
                if (block.getType() == Material.SWEET_BERRY_BUSH) {
                    if (!(plotDataBlock.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미가 아님
                            || plotDataBlock.helpers.contains(player.getUniqueId().toString())))
                        event.setCancelled(true);
                }

                if (block.getType() == Material.CHEST || block.getType() == Material.FURNACE || block.getType() == Material.SHULKER_BOX || block.getType() == Material.LOOM || block.getType() == Material.BARREL
                        || block.getType() == Material.SMOKER || block.getType() == Material.BLAST_FURNACE || block.getType() == Material.GRINDSTONE || block.getType() == Material.STONECUTTER
                        || block.getType() == Material.DISPENSER || block.getType() == Material.DROPPER || block.getType() == Material.TRAPPED_CHEST || block.getType() == Material.HOPPER
                        || block.getType() == Material.LECTERN || block.getType() == Material.FLOWER_POT) {
                    if (plotDataBlock.blockClick)
                        return;
                }
                else {
                    if (plotDataBlock.click)
                        return;
                }

                if (plotDataBlock.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                        || plotDataBlock.helpers.contains(player.getUniqueId().toString()))
                    return;

                event.setCancelled(true);
            }
        }
        else if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            PlotDataEx plotDataPlayer = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(player.getLocation().getBlockX(), player.getLocation().getBlockZ());

            if (plotDataPlayer != null) {
                if (plotDataPlayer.click)
                    return;

                if (plotDataPlayer.owner.compareTo(player.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                        || plotDataPlayer.helpers.contains(player.getUniqueId().toString()))
                    return;

                event.setCancelled(true);
            }
        }
    }
}