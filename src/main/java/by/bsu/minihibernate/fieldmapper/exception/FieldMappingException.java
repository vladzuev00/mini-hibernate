package by.bsu.minihibernate.fieldmapper.exception;

public final class FieldMappingException extends Exception
{
    public FieldMappingException()
    {
        super();
    }

    public FieldMappingException(final String description)
    {
        super(description);
    }

    public FieldMappingException(final Exception cause)
    {
        super(cause);
    }

    public FieldMappingException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
