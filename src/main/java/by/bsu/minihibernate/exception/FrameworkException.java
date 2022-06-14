package by.bsu.minihibernate.exception;

public class FrameworkException extends Exception
{
    public FrameworkException()
    {
        super();
    }

    public FrameworkException(final String description)
    {
        super(description);
    }

    public FrameworkException(final Exception cause)
    {
        super(cause);
    }

    public FrameworkException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
