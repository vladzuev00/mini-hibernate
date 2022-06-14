package by.bsu.minihibernate.annotationhandler.column.exception;

import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;

public final class ColumnAnnotationHandlingException extends AnnotationHandlingException
{
    public ColumnAnnotationHandlingException()
    {
        super();
    }

    public ColumnAnnotationHandlingException(final String description)
    {
        super(description);
    }

    public ColumnAnnotationHandlingException(final Exception cause)
    {
        super(cause);
    }

    public ColumnAnnotationHandlingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}

