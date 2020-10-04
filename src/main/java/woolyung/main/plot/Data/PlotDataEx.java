package woolyung.main.plot.Data;

import java.util.ArrayList;

public class PlotDataEx extends PlotData
{
    public String owner;
    public ArrayList<String> helpers = new ArrayList<>();
    public ArrayList<String> denies = new ArrayList<>();
    public int plotSize;

    // 설정들
    public int skin1;
    public int skin2;
    public int skin3;
    public String biome;
    public boolean pvp;
    public boolean click;
    public boolean blockClick;
    public boolean itemClear;
}