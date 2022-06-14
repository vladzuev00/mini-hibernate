package by.bsu.minihibernate.annotationhandler.primarykey;

import by.bsu.minihibernate.annotation.Column;
import by.bsu.minihibernate.annotation.Table;
import by.bsu.minihibernate.annotation.PrimaryKey;
import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.column.ColumnAnnotationHandler;
import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;
import by.bsu.minihibernate.annotationhandler.primarykey.exception.PrimaryKeyAnnotationHandlingException;
import by.bsu.minihibernate.annotationhandler.primarykey.exception.PrimaryKeyConstraintAddingException;
import by.bsu.minihibernate.annotationhandler.primarykey.exception.PrimaryKeyConstraintSearchingException;
import by.bsu.minihibernate.creatorofnameofprimarykeyconstraint.CreatorOfNameOfPrimaryKeyConstraint;
import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;

import java.lang.reflect.Field;
import java.sql.*;

public final class PrimaryKeyAnnotationHandler implements AnnotationHandler<Field>
{
    private final AnnotationHandler<Field> columnAnnotationHandler;
    private final CreatorOfNameOfPrimaryKeyConstraint creatorOfNameOfPrimaryKeyConstraint;
    private final PrimaryKeyOperator primaryKeyOperator;
    private final FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;

    public static PrimaryKeyAnnotationHandler createPrimaryKeyAnnotationHandler()
    {
        if(PrimaryKeyAnnotationHandler.primaryKeyAnnotationHandler == null)
        {
            synchronized(PrimaryKeyAnnotationHandler.class)
            {
                if(PrimaryKeyAnnotationHandler.primaryKeyAnnotationHandler == null)
                {
                    PrimaryKeyAnnotationHandler.primaryKeyAnnotationHandler = new PrimaryKeyAnnotationHandler();
                }
            }
        }
        return PrimaryKeyAnnotationHandler.primaryKeyAnnotationHandler;
    }

    private static PrimaryKeyAnnotationHandler primaryKeyAnnotationHandler = null;

    private PrimaryKeyAnnotationHandler()
    {
        super();
        this.creatorOfNameOfPrimaryKeyConstraint
                = CreatorOfNameOfPrimaryKeyConstraint.createCreatorOfNameOfPrimaryKeyConstraint();
        this.columnAnnotationHandler = ColumnAnnotationHandler.createColumnAnnotationHandler();
        this.primaryKeyOperator = this.new PrimaryKeyOperator();
        this.fieldAssociatedPropertiesIdentifier
                = FieldAssociatedPropertiesIdentifier.createFieldAssociatedPropertiesIdentifier();
    }

    @Override
    public final void handleAnnotation(final Field annotatedField, final Connection connectionToDataBase)
            throws PrimaryKeyAnnotationHandlingException
    {
        final Class<?> declaringClassOfField = annotatedField.getDeclaringClass();
        final PrimaryKey primaryKeyAnnotation = annotatedField.getAnnotation(PrimaryKey.class);
        if(primaryKeyAnnotation == null)
        {
            throw new PrimaryKeyAnnotationHandlingException("Field '" + annotatedField + "' of class '"
                    + declaringClassOfField.getName() + "' isn't annotated by annotation '"
                    + PrimaryKey.class.getName() + "'.");
        }

        final Column columnAnnotation = annotatedField.getAnnotation(Column.class);
        if(columnAnnotation == null)
        {
            throw new PrimaryKeyAnnotationHandlingException("Field '" + annotatedField + "' of class '"
                    + declaringClassOfField.getName() + "' isn't annotated by annotation '"
                    + Column.class.getName() + "'.");
        }

        final Table tableAnnotation = declaringClassOfField.getAnnotation(Table.class);
        if(tableAnnotation == null)
        {
            throw new PrimaryKeyAnnotationHandlingException("Class '" + declaringClassOfField.getName()
                    + "' isn't annotated by annotation '" + Table.class.getName() + "'.");
        }

        try
        {
            if(!this.primaryKeyOperator.isCorrespondingPrimaryKeyConstraintAlreadyMapped(
                    annotatedField, connectionToDataBase))
            {
                this.columnAnnotationHandler.handleAnnotation(annotatedField, connectionToDataBase);
                this.primaryKeyOperator.addPrimaryKeyConstraint(annotatedField, connectionToDataBase);
            }
        }
        catch(final AnnotationHandlingException cause)
        {
            throw new PrimaryKeyAnnotationHandlingException(cause);
        }
    }

