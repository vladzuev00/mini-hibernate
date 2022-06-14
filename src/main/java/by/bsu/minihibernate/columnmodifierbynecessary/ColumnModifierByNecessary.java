package by.bsu.minihibernate.columnmodifierbynecessary;

import by.bsu.minihibernate.columnmodifierbynecessary.exception.ColumnModifyingException;

import java.lang.reflect.Field;
import java.sql.Connection;

@FunctionalInterface
public interface ColumnModifierByNecessary
{
    public abstract void modifyIfNecessary(final Field associatedField, final Connection connectionToDataBase)
            throws ColumnModifyingException;
}

