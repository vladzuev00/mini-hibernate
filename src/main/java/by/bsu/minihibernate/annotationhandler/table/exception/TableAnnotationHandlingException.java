package by.bsu.minihibernate.annotationhandler.table.exception;

import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;

public final class TableAnnotationHandlingException extends AnnotationHandlingException
{
    public TableAnnotationHandlingException()
    {
        super();
    }

    public TableAnnotationHandlingException(final String description)
    {
        super(description);
    }

    public TableAnnotationHandlingException(final Exception cause)
    {
        super(cause);
    }

    public TableAnnotationHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
