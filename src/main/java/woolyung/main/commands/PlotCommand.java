package woolyung.main.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlayerDataEx;
import woolyung.main.plot.Data.PlotDataEx;
import woolyung.main.plot.Data.PlotLocData;
import woolyung.util.UUIDUtil;

import java.util.concurrent.ExecutionException;

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

            }
            else if (args[0].compareTo("help") == 0 || args[0].compareTo("?") == 0)
            {
                player.sendMessage("플롯 명령어 도움말");
            }
            else if (args[0].compareTo("info") == 0)
            {
                arg_info(sender, command, label, args, player);
            }
            else if (args[0].compareTo("list") == 0)
            {
                arg_list(sender, command, label, args, player);
            }
            else if (args[0].compareTo("buy") == 0)
            {
                arg_buy(sender, command, label, args, player);
            }
            else if (args[0].compareTo("home") == 0)
            {
                arg_home(sender, command, label, args, player);
            }
            else if (args[0].compareTo("tp") == 0)
            {
                arg_tp(sender, command, label, args, player);
            }
            else if (args[0].compareTo("merge") == 0)
            {
                if (player.hasPermission("mmcplanetplot.permission.merge"))
                    arg_merge(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("detach") == 0)
            {
                if (player.hasPermission("mmcplanetplot.permission.detach"))
                    arg_detach(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
        }

        return true;
    }

    private void arg_merge(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.merge.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = 0;
        if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.RIGHT) result = MineplanetPlot.instance.getPlotManager().mergePlot2(x, z, x - 1, z);
        else if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.LEFT) result = MineplanetPlot.instance.getPlotManager().mergePlot2(x, z, x + 1, z);
        else if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.TOP) result = MineplanetPlot.instance.getPlotManager().mergePlot2(x, z, x, z + 1);
        else if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.BOTTOM) result = MineplanetPlot.instance.getPlotManager().mergePlot2(x, z, x, z - 1);
        else if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.LEFT_TOP) result = MineplanetPlot.instance.getPlotManager().mergePlot4(x, z, x + 1, z + 1);
        else if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.RIGHT_TOP) result = MineplanetPlot.instance.getPlotManager().mergePlot4(x, z, x - 1, z + 1);
        else if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.RIGHT_BOTTOM) result = MineplanetPlot.instance.getPlotManager().mergePlot4(x, z, x - 1, z - 1);
        else if (plotLocData.extendSection == PlotLocData.EXTEND_SECTION.LEFT_BOTTOM) result = MineplanetPlot.instance.getPlotManager().mergePlot4(x, z, x + 1, z - 1);
        else
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.merge.not_between")); // 플롯 사이가 아님
            return;
        }

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.merge.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.merge.no_owner")); // 주인 없음
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.merge.diff_owner")); // 주인 다름
        else if (result == 4) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.merge.already_merged")); // 이미 병합된 플롯
        else if (result == 5) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.merge.not_merged")); // 인접한 플롯이 모두 병합된 상태가 아님
    }

    private void arg_detach(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.detach.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().detachPlot(x, z);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.detach.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.detach.no_owner")); // 주인 없음
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.detach.not_merged")); // 병합 안된 플롯
    }

    private void arg_tp(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int x, z;

        if (args.length < 3)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.tp.no_arg")); // 좌표 입력하셈
            return;
        }

        try
        {
            x = Integer.parseInt(args[1]);
            z = Integer.parseInt(args[2]);
        }
        catch (Exception e)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.tp.error")); // 명령어 에러
            return;
        }

        int radius = MineplanetPlot.instance.getConfig().getInt("radius");
        if (x >= radius || x <= -radius || z >= radius || z <= -radius)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.tp.over_radius")); // 범위 넘음
            return;
        }

        int posX = x * 44;
        int posZ = z * 44 - 20;

        player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.tp.teleport")); // 이동함
        Location location = new Location(MineplanetPlot.instance.getPlotWorld().getWorld(), posX, MineplanetPlot.instance.getConfig().getInt("height") + 1, posZ);
        player.teleport(location);
    }

    private void arg_home(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int plot = 0;
        try
        {
            if (args.length >= 2) plot = Integer.parseInt(args[1]);
        }
        catch (Exception e)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.home.error")); // 명령어 에러
            return;
        }

        PlayerDataEx playerDataEx = MineplanetPlot.instance.getPlotDatabase().getPlayerDataEx(player);
        if (playerDataEx == null)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.home.no_plot")); // 플롯이 없음
            return;
        }
        if (playerDataEx.plotCount <= plot)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.home.no_plot2")); // 플롯이 그 만큼 없음
            return;
        }

        String[] pos = playerDataEx.plots.get(plot).split(":");
        int posX = Integer.parseInt(pos[0]) * 44;
        int posZ = Integer.parseInt(pos[1]) * 44 - 20;

        player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.home.teleport")); // 이동함
        Location location = new Location(MineplanetPlot.instance.getPlotWorld().getWorld(), posX, MineplanetPlot.instance.getConfig().getInt("height") + 1, posZ);
        player.teleport(location);
    }

    private void arg_list(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        PlayerDataEx playerDataEx = MineplanetPlot.instance.getPlotDatabase().getPlayerDataEx(player);
        player.sendMessage("플롯 개수 : " + playerDataEx.plotCount + "/" + playerDataEx.maxPlot);
        player.sendMessage("갖고 있는 플롯 : ");
        for (String str : playerDataEx.plots)
            player.sendMessage(str);
    }

    private void arg_info(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);
        PlotDataEx plotDataEx = MineplanetPlot.instance.getPlotDatabase().getPlotDataEx(plotLocData.plotLocX, plotLocData.plotLocZ);

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.info.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        if (plotLocData.plotSection != PlotLocData.PLOT_SECTION.PLOT
                && plotLocData.plotSection != PlotLocData.PLOT_SECTION.INNER_LINE
                && plotLocData.plotSection != PlotLocData.PLOT_SECTION.SKIN)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.info.not_plot")); // 플롯이 아님
            return;
        }

        if (plotDataEx == null)
        {
            player.sendMessage("[" + plotLocData.plotLocX + ":" + plotLocData.plotLocZ + "] 주인 없는 플롯"); // 주인 없는 플롯
        }
        else
        {
            player.sendMessage("[" + plotLocData.plotLocX + ":" + plotLocData.plotLocZ + "] " + UUIDUtil.getName(plotDataEx.owner) + " 님의 플롯 ");
            player.sendMessage("[" + plotDataEx.extend + "] 의 하위 플롯 ");
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

        int radius = MineplanetPlot.instance.getConfig().getInt("radius");
        if (plotLocData.plotLocX >= radius || plotLocData.plotLocX <= -radius || plotLocData.plotLocZ >= radius || plotLocData.plotLocZ <= -radius)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.tp.over_radius")); // 범위 넘음
            return;
        }

        player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.buy.buy_plot")); // 플롯 구매함
        MineplanetPlot.instance.getPlotManager().buyPlot(player, plotLocData.plotLocX, plotLocData.plotLocZ);
    }
}