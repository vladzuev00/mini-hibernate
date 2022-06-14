package by.bsu.minihibernate.annotationhandlingrunner.exception;

public final class AnnotationHandlingRunningException extends Exception
{
    public AnnotationHandlingRunningException()
    {
        super();
    }

    public AnnotationHandlingRunningException(final String description)
    {
        super(description);
    }

    public AnnotationHandlingRunningException(final Exception cause)
    {
        super(cause);
    }

    public AnnotationHandlingRunningException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
