package by.bsu.minihibernate.annotationhandler.autoincrementprimarykey;

import by.bsu.minihibernate.annotation.AutoIncrement;
import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.autoincrementprimarykey.exception.AutoIncrementPrimaryKeyAnnotationHandlingException;
import by.bsu.minihibernate.annotationhandler.primarykey.PrimaryKeyAnnotationHandler;
import by.bsu.minihibernate.annotationhandler.primarykey.exception.PrimaryKeyAnnotationHandlingException;
import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;

import java.lang.reflect.Field;
import java.sql.*;

public final class AutoIncrementPrimaryKeyAnnotationHandler implements AnnotationHandler<Field>
{
    private final PrimaryKeyAnnotationHandler primaryKeyAnnotationHandler;
    private final FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;

    public static AutoIncrementPrimaryKeyAnnotationHandler createAutoIncrementPrimaryKeyAnnotationHandler()
    {
        if(AutoIncrementPrimaryKeyAnnotationHandler.autoIncrementPrimaryKeyAnnotationHandler == null)
        {
            synchronized(AutoIncrementPrimaryKeyAnnotationHandler.class)
            {
                if(AutoIncrementPrimaryKeyAnnotationHandler.autoIncrementPrimaryKeyAnnotationHandler == null)
                {
                    AutoIncrementPrimaryKeyAnnotationHandler.autoIncrementPrimaryKeyAnnotationHandler
                            = new AutoIncrementPrimaryKeyAnnotationHandler();
                }
            }
        }
        return AutoIncrementPrimaryKeyAnnotationHandler.autoIncrementPrimaryKeyAnnotationHandler;
    }

    private static AutoIncrementPrimaryKeyAnnotationHandler autoIncrementPrimaryKeyAnnotationHandler = null;

    private AutoIncrementPrimaryKeyAnnotationHandler()
    {
        super();
        this.primaryKeyAnnotationHandler = PrimaryKeyAnnotationHandler.createPrimaryKeyAnnotationHandler();
        this.fieldAssociatedPropertiesIdentifier = FieldAssociatedPropertiesIdentifier.createFieldAssociatedPropertiesIdentifier();
    }

    @Override
    public final void handleAnnotation(final Field annotatedField, final Connection connectionToDataBase)
            throws AutoIncrementPrimaryKeyAnnotationHandlingException
    {
        final Class<?> declaringClass = annotatedField.getDeclaringClass();
        final AutoIncrement autoIncrementPrimaryKeyAnnotation = annotatedField.getAnnotation(
                AutoIncrement.class);
        if(autoIncrementPrimaryKeyAnnotation == null)
        {
            throw new AutoIncrementPrimaryKeyAnnotationHandlingException("Field '" + annotatedField + "' "
                    + "isn't annotated by '" + AutoIncrement.class + "'.");
        }

        try
        {
            this.primaryKeyAnnotationHandler.handleAnnotation(annotatedField, connectionToDataBase);

            if(!this.isAlreadyAutoIncrement(annotatedField, connectionToDataBase))
            {
                this.modifyToAutoIncrement(annotatedField, connectionToDataBase);
            }
        }
        catch(final PrimaryKeyAnnotationHandlingException | SQLException cause)
        {
            throw new AutoIncrementPrimaryKeyAnnotationHandlingException(cause);
        }
    }

    private boolean isAlreadyAutoIncrement(final Field annotatedField, final Connection connectionToDataBase)
            throws SQLException
    {
        final String nameOfValuesGeneratingSequence = this.createNameOfValuesGeneratingSequence(annotatedField);
        try(final PreparedStatement preparedStatement = connectionToDataBase.prepareStatement(
                AutoIncrementPrimaryKeyAnnotationHandler.PREPARED_STATEMENT_TO_DEFINE_EXISTING_SEQUENCE))
        {
            preparedStatement.setString(
                    AutoIncrementPrimaryKeyAnnotationHandler.PARAMETER_INDEX_OF_NAME_OF_VALUES_GENERATING_SEQUENCE,
                    nameOfValuesGeneratingSequence);
            final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    private static final String PREPARED_STATEMENT_TO_DEFINE_EXISTING_SEQUENCE
            = "SELECT 0 FROM pg_class where pg_class.relname = ?";
    private static final int PARAMETER_INDEX_OF_NAME_OF_VALUES_GENERATING_SEQUENCE = 1;

    private String createNameOfValuesGeneratingSequence(final Field annotatedField)
    {
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(annotatedField);
        return String.format(AutoIncrementPrimaryKeyAnnotationHandler.TEMPLATE_OF_NAME_OF_VALUES_GENERATING_SEQUENCE,
                nameOfTable);
    }

    //%s - name of table
    private static final String TEMPLATE_OF_NAME_OF_VALUES_GENERATING_SEQUENCE = "%s_primary_key_value_generating_sequence";

    private void modifyToAutoIncrement(final Field annotatedField, final Connection connectionToDataBase)
            throws SQLException
    {
        final String nameOfValuesGeneratingSequence = this.createNameOfValuesGeneratingSequence(annotatedField);
        connectionToDataBase.setAutoCommit(false);
        try(final Statement statement = connectionToDataBase.createStatement())
        {
            final String sqlQueryToCreateValuesGeneratingSequence = String.format(
                    AutoIncrementPrimaryKeyAnnotationHandler.TEMPLATE_OF_SQL_QUERY_TO_CREATE_VALUES_GENERATING_SEQUENCE,
                    nameOfValuesGeneratingSequence);

            final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                    annotatedField);
            final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                    annotatedField);
            final String sqlQueryToModifyPrimaryKeyColumnToAutoIncrement = String.format(
                    AutoIncrementPrimaryKeyAnnotationHandler.TEMPLATE_OF_SQL_QUERY_TO_MODIFY_PRIMARY_KEY_COLUMN_TO_AUTO_INCREMENT,
                    nameOfTable, nameOfColumn, nameOfValuesGeneratingSequence);

            statement.executeUpdate(sqlQueryToCreateValuesGeneratingSequence);
            statement.executeUpdate(sqlQueryToModifyPrimaryKeyColumnToAutoIncrement);

            connectionToDataBase.commit();
        }
        catch(final SQLException exception)
        {
            connectionToDataBase.rollback();
            throw exception;
        }
        finally
        {
            connectionToDataBase.setAutoCommit(true);
        }
    }

    //%s - name of sequence
    private static final String TEMPLATE_OF_SQL_QUERY_TO_CREATE_VALUES_GENERATING_SEQUENCE
            = "CREATE SEQUENCE %s START 1";
    /*
        first %s - name of table
        second %s - name of column
        third %s - name of sequence
     */
    private static final String TEMPLATE_OF_SQL_QUERY_TO_MODIFY_PRIMARY_KEY_COLUMN_TO_AUTO_INCREMENT
            = "ALTER TABLE %s ALTER COLUMN %s SET DEFAULT nextval('%s')";
}

