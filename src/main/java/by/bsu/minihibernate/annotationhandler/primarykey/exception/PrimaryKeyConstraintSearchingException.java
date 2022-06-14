package by.bsu.minihibernate.annotationhandler.primarykey.exception;

public final class PrimaryKeyConstraintSearchingException extends PrimaryKeyAnnotationHandlingException
{
    public PrimaryKeyConstraintSearchingException()
    {
        super();
    }

    public PrimaryKeyConstraintSearchingException(final String description)
    {
        super(description);
    }

    public PrimaryKeyConstraintSearchingException(final Exception cause)
    {
        super(cause);
    }

    public PrimaryKeyConstraintSearchingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
