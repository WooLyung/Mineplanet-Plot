package woolyung.main;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import woolyung.main.MineplanetPlot;
import woolyung.main.PlotDatabase;
import woolyung.main.SkinDatabase;
import woolyung.main.plot.Data.SkinData;
import woolyung.main.plot.PlotWorld;

import java.io.File;
import java.io.FileInputStream;

public class SkinManager
{
    MineplanetPlot plugin;
    SkinDatabase database;
    PlotWorld world;

    public SkinManager(MineplanetPlot plugin, SkinDatabase database, PlotWorld world)
    {
        this.plugin = plugin;
        this.database = database;
        this.world = world;
    }

    public int setSkinData(int x, int z, String skin)
    {
        SkinData data = database.getSkinData(skin);
        if (data == null) return 1; // 존재하지 않는 스킨 정보
        if (MineplanetPlot.instance.getPlotDatabase().getPlotData(x, z) == null) return 2; // 주인이 없는 플롯

        MineplanetPlot.instance.getPlotDatabase().setSkinData(x, z, skin);

        return 0;
    }

    public int updateSkin(int x, int z)
    {
        if (MineplanetPlot.instance.getPlotDatabase().getPlotData(x, z) == null) return 1; // 주인이 없는 플롯
        PlotDatabase plotDatabase = plugin.getPlotDatabase();
        String skin = plotDatabase.getPlotDataEx(x, z).skin;

        updateSkin_extendSide(x, z, skin, plotDatabase);
        updateSkin_side(x, z, skin, plotDatabase);
        updateSkin_edge_top_left(x, z, skin, plotDatabase);
        updateSkin_edge_top_right(x, z, skin, plotDatabase);
        updateSkin_edge_bottom_left(x, z, skin, plotDatabase);
        updateSkin_edge_bottom_right(x, z, skin, plotDatabase);

        return 0;
    }

