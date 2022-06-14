package by.bsu.minihibernate.accessmethodextractor.exception;

import by.bsu.minihibernate.exception.FrameworkException;

public final class ExtractionAccessMethodException extends FrameworkException
{
    public ExtractionAccessMethodException()
    {
        super();
    }

    public ExtractionAccessMethodException(final String description)
    {
        super(description);
    }

    public ExtractionAccessMethodException(final Exception cause)
    {
        super(cause);
    }

    public ExtractionAccessMethodException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
