package by.bsu.minihibernate.annotationhandler.entity.exception;

import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;

public final class EntityAnnotationHandlingException extends AnnotationHandlingException
{
    public EntityAnnotationHandlingException()
    {
        super();
    }

    public EntityAnnotationHandlingException(final String description)
    {
        super(description);
    }

    public EntityAnnotationHandlingException(final Exception cause)
    {
        super(cause);
    }

    public EntityAnnotationHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
