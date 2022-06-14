package by.bsu.minihibernate.columnmodifierbynecessary.exception;

public class ColumnModifyingException extends Exception
{
    public ColumnModifyingException()
    {
        super();
    }

    public ColumnModifyingException(final String description)
    {
        super(description);
    }

    public ColumnModifyingException(final Exception cause)
    {
        super(cause);
    }

    public ColumnModifyingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
