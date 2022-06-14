package by.bsu.minihibernate.fieldmapper;

import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;
import by.bsu.minihibernate.fieldmapper.exception.FieldMappingException;
import by.bsu.minihibernate.fieldsofclassfounder.FieldsOfClassFounder;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.Set;

public class FieldMapperToDataBase
{
    private final FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;
    private final FieldsOfClassFounder fieldsOfClassFounder;

    public static FieldMapperToDataBase createFieldMapperToDataBase()
    {
        if(FieldMapperToDataBase.fieldMapperToDataBase == null)
        {
            synchronized(FieldMapperToDataBase.class)
            {
                if(FieldMapperToDataBase.fieldMapperToDataBase == null)
                {
                    FieldMapperToDataBase.fieldMapperToDataBase = new FieldMapperToDataBase();
                }
            }
        }
        return FieldMapperToDataBase.fieldMapperToDataBase;
    }

    private static FieldMapperToDataBase fieldMapperToDataBase = null;

    private FieldMapperToDataBase()
    {
        super();
        this.fieldAssociatedPropertiesIdentifier
                = FieldAssociatedPropertiesIdentifier.createFieldAssociatedPropertiesIdentifier();
        this.fieldsOfClassFounder = FieldsOfClassFounder.createFieldsOfClassFounder();
    }

    public final boolean isFieldAlreadyMapped(final Field researchField, final Connection connectionToDataBase)
            throws FieldMappingException
    {
        if(this.fieldAssociatedPropertiesIdentifier.isSimpleField(researchField))
        {
            return this.isSimpleFieldAlreadyMapped(researchField, connectionToDataBase);
        }
        else
        {
            return this.isCompositeFieldAlreadyMapped(researchField, connectionToDataBase);
        }
    }

