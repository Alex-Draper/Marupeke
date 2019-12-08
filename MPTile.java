/** Represents a tile on the board of the puzzle
 * @author Alex Draper
 * @version 1.5
 */

public class MPTile
{
    // The Mark assigned to the Tile
    private Mark mark = Mark.BLANK;

    // Define whether the tile mark can be edited by the user
    private boolean editable = true;

    //Public Methods

    /**
     * Returns the Mark of the tile
     * @return the mark of the tile
     */
    public Mark getMark()
    {
        return this.mark;
    }

    /**
     * Sets the Mark of the tile to the supplied mark if the tile is editable
     * @param mark The mark to change the tile too
     */
    public void setMark(Mark mark)
    {
        if(isEditable())
        {
            this.mark = mark;
        }
    }

    /**
     * Returns whether the the Tile is editable by the user or not
     * @return True is editable by user, false if not
     */
    public boolean isEditable()
    {
        return this.editable;
    }

    /**
     * Set whether the tile is editable by the user or not
     * @param editableState The editable state to change the tile too
     */
    public void setEditable(boolean editableState)
    {
        this.editable = editableState;
    }

    /**
     * Print out the String representation of the Tile, defined by the Mark of the Tile
     * @return The String representation of the Tile
     */
    @Override
    public String toString()
    {
        return this.getMark().toString();
    }

}
