package by.bsu.minihibernate.annotationhandler.autoincrementprimarykey.exception;

import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;

public final class AutoIncrementPrimaryKeyAnnotationHandlingException extends AnnotationHandlingException
{
    public AutoIncrementPrimaryKeyAnnotationHandlingException()
    {
        super();
    }

    public AutoIncrementPrimaryKeyAnnotationHandlingException(final String description)
    {
        super(description);
    }

    public AutoIncrementPrimaryKeyAnnotationHandlingException(final Exception cause)
    {
        super(cause);
    }

    public AutoIncrementPrimaryKeyAnnotationHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