    private final class PrimaryKeyOperator
    {
        public PrimaryKeyOperator()
        {
            super();
        }

        public final boolean isCorrespondingPrimaryKeyConstraintAlreadyMapped(final Field associatedField,
                                                                              final Connection connectionToDataBase)
                throws PrimaryKeyConstraintSearchingException
        {
            try
            {
                final String nameOfTable = PrimaryKeyAnnotationHandler.this.fieldAssociatedPropertiesIdentifier
                        .findNameOfAssociatedTable(associatedField);
                final String nameOfPrimaryKeyConstraint = PrimaryKeyAnnotationHandler.this
                        .creatorOfNameOfPrimaryKeyConstraint.createNameOfPrimaryKeyConstraint(associatedField);
                try(final PreparedStatement preparedStatement = connectionToDataBase.prepareStatement(
                        PrimaryKeyOperator.SQL_QUERY_TO_DEFINE_CORRESPONDING_PRIMARY_KEY_CONSTRAINT_EXISTING))
                {
                    preparedStatement.setString(
                            PrimaryKeyOperator.PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT, nameOfTable);
                    preparedStatement.setString(
                            PrimaryKeyOperator.PARAMETER_INDEX_OF_NAME_OF_CONSTRAINT_IN_PREPARED_STATEMENT,
                            nameOfPrimaryKeyConstraint);
                    final ResultSet resultSet = preparedStatement.executeQuery();
                    return resultSet.next();
                }
            }
            catch(final SQLException cause)
            {
                throw new PrimaryKeyConstraintSearchingException(cause);
            }
        }

        private static final String SQL_QUERY_TO_DEFINE_CORRESPONDING_PRIMARY_KEY_CONSTRAINT_EXISTING
                = "SELECT 1 FROM information_schema.table_constraints "
                + "WHERE information_schema.table_constraints.table_name = ? "
                + "AND information_schema.table_constraints.constraint_name = ?";
        private static final int PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT = 1;
        private static final int PARAMETER_INDEX_OF_NAME_OF_CONSTRAINT_IN_PREPARED_STATEMENT = 2;

        public final void addPrimaryKeyConstraint(final Field associatedField, final Connection connectionToDataBase)
                throws PrimaryKeyConstraintAddingException
        {
            try
            {
                final String nameOfTable = PrimaryKeyAnnotationHandler.this.fieldAssociatedPropertiesIdentifier
                        .findNameOfAssociatedTable(associatedField);
                final String nameOfPrimaryKeyConstraint = PrimaryKeyAnnotationHandler.this
                        .creatorOfNameOfPrimaryKeyConstraint.createNameOfPrimaryKeyConstraint(associatedField);
                final String nameOfColumn = PrimaryKeyAnnotationHandler.this.fieldAssociatedPropertiesIdentifier
                        .findNameOfAssociatedColumn(associatedField);
                final String sqlQueryToAddPrimaryKeyConstraint = String.format(
                        PrimaryKeyOperator.TEMPLATE_OF_SQL_QUERY_TO_ADD_PRIMARY_KEY_CONSTRAINT, nameOfTable,
                        nameOfPrimaryKeyConstraint, nameOfColumn);
                try(final Statement statement = connectionToDataBase.createStatement())
                {
                    statement.executeUpdate(sqlQueryToAddPrimaryKeyConstraint);
                }
            }
            catch(final SQLException cause)
            {
                throw new PrimaryKeyConstraintAddingException(cause);
            }
        }

        private static final String TEMPLATE_OF_SQL_QUERY_TO_ADD_PRIMARY_KEY_CONSTRAINT
                = "ALTER TABLE %s ADD CONSTRAINT %s PRIMARY KEY(%s)";
    }
}

