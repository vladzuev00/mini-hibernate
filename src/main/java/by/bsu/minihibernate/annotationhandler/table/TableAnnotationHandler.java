package by.bsu.minihibernate.annotationhandler.table;

import by.bsu.minihibernate.annotation.Table;
import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.table.exception.TableAnnotationHandlingException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class TableAnnotationHandler implements AnnotationHandler<Class<?>>
{
    public static TableAnnotationHandler createTableAnnotationHandler()
    {
        if(TableAnnotationHandler.tableAnnotationHandler == null)
        {
            synchronized(TableAnnotationHandler.class)
            {
                if(TableAnnotationHandler.tableAnnotationHandler == null)
                {
                    TableAnnotationHandler.tableAnnotationHandler = new TableAnnotationHandler();
                }
            }
        }
        return TableAnnotationHandler.tableAnnotationHandler;
    }

    private static TableAnnotationHandler tableAnnotationHandler = null;

    private TableAnnotationHandler()
    {
        super();
    }

    @Override
    public final void handleAnnotation(final Class<?> annotatedClass, final Connection connectionToDataBase)
            throws TableAnnotationHandlingException
    {
        final Table tableAnnotation = annotatedClass.getAnnotation(Table.class);
        final String nameOfTable = tableAnnotation.name();
        final String sqlQueryOfCreatingEmptyTableIfNotExist = String.format(
                TableAnnotationHandler.TEMPLATE_OF_SQL_QUERY_TO_CREATE_EMPTY_TABLE_IF_NOT_EXIST, nameOfTable);
        try(final Statement statement = connectionToDataBase.createStatement())
        {
            statement.execute(sqlQueryOfCreatingEmptyTableIfNotExist);
        }
        catch(final SQLException cause)
        {
            throw new TableAnnotationHandlingException(cause);
        }
    }

    private static final String TEMPLATE_OF_SQL_QUERY_TO_CREATE_EMPTY_TABLE_IF_NOT_EXIST
            = "CREATE TABLE IF NOT EXISTS %s()";
}
