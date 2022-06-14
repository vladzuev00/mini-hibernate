package by.bsu.minihibernate.columnmodifierbynecessary.type.exception;

import by.bsu.minihibernate.columnmodifierbynecessary.exception.ColumnModifyingException;

public final class ColumnModifyingTypeException extends ColumnModifyingException
{
    public ColumnModifyingTypeException()
    {
        super();
    }

    public ColumnModifyingTypeException(final String description)
    {
        super(description);
    }

    public ColumnModifyingTypeException(final Exception cause)
    {
        super(cause);
    }

    public ColumnModifyingTypeException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
