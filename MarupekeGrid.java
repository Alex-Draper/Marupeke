
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/** Represents the board of the Marupeke Puzzle
 * @author Alex Draper
 * @version 1.5
 */
public class MarupekeGrid {

    final int gridSize;
    final MPTile grid[][];

    int blankTiles;


    /**
     * Constructor to initialise new grid with supplied size
     * @param size Int to specify the dimensions of the new puzzle (height and width always the same)
     */
    public MarupekeGrid(int size)
    {
        this.gridSize = size;
        grid = new MPTile[this.gridSize][this.gridSize];

        // Set each index in the array to a fresh instance of MPTile to prevent NULL Errors
        for(int row=0; row<this.gridSize; row++)
        {
            for(int column = 0; column < this.gridSize; column++)
            {
                grid[row][column] = new MPTile();
            }
        }
    }

    //public methods

    /** Return the Tile at the specific index of the grid
     *
     * @param row The row of the grid to get
     * @param column The column of the grid to get
     * @return The Tile object at the index specified by row and column
     * @throws ArrayIndexOutOfBoundsException
     */
    public MPTile getTile(int row, int column) throws ArrayIndexOutOfBoundsException
    {
        return grid[row][column];
    }

    /**
     * Sets the tile at the specified index to the supplied mark if the tile is editable
     * @param row The row index of the tile on the grid
     * @param column The column index of the tile on the grid
     * @param mark The mark to update the tile to
     * @return True if the update was successful, false if not (not editable or index does not exist)
     */
    public boolean userMarkRequest(int row, int column, Mark mark)
    {
        MPTile tile;

        try {
             tile = getTile(row, column);
        }
        catch(ArrayIndexOutOfBoundsException boundsException)
        {
            return false;
        }


        if(!tile.isEditable())
        {
            return false;
        }

        switch (mark)
        {
            case BLANK:
                unmarkTile(tile);
                return true;

            case CROSS:
                markTileCross(tile);
                return true;

            case NOUGHT:
                markTileNought(tile);
                return true;

            default:
                return false;
        }

    }

    /**
     * Sets a tile on the board to Blank mark
     * @param tile The tile to update
     */
    private void unmarkTile(MPTile tile)
    {
        setGrid(tile, Mark.BLANK);
    }

    /**
     * Sets a tile on the board to Cross mark
     * @param tile The tile to update
     */
    private void markTileCross(MPTile tile)
    {
        setGrid(tile, Mark.CROSS);
    }

    /**
     * Sets a tile on the board to Nought mark
     * @param tile The tile to update
     */
    private void markTileNought(MPTile tile)
    {
        setGrid(tile, Mark.NOUGHT);
    }

    /**
     * Updates the supplied tile with a new mark
     * @param tile The tile to update
     * @param mark The mark to update the tile to
     */
    private void setGrid(MPTile tile, Mark mark)
    {
        tile.setMark(mark);
    }

    /**
     * Updates a tile at the specified index on the grid with the supplied mark
     * @param row The row index of the tile
     * @param column The column index of the tile
     * @param mark The mark to update the tile to
     * @return True if update was successful, false if index does not exist in grid
     */
    private boolean setGrid(int row, int column, Mark mark) {
        try
        {
            MPTile tile = getTile(row, column);
            tile.setMark(mark);
            return true;
        }
        catch (ArrayIndexOutOfBoundsException boundsException)
        {
            return false;
        }
    }

    /**
     * Update a tile with a new mark and set a new editable state for the tile
     * @param row The row index of the tile
     * @param column The column index of the tile
     * @param newEditableState The new editable state for the tile
     * @param mark The mark to update the tile to
     * @return True if update successful, false if index out of bounds
     */
    private boolean setGrid(int row, int column, boolean newEditableState, Mark mark) {
        try {
            MPTile tile = getTile(row, column);
            tile.setMark(mark);
            tile.setEditable(newEditableState);
            return true;
        }
        catch(ArrayIndexOutOfBoundsException boundsException)
        {
            return false;
        }
    }

    /**
     * Return string representation of the puzzle grid
     * @return the string representation of the grid
     */
    @Override
    public String toString()
    {
        String boardString = "";

        for(int row = 0; row < gridSize; row++)
        {
            for(int column = 0; column < gridSize; column++)
            {
                boardString += getTile(row, column).toString();
            }

            boardString += "\n";
        }

        return boardString;

    }

    /**
     * Indicates whether the puzzle contains tile placement that does not conform to the rules of the puzzle
     * @return True if puzzle is complete and legal, false if not
     */
    public boolean isLegal()
    {
        return illegalities().size() == 0;
    }

    /**
     * method checks the grid for tiles with blank marks and for consecutive tiles with the same mark
     * @return a list of tuples/pairs that contain row/column index of problem tiles
     */
    public ArrayList<Tuple> illegalities()
    {
        ArrayList<Tuple> illegalTiles = new ArrayList<>();
        Tuple problemTile;
        blankTiles = 0;

        for(int row = 0; row < gridSize; row++)
        {
            for(int column = 0; column < gridSize; column++)
            {
                MPTile tile = getTile(row, column);

                if(solidTileCheck(tile))
                {
                    continue;
                }

                if(blankTileCheck(tile)) {
                    blankTiles++;
                    continue;
                }


                if(     horizontalCheck(row, column) ||
                        verticalCheck(row, column)   ||
                        diagonalCheck(row, column)   )
                {
                    //print("prob: " + row + " " + column);
                    problemTile = new Tuple(row, column);
                    illegalTiles.add(problemTile);
                }

            }
        }
        return illegalTiles;
    }

