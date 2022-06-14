package by.bsu.minihibernate.columnmodifierbynecessary.type;

import by.bsu.minihibernate.columnmodifierbynecessary.ColumnModifierByNecessary;
import by.bsu.minihibernate.columnmodifierbynecessary.type.exception.ColumnModifyingTypeException;
import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;
import by.bsu.minihibernate.fieldsofclassfounder.FieldsOfClassFounder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Set;

public final class ColumnTypeModifier implements ColumnModifierByNecessary
{
    private final FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;
    private final FieldsOfClassFounder fieldsOfClassFounder;

    public static ColumnTypeModifier createColumnTypeModifier()
    {
        if(ColumnTypeModifier.columnTypeModifier == null)
        {
            synchronized(ColumnTypeModifier.class)
            {
                if(ColumnTypeModifier.columnTypeModifier == null)
                {
                    ColumnTypeModifier.columnTypeModifier = new ColumnTypeModifier();
                }
            }
        }
        return ColumnTypeModifier.columnTypeModifier;
    }

    private static ColumnTypeModifier columnTypeModifier = null;

    private ColumnTypeModifier()
    {
        super();
        this.fieldAssociatedPropertiesIdentifier
                = FieldAssociatedPropertiesIdentifier.createFieldAssociatedPropertiesIdentifier();
        this.fieldsOfClassFounder = FieldsOfClassFounder.createFieldsOfClassFounder();
    }

    @Override
    public final void modifyIfNecessary(final Field associatedField, final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        if(this.fieldAssociatedPropertiesIdentifier.isSimpleField(associatedField)
                && !this.isTypeOfSimpleFieldCorresponding(associatedField, connectionToDataBase))
        {
            this.changeTypeOfColumnAssociatedWithSimpleField(associatedField, connectionToDataBase);
        }
        else if(this.fieldAssociatedPropertiesIdentifier.isCompositeField(associatedField))
        {
            this.changeTypesOfColumnsAssociatedWithFieldsOfCompositeFieldByNecessary(
                    associatedField, connectionToDataBase);
        }
    }

    private boolean isTypeOfSimpleFieldCorresponding(final Field associatedField, final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                associatedField);
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                associatedField);
        final String typeOfColumnInInformationSchema = this.findTypeOfColumnInInformationSchema(
                nameOfTable, nameOfColumn, connectionToDataBase);
        final String typeOfColumnInAnnotation = this.fieldAssociatedPropertiesIdentifier
                .findNameOfTypeOfAssociatedColumn(associatedField);
        return typeOfColumnInAnnotation.equals(typeOfColumnInInformationSchema);
    }

    private String findTypeOfColumnInInformationSchema(final String nameOfTable, final String nameOfColumn,
                                                       final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        try(final PreparedStatement preparedStatement = connectionToDataBase.prepareStatement(
                ColumnTypeModifier.SQL_QUERY_OF_PREPARED_STATEMENT_TO_SELECT_TYPE_OF_COLUMN))
        {
            preparedStatement.setString(ColumnTypeModifier.PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT,
                    nameOfTable);
            preparedStatement.setString(ColumnTypeModifier.PARAMETER_INDEX_OF_NAME_OF_COLUMN_IN_PREPARED_STATEMENT,
                    nameOfColumn);
            final ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString(ColumnTypeModifier.NAME_OF_COLUMN_OF_TYPE_OF_COLUMN);
        }
        catch(final SQLException cause)
        {
            throw new ColumnModifyingTypeException(cause);
        }
    }

    private static final String SQL_QUERY_OF_PREPARED_STATEMENT_TO_SELECT_TYPE_OF_COLUMN
            = "SELECT information_schema.columns."+ ColumnTypeModifier.NAME_OF_COLUMN_OF_TYPE_OF_COLUMN
            + " FROM information_schema.columns "
            + "WHERE information_schema.columns.table_name = ? AND information_schema.columns.column_name = ?";
    private static final String NAME_OF_COLUMN_OF_TYPE_OF_COLUMN = "data_type";
    private static final int PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT = 1;
    private static final int PARAMETER_INDEX_OF_NAME_OF_COLUMN_IN_PREPARED_STATEMENT = 2;

    private void changeTypeOfColumnAssociatedWithSimpleField(final Field associatedField,
                                                             final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                associatedField);
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                associatedField);
        final String newTypeOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfTypeOfAssociatedColumn(
                associatedField);
        this.changeTypeOfColumn(nameOfTable, nameOfColumn, newTypeOfColumn, connectionToDataBase);
    }

    private void changeTypeOfColumn(final String nameOfTable, final String nameOfColumn,
                                    final String newTypeOfColumn, final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        final String queryToChangeTypeOfColumn = String.format(
                ColumnTypeModifier.TEMPLATE_OF_QUERY_TO_CHANGE_TYPE_OF_COLUMN, nameOfTable, nameOfColumn,
                newTypeOfColumn);
        try(final Statement statement = connectionToDataBase.createStatement())
        {
            statement.executeUpdate(queryToChangeTypeOfColumn);
        }
        catch(final SQLException cause)
        {
            throw new ColumnModifyingTypeException(cause);
        }
    }

    private static final String TEMPLATE_OF_QUERY_TO_CHANGE_TYPE_OF_COLUMN
            = "ALTER TABLE %s ALTER COLUMN %s TYPE %s";

    private void changeTypesOfColumnsAssociatedWithFieldsOfCompositeFieldByNecessary(final Field associatedField,
                                                                                     final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        final Class<?> typeOfCompositeAssociatedField = associatedField.getType();
        final Set<Field> fieldsOfCompositeAssociatedField = this.fieldsOfClassFounder.findFieldsOfType(
                typeOfCompositeAssociatedField);
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                associatedField);
        for(final Field fieldOfCompositeAssociatedField : fieldsOfCompositeAssociatedField)
        {
            if(!this.isTypeOfFieldOfCompositeFieldCorresponding(
                    nameOfTable, fieldOfCompositeAssociatedField, connectionToDataBase))
            {
                this.changeTypeOfColumnAssociatedWithFieldOfCompositeField(
                        nameOfTable, fieldOfCompositeAssociatedField, connectionToDataBase);
            }
        }
    }

    private boolean isTypeOfFieldOfCompositeFieldCorresponding(final String nameOfTable,
                                                               final Field fieldOfCompositeField,
                                                               final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                fieldOfCompositeField);
        final String typeInAnnotation = this.fieldAssociatedPropertiesIdentifier.findNameOfTypeOfAssociatedColumn(
                fieldOfCompositeField);
        final String typeInInformationSchema = this.findTypeOfColumnInInformationSchema(
                nameOfTable, nameOfColumn, connectionToDataBase);
        return typeInAnnotation.equals(typeInInformationSchema);
    }

    private void changeTypeOfColumnAssociatedWithFieldOfCompositeField(final String nameOfTable,
                                                                       final Field fieldOfCompositeField,
                                                                       final Connection connectionToDataBase)
            throws ColumnModifyingTypeException
    {
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                fieldOfCompositeField);
        final String newTypeOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfTypeOfAssociatedColumn(
                fieldOfCompositeField);
        this.changeTypeOfColumn(nameOfTable, nameOfColumn, newTypeOfColumn, connectionToDataBase);
    }
}
