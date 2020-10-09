package woolyung.main.Events;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.world.StructureGrowEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;
import woolyung.main.plot.Data.PlotLocData;

public class GrowEventListener implements Listener {
    @EventHandler
    public void onStructureGrowEvent(StructureGrowEvent event) {
        if (event.getBlocks().get(0).getWorld().getName().compareTo(MineplanetPlot.instance.getConfig().getString("world")) != 0) // 월드가 다름
            return;

        for (BlockState block : event.getBlocks()) {
            PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(block.getLocation().getBlockX(), block.getLocation().getBlockZ());
            PlotLocData locData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(block.getLocation().getBlockX(), block.getLocation().getBlockZ());
            if (plotData == null && locData.plotSection != PlotLocData.PLOT_SECTION.PLOT) {
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onBlockFertilizeEvent(BlockFertilizeEvent event) {
        if (event.getBlocks().get(0).getWorld().getName().compareTo(MineplanetPlot.instance.getConfig().getString("world")) != 0) // 월드가 다름
            return;

        for (BlockState block : event.getBlocks()) {
            PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(block.getLocation().getBlockX(), block.getLocation().getBlockZ());
            PlotLocData locData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(block.getLocation().getBlockX(), block.getLocation().getBlockZ());
            if (plotData == null && locData.plotSection != PlotLocData.PLOT_SECTION.PLOT) {
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onBlockSpreadEvent(BlockSpreadEvent event) {
        if (event.getBlock().getWorld().getName().compareTo(MineplanetPlot.instance.getConfig().getString("world")) != 0) // 월드가 다름
            return;

        PlotDataEx plotData = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockZ());
        PlotLocData locData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(event.getBlock().getLocation().getBlockX(), event.getBlock().getLocation().getBlockZ());
        if (plotData == null && locData.plotSection != PlotLocData.PLOT_SECTION.PLOT) {
            event.setCancelled(true);
        }
    }
}