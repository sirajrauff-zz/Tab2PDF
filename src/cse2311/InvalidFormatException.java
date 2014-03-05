package cse2311;

public class InvalidFormatException extends Exception{
	
	/**
     * Creates a new instance of <code>InvalidFormatException</code> without
     * detail message.
     */
    public InvalidFormatException() {
    }

    /**
     * Constructs an instance of <code>InvalidFormatException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidFormatException(String msg) {
        super(msg);
    }
}
