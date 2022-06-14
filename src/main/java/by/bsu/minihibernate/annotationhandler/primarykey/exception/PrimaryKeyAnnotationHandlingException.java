package by.bsu.minihibernate.annotationhandler.primarykey.exception;

import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;

public class PrimaryKeyAnnotationHandlingException extends AnnotationHandlingException
{
    public PrimaryKeyAnnotationHandlingException()
    {
        super();
    }

    public PrimaryKeyAnnotationHandlingException(final String description)
    {
        super(description);
    }

    public PrimaryKeyAnnotationHandlingException(final Exception cause)
    {
        super(cause);
    }

    public PrimaryKeyAnnotationHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
