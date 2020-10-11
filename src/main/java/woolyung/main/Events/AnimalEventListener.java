package woolyung.main.Events;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlotDataEx;

public class AnimalEventListener implements Listener
{
    public class CreatureThread extends Thread
    {
        MineplanetPlot plugin;
        World world;

        @Override
        public void run()
        {
            plugin = MineplanetPlot.instance;
            world = plugin.getPlotWorld().getWorld();

            while (plugin.isEnabled())
            {
                try
                {
                    Thread.sleep(5000);

                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (Entity entity : world.getEntities())
                        {
                            if (isAnimal(entity) && !entity.getWorld().equals(plugin.getPlotWorld().getWorld()))
                            {
                                PlotDataEx data = MineplanetPlot.instance.getPlotDatabase().getPlotInnerData(entity.getLocation().getBlockX(), entity.getLocation().getBlockZ());
                                if (data == null)
                                {
                                    entity.remove();
                                }
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    Thread thread;

    public AnimalEventListener()
    {
        thread = new CreatureThread();
        thread.start();
    }

    private boolean isAnimal(Entity entity)
    {
        EntityType type = entity.getType();

        if (type == EntityType.BAT || type == EntityType.BEE || type == EntityType.CAT || type == EntityType.CHICKEN || type == EntityType.COD || type == EntityType.COW || type == EntityType.DOLPHIN
                || type == EntityType.DONKEY || type == EntityType.FOX || type == EntityType.HORSE || type == EntityType.LLAMA || type == EntityType.MUSHROOM_COW || type == EntityType.OCELOT || type == EntityType.PANDA
                || type == EntityType.PARROT || type == EntityType.PIG || type == EntityType.POLAR_BEAR || type == EntityType.PUFFERFISH || type == EntityType.RABBIT || type == EntityType.SALMON || type == EntityType.SHEEP
                || type == EntityType.SQUID || type == EntityType.TROPICAL_FISH || type == EntityType.TURTLE || type == EntityType.WOLF)
            return true;

        return false;
    }

    @EventHandler
    public void onCreatureSpawnEvent(CreatureSpawnEvent event)
    {
        Entity entity = event.getEntity();
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();

        if (!entity.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
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

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player)
        {
            Player attacker = (Player) event.getDamager();

            if (!attacker.getWorld().equals(MineplanetPlot.instance.getPlotWorld().getWorld())) // 월드가 다름
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