    private boolean isSimpleFieldAlreadyMapped(final Field researchField, final Connection connectionToDataBase)
            throws FieldMappingException
    {
        final String nameOfAssociatedTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                researchField);
        final String nameOfAssociatedColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                researchField);
        try(final PreparedStatement preparedStatement = connectionToDataBase.prepareStatement(
                FieldMapperToDataBase.SQL_QUERY_OF_PREPARED_STATEMENT_OF_DEFINING_COLUMN_EXISTING))
        {
            preparedStatement.setString(
                    FieldMapperToDataBase.PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT,
                    nameOfAssociatedTable);
            preparedStatement.setString(
                    FieldMapperToDataBase.PARAMETER_INDEX_OF_NAME_OF_COLUMN_IN_PREPARED_STATEMENT,
                    nameOfAssociatedColumn);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch(final SQLException cause)
        {
            throw new FieldMappingException(cause);
        }
    }

    private static final String SQL_QUERY_OF_PREPARED_STATEMENT_OF_DEFINING_COLUMN_EXISTING
            = "SELECT 1 FROM information_schema.columns " +
            "WHERE information_schema.columns.table_name = ? AND information_schema.columns.column_name = ?";
    private static final int PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT = 1;
    private static final int PARAMETER_INDEX_OF_NAME_OF_COLUMN_IN_PREPARED_STATEMENT = 2;

    private boolean isCompositeFieldAlreadyMapped(final Field researchField, final Connection connectionToDataBase)
            throws FieldMappingException
    {
        try
        {
            final String nameOfAssociatedTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                    researchField);
            final Class<?> typeOfResearchCompositeField = researchField.getType();
            final Set<Field> fieldsOfCompositeField = this.fieldsOfClassFounder.findFieldsOfType(
                    typeOfResearchCompositeField);
            try(final PreparedStatement preparedStatement = connectionToDataBase.prepareStatement(
                    FieldMapperToDataBase.SQL_QUERY_OF_PREPARED_STATEMENT_OF_DEFINING_COLUMN_EXISTING))
            {
                String nameOfAssociatedColumn;
                ResultSet resultSet;
                for(final Field fieldOfCompositeField : fieldsOfCompositeField)
                {
                    preparedStatement.setString(
                            FieldMapperToDataBase.PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT,
                            nameOfAssociatedTable);

                    nameOfAssociatedColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                            fieldOfCompositeField);
                    preparedStatement.setString(
                            FieldMapperToDataBase.PARAMETER_INDEX_OF_NAME_OF_COLUMN_IN_PREPARED_STATEMENT,
                            nameOfAssociatedColumn);

                    resultSet = preparedStatement.executeQuery();
                    if(!resultSet.next())
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        catch(final SQLException cause)
        {
            throw new FieldMappingException(cause);
        }
    }

    public final void mapFieldWithoutConstraints(final Field mappedField, final Connection connectionToDataBase)
            throws FieldMappingException
    {
        if(this.fieldAssociatedPropertiesIdentifier.isSimpleField(mappedField))
        {
            this.mapSimpleFieldWithoutConstraints(mappedField, connectionToDataBase);
        }
        else
        {
            this.mapCompositeFieldWithoutConstraints(mappedField, connectionToDataBase);
        }
    }

    private void mapSimpleFieldWithoutConstraints(final Field mappedField, final Connection connectionToDataBase)
            throws FieldMappingException
    {
        try
        {
            final String nameOfAssociatedTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                    mappedField);
            final String nameOfAssociatedColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                    mappedField);
            final String nameOfTypeOfAssociatedColumn = this.fieldAssociatedPropertiesIdentifier
                    .findNameOfTypeOfAssociatedColumn(mappedField);
            try(final Statement statement = connectionToDataBase.createStatement())
            {
                final String queryToAddNewColumnWithoutConstraints = String.format(
                        FieldMapperToDataBase.TEMPLATE_OF_SQL_QUERY_TO_ADD_NEW_COLUMN_WITHOUT_CONSTRAINTS,
                        nameOfAssociatedTable, nameOfAssociatedColumn, nameOfTypeOfAssociatedColumn);
                statement.executeUpdate(queryToAddNewColumnWithoutConstraints);
            }
        }
        catch(final SQLException cause)
        {
            throw new FieldMappingException(cause);
        }
    }

    private static final String TEMPLATE_OF_SQL_QUERY_TO_ADD_NEW_COLUMN_WITHOUT_CONSTRAINTS
            = "ALTER TABLE %s ADD COLUMN %s %s";

    private void mapCompositeFieldWithoutConstraints(final Field mappedField, final Connection connectionToDataBase)
            throws FieldMappingException
    {
        try
        {
            final String nameOfAssociatedTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                    mappedField);
            final Class<?> typeOfMappedCompositeField = mappedField.getType();
            final Set<Field> fieldsOfCompositeField = this.fieldsOfClassFounder.findFieldsOfType(
                    typeOfMappedCompositeField);
            try(final Statement statement = connectionToDataBase.createStatement())
            {
                String queryToAddNewColumnWithoutConstraints;
                String nameOfAssociatedColumn;
                String nameOfTypeOfAssociatedColumn;
                for(final Field fieldOfCompositeField : fieldsOfCompositeField)
                {
                    nameOfAssociatedColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                            fieldOfCompositeField);
                    nameOfTypeOfAssociatedColumn = this.fieldAssociatedPropertiesIdentifier
                            .findNameOfTypeOfAssociatedColumn(fieldOfCompositeField);
                    queryToAddNewColumnWithoutConstraints = String.format(
                            FieldMapperToDataBase.TEMPLATE_OF_SQL_QUERY_TO_ADD_NEW_COLUMN_WITHOUT_CONSTRAINTS,
                            nameOfAssociatedTable, nameOfAssociatedColumn, nameOfTypeOfAssociatedColumn);
                    statement.executeUpdate(queryToAddNewColumnWithoutConstraints);
                }
            }
        }
        catch(final SQLException cause)
        {
            throw new FieldMappingException(cause);
        }
    }
}
