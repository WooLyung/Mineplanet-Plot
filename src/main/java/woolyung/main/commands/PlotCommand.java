package woolyung.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.PlotWorld;

public class PlotCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            PlotWorld.PlotLocData data = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player.getLocation().getBlockX(), player.getLocation().getBlockZ());
            player.sendMessage("플롯좌표:" + data.plotLocX + ", " + data.plotLocZ);
            player.sendMessage("내부좌표:" + data.plotInnerLocX + ", " + data.plotInnerLocZ);
            player.sendMessage("구역:" + data.plotSection);
        }

        return true;
    }
}