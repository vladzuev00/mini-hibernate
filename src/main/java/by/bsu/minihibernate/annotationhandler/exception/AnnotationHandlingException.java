package by.bsu.minihibernate.annotationhandler.exception;

import by.bsu.minihibernate.exception.FrameworkException;

public class AnnotationHandlingException extends FrameworkException
{
    public AnnotationHandlingException()
    {
        super();
    }

    public AnnotationHandlingException(final String description)
    {
        super(description);
    }

    public AnnotationHandlingException(final Exception cause)
    {
        super(cause);
    }

    public AnnotationHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
