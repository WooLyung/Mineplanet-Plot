package woolyung.main.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class EntityDamageByEntityEventListener implements Listener
{
    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player)
        {
            Player attacker = (Player) event.getDamager();

            if (attacker.getWorld().getName().compareTo(MineplanetPlot.instance.getConfig().getString("world")) != 0) // 월드가 다름
                return;

            if (attacker.hasPermission("mcplanetplot.permission.attack")) // 권한이 있음
                return;

            PlotDataEx plotDataAttacker = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(attacker.getLocation().getBlockX(), attacker.getLocation().getBlockZ());
            PlotDataEx plotDataVictim = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(event.getEntity().getLocation().getBlockX(), event.getEntity().getLocation().getBlockZ());

            if (event.getEntity() instanceof Player)
            {
                Player victim = (Player) event.getEntity();

                if (plotDataAttacker == null || plotDataVictim == null)
                {
                    event.setCancelled(true);
                    return;
                }
                if (!plotDataAttacker.pvp || !plotDataVictim.pvp)
                {
                    event.setCancelled(true);
                    return;
                }
            }
            else
            {
                if (plotDataVictim == null) return;
                if (plotDataVictim.attack_animal) return;
                if (plotDataVictim.owner.compareTo(attacker.getUniqueId().toString()) == 0 // 주인 혹은 도우미
                        || plotDataVictim.helpers.contains(attacker.getUniqueId().toString()))
                    return;

                event.setCancelled(true);
            }
        }
    }
}
