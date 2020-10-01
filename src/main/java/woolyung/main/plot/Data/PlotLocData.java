package woolyung.main.plot.Data;

import woolyung.main.plot.PlotWorld;

public class PlotLocData
{
    public enum PLOT_SECTION
    {
        PLOT,
        INNER_LINE, OUTER_LINE,
        ROAD_TOP, ROAD_LEFT, ROAD_EDGE,
        SKIN
    }

    public int plotLocX;
    public int plotLocZ;
    public int plotInnerLocX;
    public int plotInnerLocZ;
    public PLOT_SECTION plotSection;
}