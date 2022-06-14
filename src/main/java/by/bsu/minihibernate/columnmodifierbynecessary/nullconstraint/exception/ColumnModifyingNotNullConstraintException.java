package by.bsu.minihibernate.columnmodifierbynecessary.nullconstraint.exception;

import by.bsu.minihibernate.columnmodifierbynecessary.exception.ColumnModifyingException;

public final class ColumnModifyingNotNullConstraintException extends ColumnModifyingException
{
    public ColumnModifyingNotNullConstraintException()
    {
        super();
    }

    public ColumnModifyingNotNullConstraintException(final String description)
    {
        super(description);
    }

    public ColumnModifyingNotNullConstraintException(final Exception cause)
    {
        super(cause);
    }

    public ColumnModifyingNotNullConstraintException(final String description, final Exception cause)
    {
        super(description, cause);
    }
}
