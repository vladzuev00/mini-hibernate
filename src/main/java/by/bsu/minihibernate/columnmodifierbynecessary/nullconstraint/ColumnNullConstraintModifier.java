package by.bsu.minihibernate.columnmodifierbynecessary.nullconstraint;

import by.bsu.minihibernate.columnmodifierbynecessary.ColumnModifierByNecessary;
import by.bsu.minihibernate.columnmodifierbynecessary.nullconstraint.exception.ColumnModifyingNotNullConstraintException;
import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;
import by.bsu.minihibernate.fieldsofclassfounder.FieldsOfClassFounder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Set;

public final class ColumnNullConstraintModifier implements ColumnModifierByNecessary
{
    private final FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;
    private final FieldsOfClassFounder fieldsOfClassFounder;

    public static ColumnNullConstraintModifier createColumnNullConstraintModifier()
    {
        if(ColumnNullConstraintModifier.columnNullConstraintModifier == null)
        {
            synchronized(ColumnNullConstraintModifier.class)
            {
                if(ColumnNullConstraintModifier.columnNullConstraintModifier == null)
                {
                    ColumnNullConstraintModifier.columnNullConstraintModifier = new ColumnNullConstraintModifier();
                }
            }
        }
        return ColumnNullConstraintModifier.columnNullConstraintModifier;
    }

    private static ColumnNullConstraintModifier columnNullConstraintModifier = null;

    private ColumnNullConstraintModifier()
    {
        super();
        this.fieldAssociatedPropertiesIdentifier
                = FieldAssociatedPropertiesIdentifier.createFieldAssociatedPropertiesIdentifier();
        this.fieldsOfClassFounder = FieldsOfClassFounder.createFieldsOfClassFounder();
    }

    @Override
    public final void modifyIfNecessary(final Field associatedField, final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        if(!this.fieldAssociatedPropertiesIdentifier.isFieldAssociatedWithPrimaryKey(associatedField))
        {
            if(this.fieldAssociatedPropertiesIdentifier.isSimpleField(associatedField)
                    && !this.isNullableConstraintOfSimpleFieldCorresponding(associatedField, connectionToDataBase))
            {
                this.changeNullableConstraintOfColumnAssociatedWithSimpleField(associatedField, connectionToDataBase);
            }
            else if(this.fieldAssociatedPropertiesIdentifier.isCompositeField(associatedField))
            {
                this.changeNullableConstraintsOfColumnsAssociatedWithFieldsOfCompositeFieldByNecessary(
                        associatedField, connectionToDataBase);
            }
        }
    }

