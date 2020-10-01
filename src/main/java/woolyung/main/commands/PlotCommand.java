package woolyung.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlayerData;
import woolyung.main.plot.Data.PlotData;
import woolyung.main.plot.Data.PlotLocData;
import woolyung.main.plot.PlotWorld;

public class PlotCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (args.length == 0) //          /plot
            {
                player.sendMessage("지금 있는 플롯 정보");
                return true;
            }
            if (args[0].compareTo("help") == 0|| args[0].compareTo("?") == 0) //          /plot help
            {
                player.sendMessage("플롯 명령어 도움말");
                return true;
            }
            if (args[0].compareTo("buy") == 0)//          /plot buy
            {
                arg_buy(sender, command, label, args, player);
            }
        }

        return true;
    }

    private void arg_buy(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.not_plot_world"));
            return;
        }

        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);
        PlotData plotData = MineplanetPlot.instance.getPlotDatabase().getPlotData(plotLocData.plotLocX, plotLocData.plotLocZ);
        PlayerData playerData = MineplanetPlot.instance.getPlotDatabase().getPlayerData(player);

        if (plotLocData.plotSection != PlotLocData.PLOT_SECTION.PLOT
            && plotLocData.plotSection != PlotLocData.PLOT_SECTION.INNER_LINE
            && plotLocData.plotSection != PlotLocData.PLOT_SECTION.SKIN)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.not_plot"));
            return;
        }

        if (plotData != null)
        {
            // 이미 주인이 있는 땅
            return;
        }

        // 이미 최대 개수임

        player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.buy_plot"));
        MineplanetPlot.instance.getPlotDatabase().insertPlotData(plotLocData.plotLocX, plotLocData.plotLocZ);
        MineplanetPlot.instance.getPlotDatabase().insertPlayerPlotData(plotLocData.plotLocX, plotLocData.plotLocZ, player.getUniqueId().toString(), "owner");
    }
}