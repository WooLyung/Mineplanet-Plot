package woolyung.main.Events;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class CreatureSpawnEventListener implements Listener
{
    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event)
    {
        Entity entity = event.getEntity();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if (entity.getWorld().getName().compareTo(MineplanetPlot.instance.getConfig().getString("world")) != 0) // 월드가 다름
            return;

        if (reason == CreatureSpawnEvent.SpawnReason.NATURAL)
        {
            PlotDataEx data = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(entity.getLocation().getBlockX(), entity.getLocation().getBlockZ());
            if (data == null)
            {
                event.setCancelled(true);
                return;
            }
            else if (!data.spawn_animal)
            {
                event.setCancelled(true);
                return;
            }
        }
        else if (reason == CreatureSpawnEvent.SpawnReason.BREEDING
                || reason == CreatureSpawnEvent.SpawnReason.EGG
                || reason == CreatureSpawnEvent.SpawnReason.DISPENSE_EGG)
        {
            event.setCancelled(true);
        }
    }
}