    private boolean isNullableConstraintOfSimpleFieldCorresponding(final Field associatedField,
                                                                   final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(associatedField);
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                associatedField);
        final boolean isNullableInAnnotation = this.fieldAssociatedPropertiesIdentifier
                .isAssociatedWithNullableColumn(associatedField);
        final boolean isNullableInInformationSchema = this.isNullableColumnInInformationSchema(
                nameOfTable, nameOfColumn, connectionToDataBase);
        return isNullableInAnnotation == isNullableInInformationSchema;
    }

    private boolean isNullableColumnInInformationSchema(final String nameOfTable, final String nameOfColumn,
                                                        final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        try(final PreparedStatement preparedStatement = connectionToDataBase.prepareStatement(
                ColumnNullConstraintModifier.SQL_QUERY_OF_PREPARED_STATEMENT_TO_SELECT_NULLABLE_CONSTRAINT_OF_COLUMN))
        {
            preparedStatement.setString(
                    ColumnNullConstraintModifier.PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT, nameOfTable);
            preparedStatement.setString(
                    ColumnNullConstraintModifier.PARAMETER_INDEX_OF_NAME_OF_COLUMN_IN_PREPARED_STATEMENT, nameOfColumn);
            final ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            final String valueOfActivatedNullableConstraint = resultSet.getString(
                    ColumnNullConstraintModifier.NAME_OF_COLUMN_OF_NULLABLE_CONSTRAINT);
            return valueOfActivatedNullableConstraint.equals(
                    ColumnNullConstraintModifier.VALUE_OF_ACTIVATED_NULLABLE_CONSTRAINT);
        }
        catch(final SQLException cause)
        {
            throw new ColumnModifyingNotNullConstraintException(cause);
        }
    }

    private static final String SQL_QUERY_OF_PREPARED_STATEMENT_TO_SELECT_NULLABLE_CONSTRAINT_OF_COLUMN
            = "SELECT information_schema.columns."
            + ColumnNullConstraintModifier.NAME_OF_COLUMN_OF_NULLABLE_CONSTRAINT
            + " FROM information_schema.columns "
            + "WHERE information_schema.columns.table_name = ? AND information_schema.columns.column_name = ?";

    private static final int PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT = 1;
    private static final int PARAMETER_INDEX_OF_NAME_OF_COLUMN_IN_PREPARED_STATEMENT = 2;

    private static final String NAME_OF_COLUMN_OF_NULLABLE_CONSTRAINT = "is_nullable";

    private static final String VALUE_OF_ACTIVATED_NULLABLE_CONSTRAINT = "YES";

    private void changeNullableConstraintOfColumnAssociatedWithSimpleField(final Field associatedField,
                                                                           final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        final boolean shouldAddNotNullConstraintToColumn = !this.fieldAssociatedPropertiesIdentifier
                .isAssociatedWithNullableColumn(associatedField);
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(associatedField);
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                associatedField);
        if(shouldAddNotNullConstraintToColumn)
        {
            this.addNotNullConstraintToColumn(nameOfTable, nameOfColumn, connectionToDataBase);
        }
        else
        {
            this.dropNotNullConstraintOfColumn(nameOfTable, nameOfColumn, connectionToDataBase);
        }
    }

    private void addNotNullConstraintToColumn(final String nameOfTable, final String nameOfColumn,
                                              final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        try
        {
            final String queryToAddNotNullConstraintToColumn = String.format(
                    ColumnNullConstraintModifier.TEMPLATE_OF_SQL_QUERY_TO_ADD_NOT_NULL_CONSTRAINT_TO_COLUMN,
                    nameOfTable, nameOfColumn);
            try(final Statement statement = connectionToDataBase.createStatement())
            {
                statement.executeUpdate(queryToAddNotNullConstraintToColumn);
            }
        }
        catch(final SQLException cause)
        {
            throw new ColumnModifyingNotNullConstraintException(cause);
        }
    }

    private static final String TEMPLATE_OF_SQL_QUERY_TO_ADD_NOT_NULL_CONSTRAINT_TO_COLUMN
            = "ALTER TABLE %s ALTER COLUMN %s SET NOT NULL";

    private void dropNotNullConstraintOfColumn(final String nameOfTable, final String nameOfColumn,
                                               final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        try
        {
            final String queryToDropNotNullConstraintForColumn = String.format(
                    ColumnNullConstraintModifier.TEMPLATE_OF_SQL_QUERY_TO_DROP_NOT_NULL_CONSTRAINT_FOR_COLUMN,
                    nameOfTable, nameOfColumn);
            try(final Statement statement = connectionToDataBase.createStatement())
            {
                statement.executeUpdate(queryToDropNotNullConstraintForColumn);
            }
        }
        catch(final SQLException cause)
        {
            throw new ColumnModifyingNotNullConstraintException(cause);
        }
    }

    private static final String TEMPLATE_OF_SQL_QUERY_TO_DROP_NOT_NULL_CONSTRAINT_FOR_COLUMN
            = "ALTER TABLE %s ALTER COLUMN %s DROP NOT NULL";

    private void changeNullableConstraintsOfColumnsAssociatedWithFieldsOfCompositeFieldByNecessary(final Field associatedField,
                                                                                                   final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        final Class<?> typeOfCompositeAssociatedField = associatedField.getType();
        final Set<Field> fieldsOfCompositeAssociatedField = this.fieldsOfClassFounder.findFieldsOfType(
                typeOfCompositeAssociatedField);
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                associatedField);
        for(final Field fieldOfCompositeAssociatedField : fieldsOfCompositeAssociatedField)
        {
            if(!this.isNullableConstraintOfFieldOfCompositeFieldCorresponding(
                    nameOfTable, fieldOfCompositeAssociatedField, connectionToDataBase))
            {
                this.changeNullableConstraintOfColumnAssociatedWithFieldOfCompositeField(
                        nameOfTable, fieldOfCompositeAssociatedField, connectionToDataBase);
            }
        }
    }

    private boolean isNullableConstraintOfFieldOfCompositeFieldCorresponding(final String nameOfTable,
                                                                             final Field fieldOfCompositeField,
                                                                             final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                fieldOfCompositeField);
        final boolean isNullableInAnnotation = this.fieldAssociatedPropertiesIdentifier.isAssociatedWithNullableColumn(
                fieldOfCompositeField);
        final boolean isNullableInInformationSchema = this.isNullableColumnInInformationSchema(
                nameOfTable, nameOfColumn, connectionToDataBase);
        return isNullableInAnnotation == isNullableInInformationSchema;
    }

    private void changeNullableConstraintOfColumnAssociatedWithFieldOfCompositeField(final String nameOfTable,
                                                                                     final Field fieldOfCompositeField,
                                                                                     final Connection connectionToDataBase)
            throws ColumnModifyingNotNullConstraintException
    {
        final boolean shouldAddNotNullConstraintToColumn = !this.fieldAssociatedPropertiesIdentifier
                .isAssociatedWithNullableColumn(fieldOfCompositeField);
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                fieldOfCompositeField);
        if(shouldAddNotNullConstraintToColumn)
        {
            this.addNotNullConstraintToColumn(nameOfTable, nameOfColumn, connectionToDataBase);
        }
        else
        {
            this.dropNotNullConstraintOfColumn(nameOfTable, nameOfColumn, connectionToDataBase);
        }
    }
}
