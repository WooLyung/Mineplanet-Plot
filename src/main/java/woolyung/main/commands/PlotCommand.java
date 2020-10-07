package woolyung.main.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import woolyung.main.MineplanetPlot;
import woolyung.main.plot.Data.PlayerData;
import woolyung.main.plot.Data.PlayerDataEx;
import woolyung.main.plot.Data.PlotDataEx;
import woolyung.main.plot.Data.PlotLocData;
import woolyung.util.UUIDUtil;

import java.util.ArrayList;
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
                player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_arg"));
            }
            else if (args[0].compareTo("help") == 0 || args[0].compareTo("?") == 0)
            {
                arg_help(sender, command, label, args, player);
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
            else if (args[0].compareTo("helper") == 0)
            {
                arg_helper(sender, command, label, args, player);
            }
            else if (args[0].compareTo("deny") == 0)
            {
                arg_deny(sender, command, label, args, player);
            }
            else if (args[0].compareTo("set") == 0)
            {
                arg_set(sender, command, label, args, player);
            }
            else if (args[0].compareTo("merge") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.merge"))
                    arg_merge(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("detach") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.detach"))
                    arg_detach(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("delete") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.delete"))
                    arg_delete(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("move") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.move"))
                    arg_move(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("give") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.give"))
                    arg_give(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("maxplot") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.maxplot"))
                    arg_maxplot(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("setskin") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.setskin"))
                    arg_setskin(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("op") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.ophelp"))
                    arg_op(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("clear") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.clear"))
                    arg_clear(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else if (args[0].compareTo("setbiome") == 0)
            {
                if (player.hasPermission("mcplanetplot.permission.setbiome"))
                    arg_setbiome(sender, command, label, args, player);
                else
                    player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.no_permission"));
            }
            else
            {
                player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.command.wrong_command"));
            }
        }

        return true;
    }

    private void arg_help(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        player.sendMessage("§a[Plot] ─────────────────────────");
        player.sendMessage("§a · §7/plot help §f: 플롯 명령어를 봅니다");
        player.sendMessage("§a · §7/plot info §f: 플롯의 정보를 확인합니다");
        player.sendMessage("§a · §7/plot buy §f: 플롯을 구매합니다");
        player.sendMessage("§a · §7/plot list [p] §f: 플레이어의 플롯을 확인합니다");
        player.sendMessage("§a · §7/plot home [n] §f: 자신의 플롯으로 이동합니다");
        player.sendMessage("§a · §7/plot tp <x> <z> §f: 해당 플롯으로 이동합니다");
        player.sendMessage("§a · §7/plot helper <p> §f: 플롯의 도우미를 추가/삭제합니다");
        player.sendMessage("§a · §7/plot deny <p> §f: 플롯에서 플레이어를 차단/해제합니다");
        player.sendMessage("§a · §7/plot set §f: 플롯을 설정합니다");
        player.sendMessage("§a · §7/plot op §f: 관리자 명령어를 확인합니다 §c[OP]");
    }

    private void arg_op(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        player.sendMessage("§a[Plot] ─────────────────────────");
        player.sendMessage("§a · §7/plot merge §f: 플롯을 병합합니다");
        player.sendMessage("§a · §7/plot detach §f: 플롯의 모든 병합을 해제합니다");
        player.sendMessage("§a · §7/plot delete §f: 플롯을 삭제합니다");
        player.sendMessage("§a · §7/plot move <x> <z> §f: 플롯을 이동시킵니다");
        player.sendMessage("§a · §7/plot give <p> §f: 플롯을 지급합니다");
        player.sendMessage("§a · §7/plot maxplot <p> <n> §f: 최대 플롯을 설정합니다");
        player.sendMessage("§a · §7/plot setskin <s> §f: 플롯 스킨을 설정합니다");
        player.sendMessage("§a · §7/plot setbiome <b> §f: 바이옴을 설정합니다");
        player.sendMessage("§a · §7/plot clear §f: 플롯을 초기화합니다");
    }

    private void arg_set(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.set.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        PlotDataEx plotDataEx = MineplanetPlot.instance.getPlotDatabase().getPlotDataEx(x, z);
        if (plotDataEx == null)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.set.no_owner")); // 주인 없는 플롯
            return;
        }
        if (plotDataEx.owner.compareTo(player.getUniqueId().toString()) != 0)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.set.diff_owner")); // 다른 주인
            return;
        }

        if (args.length == 1 || args.length == 2)
        {
            player.sendMessage("§a[Plot] ─────────────────────────");
            player.sendMessage("§a · §7/plot set <s> <on/off> §f: 플롯을 설정합니다");
            player.sendMessage("");
            player.sendMessage(getSettingString(plotDataEx.pvp) + "§7 pvp §f: 플롯 내 pvp를 설정합니다");
            player.sendMessage(getSettingString(plotDataEx.blockClick) + "§7 blcok_click §f: 상자, 화로 등의 클릭을 설정합니다");
            player.sendMessage(getSettingString(plotDataEx.click) + "§7 click §f: 기타 우클릭을 설정합니다");
            player.sendMessage(getSettingString(plotDataEx.itemClear) + "§7 item_clear §f: 떨어진 아이템 청소를 설정합니다");
            player.sendMessage(getSettingString(plotDataEx.spawn_animal) + "§7 spawn_animal §f: 동물 스폰을 설정합니다");
            player.sendMessage(getSettingString(plotDataEx.attack_animal) + "§7 attack_animal §f: 동물 공격을 설정합니다");
            player.sendMessage(getSettingString(plotDataEx.flow) + "§7 flow §f: 물과 용암의 흐름을 설정합니다");
        }
        else
        {
            if (args[1].compareTo("pvp") != 0 && args[1].compareTo("block_click") != 0 && args[1].compareTo("click") != 0 && args[1].compareTo("item_clear") != 0 && args[1].compareTo("spawn_animal") != 0 && args[1].compareTo("attack_animal") != 0 && args[1].compareTo("flow") != 0)
            {
                player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.set.wrong_set")); // 잘못된 설정
                return;
            }
            if (args[2].compareTo("on") != 0 && args[2].compareTo("off") != 0)
            {
                player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.set.wrong_value")); // 잘못된 값
                return;
            }

            MineplanetPlot.instance.getPlotManager().settingPlotInt(x, z, args[1], (args[2].compareTo("on") == 0 ? 1 : 0));
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.set.success")); // 성공
        }
    }

    private String getSettingString(boolean value)
    {
        return value ? "§a(on)" : "§c(off)";
    }

    private void arg_setbiome(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        if (args.length < 2)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setbiome.no_arg")); // 바이옴 입력하셈
            return;
        }

        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setbiome.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().setBiomes(x, z, args[1]);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setbiome.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setbiome.no_owner")); // 주인이 없는 플롯
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setbiome.wrong_biome")); // 잘못된 바이옴
    }

    private void arg_clear(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.clear.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().clearPlots(x, z);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.clear.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.clear.no_owner")); // 주인이 없는 플롯
    }

    private void arg_setskin(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        if (args.length < 2)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setskin.no_arg")); // 스킨 이름 입력하셈
            return;
        }

        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setskin.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().setSkinPlots(x, z, args[1]);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setskin.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setskin.no_owner")); // 주인이 없는 플롯
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.setskin.no_data")); // 데이터가 없는 스킨
    }

    private void arg_deny(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        if (args.length < 2)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.no_arg")); // 닉네임 입력하셈
            return;
        }

        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().setDeny(x, z, args[1], player);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.add")); // 차단 추가됨
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.sub")); // 차단 삭제됨
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.no_player_data")); // 데이터가 없는 플레이어
        else if (result == 3) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.no_owner")); // 주인이 없는 플롯
        else if (result == 4) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.diff_owner")); // 주인이 다른 플롯
        else if (result == 5) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.deny.helper")); // 헬퍼 플레이어
    }

    private void arg_helper(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        if (args.length < 2)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.no_arg")); // 닉네임 입력하셈
            return;
        }

        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().setHelper(x, z, args[1], player);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.add")); // 도우미 추가됨
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.sub")); // 도우미 삭제됨
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.no_player_data")); // 데이터가 없는 플레이어
        else if (result == 3) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.no_owner")); // 주인이 없는 플롯
        else if (result == 4) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.diff_owner")); // 주인이 다른 플롯
        else if (result == 5) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.helper.deny")); // 차단 플레이어
    }

    private void arg_give(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        if (args.length < 2)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.give.no_arg")); // 플레이어 닉네임 입력하셈
            return;
        }

        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.give.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int radius = MineplanetPlot.instance.getConfig().getInt("radius");
        if (x >= radius || x <= -radius || z >= radius || z <= -radius)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.give.over_radius")); // 범위 넘음
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().givePlot(args[1], x, z);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.give.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.give.no_player_data")); // 데이터가 존재하지 않음
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.give.exist_plot_owner")); // 주인이 있는 플롯임
    }

    private void arg_maxplot(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        if (args.length < 3)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.maxplot.no_arg")); // 최대 개수 입력하셈
            return;
        }

        int max = 0;
        try
        {
            max = Integer.parseInt(args[2]);
        }
        catch (Exception e)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.maxplot.arg_error")); // 명령어 에러
            return;
        }

        int result = MineplanetPlot.instance.getPlotDatabase().setMaxPlot(args[1], max);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.maxplot.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.maxplot.error")); // 에러 발생
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.maxplot.no_player_data")); // 데이터가 존재하지 않음
    }

    private void arg_move(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int toX = 0, toZ = 0;

        if (args.length < 3)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.tp.no_arg")); // 좌표 입력하셈
            return;
        }

        try
        {
            toX = Integer.parseInt(args[1]);
            toZ = Integer.parseInt(args[2]);
        }
        catch (Exception e)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.move.error")); // 명령어 에러
            return;
        }

        int radius = MineplanetPlot.instance.getConfig().getInt("radius");
        if (toX >= radius || toX <= -radius || toZ >= radius || toZ <= -radius)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.move.over_radius")); // 범위 넘음
            return;
        }

        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.move.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().movePlot(x, z, toX, toZ);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.move.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.move.no_owner")); // 주인 없음
        else if (result == 2) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.move.exist_owner")); // 이동 위치에 주인이 있음
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

    private void arg_delete(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        int player_posX = player.getLocation().getBlockX();
        int player_posZ = player.getLocation().getBlockZ();
        PlotLocData plotLocData = MineplanetPlot.instance.getPlotWorld().getPlotLocData(player_posX, player_posZ);

        int x = plotLocData.plotLocX;
        int z = plotLocData.plotLocZ;

        if (player.getWorld().getName() != MineplanetPlot.instance.getConfig().getString("world"))
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.delete.not_plot_world")); // 플롯 월드가 아님
            return;
        }

        int result = MineplanetPlot.instance.getPlotManager().deletePlot(x, z);

        if (result == 0) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.delete.success")); // 성공
        else if (result == 1) player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.delete.no_owner")); // 주인 없음
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
        int plot = 1;
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
        if (plot <= 0)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.home.smaller_one")); // 1 이상으로 적어야함
            return;
        }
        if (playerDataEx.plotCount < plot)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.home.no_plot2")); // 플롯이 그 만큼 없음
            return;
        }

        plot--;

        String[] pos = playerDataEx.plots.get(plot).split(":");
        int posX = Integer.parseInt(pos[0]) * 44;
        int posZ = Integer.parseInt(pos[1]) * 44 - 20;

        player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.home.teleport")); // 이동함
        Location location = new Location(MineplanetPlot.instance.getPlotWorld().getWorld(), posX, MineplanetPlot.instance.getConfig().getInt("height") + 1, posZ);
        player.teleport(location);
    }

    private void arg_list(CommandSender sender, Command command, String label, String[] args, Player player)
    {
        String ownerUUID;
        PlayerDataEx playerDataEx;

        if (args.length == 1)
        {
            playerDataEx = MineplanetPlot.instance.getPlotDatabase().getPlayerDataEx(player);
        }
        else
        {
            playerDataEx = MineplanetPlot.instance.getPlotDatabase().getPlayerDataExByName(args[1]);
        }

        if (playerDataEx == null)
        {
            player.sendMessage(MineplanetPlot.instance.getConfig().getString("message.list.no_player")); // 알 수 없는 플레이어
            return;
        }

        String plots = "";
        for (int i = 0; i < playerDataEx.plotCount; i++)
        {
            if (i != 0)
                plots += ", ";
            plots += "[" + playerDataEx.plots.get(i) + "]";
        }

        player.sendMessage("§a[Plot] ─────────────────────────");
        player.sendMessage("§a · §7주인 §f: [" + playerDataEx.name + "]");
        player.sendMessage("§a · §7플롯개수 §f: " + playerDataEx.plotCount + "/" + playerDataEx.maxPlot);
        player.sendMessage("§a · §7보유플롯 §f: " + plots);
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
            player.sendMessage("§a[Plot] ─────────────────────────");
            player.sendMessage("§a · §7플롯주소 §f: [" + plotLocData.plotLocX + ":" + plotLocData.plotLocZ + "]");
            player.sendMessage("§a · §7주인 §f: 없음");
        }
        else
        {
            String helpers = "";
            String denies = "";

            for (int i = 0; i < plotDataEx.helpers.size(); i++)
            {
                if (i != 0)
                {
                    helpers += ", ";
                }
                helpers += plotDataEx.helpers.get(i);
            }
            for (int i = 0; i < plotDataEx.denies.size(); i++)
            {
                if (i != 0)
                {
                    denies += ", ";
                }
                denies += plotDataEx.denies.get(i);
            }

            player.sendMessage("§a[Plot] ─────────────────────────");
            player.sendMessage("§a · §7플롯주소 §f: [" + plotLocData.plotLocX + ":" + plotLocData.plotLocZ + "]");
            player.sendMessage("§a · §7연결플롯 §f: " + MineplanetPlot.instance.getPlotDatabase().getPlotByExtendPlot(plotDataEx.extend).size() + "개");
            player.sendMessage("§a · §7스킨 §f: " + MineplanetPlot.instance.getSkinDatabase().getSkinData(plotDataEx.skin).display_name);
            player.sendMessage("§a · §7바이옴 §f: " + MineplanetPlot.instance.getPlotManager().getBiomeName(plotDataEx.biome));
            player.sendMessage("§a · §7주인 §f: " + UUIDUtil.getName(plotDataEx.owner));
            player.sendMessage("§a · §7도우미 §f: " + helpers);
            player.sendMessage("§a · §7차단 §f: " + denies);
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