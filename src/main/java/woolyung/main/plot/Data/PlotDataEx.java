package woolyung.main.plot.Data;

import java.util.ArrayList;

public class PlotDataEx extends PlotData
{
    public String owner;
    public ArrayList<String> helpers = new ArrayList<>();
    public ArrayList<String> denies = new ArrayList<>();
    public int plotSize;

    // 설정들
    public String skin;
    public String biome;
    public boolean pvp;
    public boolean click;
    public boolean blockClick;
    public boolean itemClear;
}