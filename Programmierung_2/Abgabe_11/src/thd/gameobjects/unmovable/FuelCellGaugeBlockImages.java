package thd.gameobjects.unmovable;

class FuelCellGaugeBlockImages {
    static final int MAX_NUM_FUEL_CELLS = 19;
    static final String[] FUEL_CELL = {
            "YY          ",
            "YYYY        ",
            "YYYYYY      ",
            "YYYYYYYY    ",
            "YYYYYYYYYY  ",
            "YYYYYYYYYYYY",
            "YYYYYYYYYY  ",
            "YYYYYYYY    ",
            "YYYYY       ",
            "YY          "
    };
    static final int WIDTH = FUEL_CELL[FUEL_CELL.length / 2].length();
    static final int HEIGHT = FUEL_CELL.length;
}