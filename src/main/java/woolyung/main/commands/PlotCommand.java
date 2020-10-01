package woolyung.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlayerDataEx;
import woolyung.main.plot.Data.PlotDataEx;
import woolyung.main.plot.Data.PlotLocData;
import woolyung.util.UUIDUtil;

public class PlotCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            if (args.length == 0)
            {
                arg_none(sender, command, label, args, player);
                return true;
            }
            if (args[0].compareTo("help") == 0 || args[0].compareTo("?") == 0)
            {
                player.sendMessage("플롯 명령어 도움말");
                return true;
            }
            if (args[0].compareTo("list") == 0)
            {
                arg_list(sender, command, label, args, player);
                return true;
            }
            if (args[0].compareTo("buy") == 0)
            {
                arg_buy(sender, command, label, args, player);
                return true;
            }
        }

        return true;
    }

    private void arg_list(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        PlayerDataEx playerDataEx = MineplanetPlot.instance.getPlotDatabase().getPlayerDataEx(player);
        player.sendMessage("플롯 개수 : " + playerDataEx.plotCount + "/" + playerDataEx.maxPlot);
        player.sendMessage("갖고 있는 플롯 : ");
        for (String str : playerDataEx.plots)
            player.sendMessage(str);
    }

    private void arg_none(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);
        PlotDataEx plotDataEx = MineplanetPlot.instance.getPlotDatabase().getPlotDataEx(plotLocData.plotLocX, plotLocData.plotLocZ);

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.none.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        if (plotLocData.plotSection != PlotLocData.PLOT_SECTION.PLOT
                && plotLocData.plotSection != PlotLocData.PLOT_SECTION.INNER_LINE
                && plotLocData.plotSection != PlotLocData.PLOT_SECTION.SKIN)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.none.not_plot")); // 플롯이 아님
            return;
        }

        if (plotDataEx == null)
        {
            player.sendMessage("[" + plotLocData.plotLocX + ":" + plotLocData.plotLocZ + "] 주인 없는 플롯"); // 주인 없는 플롯
        }
        else
        {
            player.sendMessage("[" + plotLocData.plotLocX + ":" + plotLocData.plotLocZ + "] " + UUIDUtil.getName(plotDataEx.owner) + " 님의 플롯 ");
            player.sendMessage("도우미 리스트 : ");
            for (String str : plotDataEx.helpers)
                player.sendMessage(UUIDUtil.getName(str));
            player.sendMessage("차단 리스트 : ");
            for (String str : plotDataEx.denies)
                player.sendMessage(UUIDUtil.getName(str));
        }
    }

    private void arg_buy(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);
        PlotDataEx plotDataEx = MineplanetPlot.instance.getPlotDatabase().getPlotDataEx(plotLocData.plotLocX, plotLocData.plotLocZ);
        PlayerDataEx playerDataEx = MineplanetPlot.instance.getPlotDatabase().getPlayerDataEx(player);

        if (playerDataEx != null)
        {
            if (playerDataEx.plotCount >= playerDataEx.maxPlot)
            {
                player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.plot_limit")); // 최대 플롯을 넘었음
                return;
            }
        }

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        if (plotLocData.plotSection != PlotLocData.PLOT_SECTION.PLOT
                && plotLocData.plotSection != PlotLocData.PLOT_SECTION.INNER_LINE
                && plotLocData.plotSection != PlotLocData.PLOT_SECTION.SKIN)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.not_plot")); // 플롯이 아님
            return;
        }

        if (plotDataEx != null)
        {
            if (plotDataEx.owner.compareTo(player.getUniqueId().toString()) == 0)
            {
                player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.already_my_plot")); // 이미 내 플롯임
            }
            else
            {
                player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.exist_plot_owner")); // 다른 사람 플롯임
            }
            return;
        }

        player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.buy_plot")); // 플롯 구매함
        MineplanetPlot.instance.getPlotDatabase().insertPlotData(plotLocData.plotLocX, plotLocData.plotLocZ);
        MineplanetPlot.instance.getPlotDatabase().insertPlayerPlotData(plotLocData.plotLocX, plotLocData.plotLocZ, player.getUniqueId().toString(), "owner");
    }
}