package woolyung.main.plot.Data;

public class PlotLocData
{
    public enum PLOT_SECTION
    {
        PLOT,
        INNER_LINE, OUTER_LINE,
        ROAD_TOP, ROAD_LEFT, ROAD_EDGE,
        SKIN
    }

    public enum EXTEND_SECTION
    {
        CENTER, BOTTOM, TOP, LEFT, RIGHT, LEFT_BOTTOM, LEFT_TOP, RIGHT_TOP, RIGHT_BOTTOM
    }

    public int plotLocX;
    public int plotLocZ;
    public int plotInnerLocX;
    public int plotInnerLocZ;
    public PLOT_SECTION plotSection;
    public EXTEND_SECTION extendSection;
}