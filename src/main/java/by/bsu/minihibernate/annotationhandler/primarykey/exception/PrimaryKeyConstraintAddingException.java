package by.bsu.minihibernate.annotationhandler.primarykey.exception;

public final class PrimaryKeyConstraintAddingException extends PrimaryKeyAnnotationHandlingException
{
    public PrimaryKeyConstraintAddingException()
    {
        super();
    }

    public PrimaryKeyConstraintAddingException(final String description)
    {
        super(description);
    }

    public PrimaryKeyConstraintAddingException(final Exception cause)
    {
        super(cause);
    }

    public PrimaryKeyConstraintAddingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
