/** Very simple representation of a pair, this should really implement the tuple interface.
 *
 */
public class Tuple<T> {

    T first;
    T second;

    /**
     * Constuct the pair
     * @param first the first element
     * @param second the second element
     */
    public Tuple(T first, T second)
    {
        this.first = first;
        this.second = second;
    }

    /**
     * Set the first element
     * @param first the value to set the first element to
     */
    public void setFirst(T first)
    {
        this.first = first;
    }

    /**
     * Set the second element
     * @param second the value to set the second element to
     */
    public void setSecond(T second)
    {
        this.second = second;
    }

    /**
     * Get the first element of the pair
     * @return the first element of the pair
     */
    public T getFirst()
    {
        return first;
    }

    /**
     * Get the second element of the pair
     * @return the second element of the pair
     */
    public T getSecond()
    {
        return second;
    }

}