    /**
     * Checks if supplied tile is marked as BLANK
     * @param tile The tile to check
     * @return True if the tile is marked blank, false if not
     */
    private boolean blankTileCheck(MPTile tile) {
        return tile.getMark().equals(Mark.BLANK);
    }

    /**
     * Checks if the supplied tile is marked as Solid
     * @param tile the tile to check
     * @return true is marked as solid, false is not
     */
    private boolean solidTileCheck(MPTile tile)
    {
        return tile.getMark().equals(Mark.SOLID);
    }

    /**
     * Call the tileCheck method with horizontal offsets for 1 tile look-around
     * @param row The row index of the tile
     * @param column The column index of the tile
     * @return True if all Tiles have the same mark, false if not
     */
    private boolean horizontalCheck(int row, int column)
    {
        //offsets left = row + 0, column + -1
        //       right = row + 0, column + 1

        return tileCheck(row, column, 0, -1, 0, 1);


    }

    /**
     * Call the tileCheck method with vertical offsets for 1 tile look-around
     * @param row The row index of the tile
     * @param column The column index of the tile
     * @return True if all Tiles have the same mark, false if not
     */
    private boolean verticalCheck(int row, int column)
    {
        //offsets up = row + -1, column + 0
        //      down = row + 1, column + 0


        return tileCheck(row, column, -1, 0, 1, 0);
    }

    /**
     * Call the tileCheck method with diagonal offsets for 1 tile look-around
     * @param row The row index of the tile
     * @param column The column index of the tile
     * @return True if tiles for one or more offsets have the same mark, false if not
     */
    private boolean diagonalCheck(int row, int column)
    {
        //offsets diagonal TopLeft = row + -1, column + -1
        //             bottomRight = row + 1, column + 1
        //
        //
        //                topRight = row + -1, column + 1
        //             bottomLeft = row + 1, column + -1


        return (tileCheck(row, column, -1, -1, 1, 1) ||
                tileCheck(row, column, -1, 1, 1, -1));

    }

    /**
     * Will check the whether the mark of the tile at supplied index and the tiles
     * from the provided offsets are the same
     * @param row the row index of the tile
     * @param column the column index of the tile
     * @param rowOffset the first row offset index
     * @param columnOffset the first column offset index
     * @param rowOffset2 the second row offset index
     * @param columnOffset2 the second column offset index
     * @return true if the mark of all tiles are the same, false if not
     */
    private boolean tileCheck(int row, int column,
                             int rowOffset, int columnOffset,
                             int rowOffset2, int columnOffset2)
    {

        MPTile tile;
        MPTile firstTile;
        MPTile secondTile;

        //remember that adding a minus value will have the effect of subtraction
        try {
            tile = getTile(row, column);
            firstTile = getTile(row + rowOffset, column + columnOffset);
            secondTile = getTile(row + rowOffset2, column + columnOffset2);
        }
        catch (ArrayIndexOutOfBoundsException boundsException)
        {
            //if a tile does not have a tile within the supplied offsets then we don't need to keep
            //checking if its legal
            return false;
        }

        //return true if the mark of firstTile and secondTile are equal mark of tile
        //else false

        return firstTile.getMark().equals(tile.getMark()) &&
                secondTile.getMark().equals(tile.getMark());

    }

    /**
     * Returns whether the puzzle is complete or not, a legal puzzle with no blank tiles is complete
     * @return True if complete, false if not
     */
    public boolean isPuzzleComplete()
    {
        return isLegal() && blankTiles == 0;
    }

    /**
     * Get the puzzle grid
     * @return The 2d array that represents the puzzle grid
     */
    public MPTile[][] getGrid() {
        return grid;
    }

    /**
     * Returns the size of the puzzle grid
     * @return the size of the puzzle grid
     */
    public int getSize() {
        return gridSize;
    }

    /**
     * Set all tiles on the grid to blank
     */
    public void clearGrid() {
        for(int row=0; row<getSize(); row++) {
            for(int column=0; column<getSize(); column++) {
                setGrid(row, column, Mark.BLANK);
            }
        }
    }

    /**
     * Factory method that can generate a random legal puzzle according to the supplied parameters,
     * the sum of numX and numO must not exceed half the number of grid tiles
     * @param size The size of the grid
     * @param numFill The amount of solid tiles on the starting grid
     * @param numX The amount of solid tiles on the starting grid
     * @param numO The amount of solid tiles on the starting grid
     * @return The randomly generated puzzle if sum of numX and numO below half the number of grid tiles,
     * null otherwise
     */
    public static MarupekeGrid randomPuzzle(int size,
                                            int numFill,
                                            int numX,
                                            int numO) {

        if (numX + numO > size * size / 2) {
            return null;
        }
        MarupekeGrid mp = new MarupekeGrid(size);
        Random rand = new Random();

        //randomly fill some squares with solids
        int countSolid = 0;
        while (countSolid < numFill) {
            if (mp.setGrid(rand.nextInt(size), rand.nextInt(size), false, Mark.SOLID)) {
                countSolid++;
            }
        }

        int countX = 0;
        int countO = 0;
        while(countX < numX || countO < numO) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if(countX < numX && mp.setGrid(x,y,true, Mark.CROSS)) {
                if(mp.isLegal()) {
                    mp.setGrid(x, y, false, Mark.CROSS);
                    countX++;
                } else {
                    mp.setGrid(x, y, Mark.BLANK);
                }

            }
            if(countO<numO && mp.setGrid(x,y,true, Mark.NOUGHT)) {
                if(mp.isLegal()) {
                    mp.setGrid(x, y, false, Mark.NOUGHT);
                    countO++;
                } else {
                    mp.setGrid(x, y, Mark.BLANK);
                }

            }
        }
        return mp;


    }

}
