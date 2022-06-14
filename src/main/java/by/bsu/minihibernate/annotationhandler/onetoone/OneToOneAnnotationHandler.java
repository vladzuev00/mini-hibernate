package by.bsu.minihibernate.annotationhandler.onetoone;

import by.bsu.minihibernate.annotation.OneToOne;
import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.onetoone.exception.OneToOneAnnotationHandlingException;
import by.bsu.minihibernate.annotationhandlingrunner.AnnotationHandlingRunner;
import by.bsu.minihibernate.creatorofnameofforeignkeyconstraint.CreatorOfNameOfForeignKeyConstraint;
import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
public final class OneToOneAnnotationHandler implements AnnotationHandler<Field>
{
    private final AnnotationHandlingRunner annotationHandlingRunner;
    private final CreatorOfNameOfForeignKeyConstraint creatorOfNameOfForeignKeyConstraint;
    private final FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;

    public static OneToOneAnnotationHandler createOneToOneAnnotationHandler()
    {
        if(OneToOneAnnotationHandler.oneToOneAnnotationHandler == null)
        {
            synchronized(OneToOneAnnotationHandler.class)
            {
                if(OneToOneAnnotationHandler.oneToOneAnnotationHandler == null)
                {
                    OneToOneAnnotationHandler.oneToOneAnnotationHandler = new OneToOneAnnotationHandler();
                }
            }
        }
        return OneToOneAnnotationHandler.oneToOneAnnotationHandler;
    }

    private static OneToOneAnnotationHandler oneToOneAnnotationHandler = null;

    private OneToOneAnnotationHandler()
    {
        super();
        this.annotationHandlingRunner = AnnotationHandlingRunner.createAnnotationHandlingRunner();
        this.creatorOfNameOfForeignKeyConstraint = CreatorOfNameOfForeignKeyConstraint
                .createCreatorOfNameOfForeignKeyConstraint();
        this.fieldAssociatedPropertiesIdentifier = FieldAssociatedPropertiesIdentifier
                .createFieldAssociatedPropertiesIdentifier();
    }

    @Override
    public final void handleAnnotation(final Field annotatedField, final Connection connectionToDataBase)
            throws OneToOneAnnotationHandlingException
    {
        final OneToOne oneToOneAnnotation = annotatedField.getAnnotation(OneToOne.class);
        if(oneToOneAnnotation == null)
        {
            throw new OneToOneAnnotationHandlingException("Field '" + annotatedField + "' isn't annotated by '"
                    + OneToOne.class.getName() + "'.");
        }

        final Class<?> typeOfAnnotatedField = annotatedField.getType();
        this.annotationHandlingRunner.runAnnotationHandling(typeOfAnnotatedField);

        if(!this.isCorrespondingForeignKeyConstraintAlreadyExisting(annotatedField, connectionToDataBase))
        {

        }
    }

    private boolean isCorrespondingForeignKeyConstraintAlreadyExisting(final Field annotatedField,
                                                                       final Connection connectionToDataBase)
            throws SQLException
    {
        try(final PreparedStatement preparedStatement = connectionToDataBase.prepareStatement(
                OneToOneAnnotationHandler.PREPARED_STATEMENT_TO_DEFINE_FOREIGN_KEY_CONSTRAINT_EXISTING))
        {
            final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                    annotatedField);
            preparedStatement.setString(
                    OneToOneAnnotationHandler.PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT,
                    nameOfTable);

            final String nameOfForeignKeyConstraint = this.creatorOfNameOfForeignKeyConstraint
                    .createNameOfForeignKeyConstraint(annotatedField);
            preparedStatement.setString(
                    OneToOneAnnotationHandler.PARAMETER_INDEX_OF_NAME_OF_CONSTRAINT_IN_PREPARED_STATEMENT,
                    nameOfForeignKeyConstraint);

            final ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next()
        }
    }

    private static final String PREPARED_STATEMENT_TO_DEFINE_FOREIGN_KEY_CONSTRAINT_EXISTING
            = "SELECT 1 FROM information_schema.table_constraints "
            + "WHERE information_schema.table_constraints.table_name = ? "
            + "AND information_schema.table_constraints.constraint_name = ?";
    private static final int PARAMETER_INDEX_OF_NAME_OF_TABLE_IN_PREPARED_STATEMENT = 1;
    private static final int PARAMETER_INDEX_OF_NAME_OF_CONSTRAINT_IN_PREPARED_STATEMENT = 2;

    private void addForeignKeyConstraint(final Field annotatedField, final Connection connectionToDataBase)
    {
        try(final Sta)
    }


        first %s - name of altered table
        second %s - name of constraint
        third %s - name of column of foreign key
        fourth %s - name of linked table
        fifth %s - name of primary key of linked table

    private static final String TEMPLATE_OF_SQL_QUERY_TO_ADD_FOREIGN_KEY_CONSTRAINT
            = "ALTER TABLE %s ADD CONSTRAINT %s FOREIGN KEY(%s) REFERENCES %s(%s)";
}
*/
