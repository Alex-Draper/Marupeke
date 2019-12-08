/** Represents the possible marks for a tile on the puzzle
 * @author Alex Draper
 * @version 1.5
 */
public enum Mark
{

    /**
     * represents that no CROSS or NOUGHT has been set on the tile
     */
    BLANK("_"),

    /**
     * Represents a tile that will not contain a value in the puzzle
     */
    SOLID("#"),

    /**
     * Represents an "x" on the board
     */
    CROSS("x"),

    /**
     * Represents an "o" on the board
     */
    NOUGHT("o");

    private String stringRepresentation;

    /**
     * Set string representation for a Mark
     * @param stringRep String to represent a Mark
     */
    private Mark(String stringRep)
    {
        this.stringRepresentation = stringRep;
    }

    /**
     * Return the string representation of a Mark
     * @return String representation of the mark
     */
    public String toString()
    {
        return stringRepresentation;
    }
}