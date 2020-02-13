public class Location {
    private int row;
    private int col;

    Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean equals(Location l) {
        return (row == l.row && col == l.col);
    }
}