    private void updateSkin_edge_top_left(int x, int z, String skin, PlotDatabase plotDatabase)
    {
        int y = plugin.getPlotWorld().getHeight() + 17;
        int centerX = x * 44;
        int centerZ = z * 44;

        if (plotDatabase.getIsExtended4(x, z, x + 1, z + 1))
            return;

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            File file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/inner_edge.schem");
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_in = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/outer_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_out = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/extend_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_extend = reader.read();

            boolean top = plotDatabase.getIsExtended(x, z, x, z + 1); // 위쪽
            boolean left = plotDatabase.getIsExtended(x, z, x + 1, z); // 왼쪽

            if (top && left)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_out);
                holder.setTransform(new AffineTransform().rotateY(0));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (!top && !left)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_in);
                holder.setTransform(new AffineTransform().rotateY(0));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (top)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(90));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ + 19))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(0));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateSkin_edge_top_right(int x, int z, String skin, PlotDatabase plotDatabase)
    {
        int y = plugin.getPlotWorld().getHeight() + 17;
        int centerX = x * 44;
        int centerZ = z * 44;

        if (plotDatabase.getIsExtended4(x, z, x - 1, z + 1))
            return;

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            File file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/inner_edge.schem");
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_in = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/outer_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_out = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/extend_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_extend = reader.read();

            boolean top = plotDatabase.getIsExtended(x, z, x, z + 1); // 위쪽
            boolean right = plotDatabase.getIsExtended(x, z, x - 1, z); // 오른쪽

            if (top && right)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_out);
                holder.setTransform(new AffineTransform().rotateY(270));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (!top && !right)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_in);
                holder.setTransform(new AffineTransform().rotateY(270));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (top)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(270));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(0));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 19, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateSkin_edge_bottom_left(int x, int z, String skin, PlotDatabase plotDatabase)
    {
        int y = plugin.getPlotWorld().getHeight() + 17;
        int centerX = x * 44;
        int centerZ = z * 44;

        if (plotDatabase.getIsExtended4(x, z, x + 1, z - 1))
            return;

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            File file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/inner_edge.schem");
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_in = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/outer_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_out = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/extend_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_extend = reader.read();

            boolean bottom = plotDatabase.getIsExtended(x, z, x, z - 1); // 아래쪽
            boolean left = plotDatabase.getIsExtended(x, z, x + 1, z); // 왼쪽

            if (bottom && left)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_out);
                holder.setTransform(new AffineTransform().rotateY(90));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (!bottom && !left)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_in);
                holder.setTransform(new AffineTransform().rotateY(90));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (bottom)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(90));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(180));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 19, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateSkin_edge_bottom_right(int x, int z, String skin, PlotDatabase plotDatabase)
    {
        int y = plugin.getPlotWorld().getHeight() + 17;
        int centerX = x * 44;
        int centerZ = z * 44;

        if (plotDatabase.getIsExtended4(x, z, x - 1, z - 1))
            return;

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            File file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/inner_edge.schem");
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_in = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/outer_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_out = reader.read();

            file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/extend_edge.schem");
            format = ClipboardFormats.findByFile(file);
            reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard_extend = reader.read();

            boolean bottom = plotDatabase.getIsExtended(x, z, x, z - 1); // 아래쪽
            boolean right = plotDatabase.getIsExtended(x, z, x - 1, z); // 오른쪽

            if (bottom && right)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_out);
                holder.setTransform(new AffineTransform().rotateY(180));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (!bottom && !right)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_in);
                holder.setTransform(new AffineTransform().rotateY(180));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else if (bottom)
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(270));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ - 19))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            else
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard_extend);
                holder.setTransform(new AffineTransform().rotateY(180));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateSkin_extendSide(int x, int z, String skin, PlotDatabase plotDatabase)
    {
        int y = plugin.getPlotWorld().getHeight() + 17;
        int centerX = x * 44;
        int centerZ = z * 44;

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            File file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/extend_side.schem");

            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();

            if (plotDatabase.getIsExtended(x, z, x - 1, z)) // 오른쪽
            {
                if (!plotDatabase.getIsExtended4(x, z, x - 1, z + 1)) // 위
                {
                    ClipboardHolder holder = new ClipboardHolder(clipboard);
                    holder.setTransform(new AffineTransform().rotateY(0));

                    Operation operation = holder
                            .createPaste(editSession)
                            .to(BlockVector3.at(centerX - 24, y, centerZ + 13))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(operation);
                }
                if (!plotDatabase.getIsExtended4(x, z, x - 1, z - 1)) // 아래
                {
                    ClipboardHolder holder = new ClipboardHolder(clipboard);
                    holder.setTransform(new AffineTransform().rotateY(180));

                    Operation operation = holder
                            .createPaste(editSession)
                            .to(BlockVector3.at(centerX - 20, y, centerZ - 13))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(operation);
                }
            }
            if (plotDatabase.getIsExtended(x, z, x, z + 1)) // 위쪽
            {
                if (!plotDatabase.getIsExtended4(x, z, x + 1, z + 1)) // 왼쪽
                {
                    ClipboardHolder holder = new ClipboardHolder(clipboard);
                    holder.setTransform(new AffineTransform().rotateY(90));

                    Operation operation = holder
                            .createPaste(editSession)
                            .to(BlockVector3.at(centerX + 13, y, centerZ + 24))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(operation);
                }
                if (!plotDatabase.getIsExtended4(x, z, x - 1, z + 1)) // 오른쪽
                {
                    ClipboardHolder holder = new ClipboardHolder(clipboard);
                    holder.setTransform(new AffineTransform().rotateY(270));

                    Operation operation = holder
                            .createPaste(editSession)
                            .to(BlockVector3.at(centerX - 13, y, centerZ + 20))
                            .ignoreAirBlocks(false)
                            .build();
                    Operations.complete(operation);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateSkin_side(int x, int z, String skin, PlotDatabase plotDatabase)
    {
        int y = plugin.getPlotWorld().getHeight() + 17;
        int centerX = x * 44;
        int centerZ = z * 44;

        try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(plugin.getPlotWorld().getWorld()), -1)) {
            File file = new File(plugin.getDataFolder().getPath() + "/skins/" + skin + "/side.schem");

            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();

            if (!plotDatabase.getIsExtended(x, z, x, z + 1)) // 위
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                holder.setTransform(new AffineTransform().rotateY(0));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            if (!plotDatabase.getIsExtended(x, z, x - 1, z)) // 오른쪽
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                holder.setTransform(new AffineTransform().rotateY(270));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX - 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            if (!plotDatabase.getIsExtended(x, z, x, z - 1)) // 아래
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                holder.setTransform(new AffineTransform().rotateY(180));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ - 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
            if (!plotDatabase.getIsExtended(x, z, x + 1, z)) // 왼쪽
            {
                ClipboardHolder holder = new ClipboardHolder(clipboard);
                holder.setTransform(new AffineTransform().rotateY(90));

                Operation operation = holder
                        .createPaste(editSession)
                        .to(BlockVector3.at(centerX + 13, y, centerZ + 13))
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
