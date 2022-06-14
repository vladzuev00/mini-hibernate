package by.bsu.minihibernate.annotationhandler.onetoone.exception;

import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;

public final class OneToOneAnnotationHandlingException extends AnnotationHandlingException
{
    public OneToOneAnnotationHandlingException()
    {
        super();
    }

    public OneToOneAnnotationHandlingException(final String description)
    {
        super(description);
    }

    public OneToOneAnnotationHandlingException(final Exception cause)
    {
        super(cause);
    }

    public OneToOneAnnotationHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
