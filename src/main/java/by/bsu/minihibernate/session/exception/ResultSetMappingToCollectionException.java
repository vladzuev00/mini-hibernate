package by.bsu.minihibernate.session.exception;

import by.bsu.minihibernate.exception.FrameworkException;

public final class ResultSetMappingToCollectionException extends FrameworkException
{
    public ResultSetMappingToCollectionException()
    {
        super();
    }

    public ResultSetMappingToCollectionException(final String description)
    {
        super(description);
    }

    public ResultSetMappingToCollectionException(final Exception cause)
    {
        super(cause);
    }

    public ResultSetMappingToCollectionException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
