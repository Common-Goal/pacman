import java.util.LinkedList;

/*
Represents a snake in the snake game
 */
public class Snake {
    private static int size;    // the number of square occupied by the snake

    private LinkedList<Location> sizeData;  // holds the data of the snake

    public LinkedList<Location> getSizeData() {
        return sizeData;
    }


    /*constructor the initializes the snake object
     @param row, row of the starting point of the snake
     @param col, col of the starting point of the snake
     */
    public Snake(int row, int col) {
        sizeData = new LinkedList<Location>();
        sizeData.add(new Location(row, col));
        sizeData.add(new Location(row, col + 1));
        size = 2;

    }

    public int getSize() {
        return sizeData.size();

    }

    public void addSize() {
        Location temp = sizeData.getLast();
        int newX = temp.getCol();
        int newY = temp.getRow();
        sizeData.addLast(new Location(newY, newX));
    }
}
