package by.bsu.minihibernate.annotationhandler.column;

import by.bsu.minihibernate.annotation.Column;
import by.bsu.minihibernate.annotation.Table;
import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.column.exception.ColumnAnnotationHandlingException;
import by.bsu.minihibernate.columnmodifierbynecessary.ColumnModifierByNecessary;
import by.bsu.minihibernate.columnmodifierbynecessary.exception.ColumnModifyingException;
import by.bsu.minihibernate.columnmodifierbynecessary.nullconstraint.ColumnNullConstraintModifier;
import by.bsu.minihibernate.columnmodifierbynecessary.type.ColumnTypeModifier;
import by.bsu.minihibernate.fieldmapper.FieldMapperToDataBase;
import by.bsu.minihibernate.fieldmapper.exception.FieldMappingException;

import java.lang.reflect.Field;
import java.sql.Connection;

public final class ColumnAnnotationHandler implements AnnotationHandler<Field>
{
    private final FieldMapperToDataBase fieldMapperToDataBase;
    private final ColumnModifierByNecessary[] columnModifiersByNecessary;

    public static ColumnAnnotationHandler createColumnAnnotationHandler()
    {
        if(ColumnAnnotationHandler.columnAnnotationHandler == null)
        {
            synchronized(ColumnAnnotationHandler.class)
            {
                if(ColumnAnnotationHandler.columnAnnotationHandler == null)
                {
                    ColumnAnnotationHandler.columnAnnotationHandler = new ColumnAnnotationHandler();
                }
            }
        }
        return ColumnAnnotationHandler.columnAnnotationHandler;
    }

    private static ColumnAnnotationHandler columnAnnotationHandler = null;

    private ColumnAnnotationHandler()
    {
        super();
        this.fieldMapperToDataBase = FieldMapperToDataBase.createFieldMapperToDataBase();
        this.columnModifiersByNecessary = new ColumnModifierByNecessary[]{
                ColumnNullConstraintModifier.createColumnNullConstraintModifier(),
                ColumnTypeModifier.createColumnTypeModifier()
        };
    }

    @Override
    public final void handleAnnotation(final Field annotatedField, final Connection connectionToDataBase)
            throws ColumnAnnotationHandlingException
    {
        final Column columnAnnotation = annotatedField.getAnnotation(Column.class);
        if(columnAnnotation == null)
        {
            throw new ColumnAnnotationHandlingException("Field '" + annotatedField + "' " + " isn't annotated by "
                    + Column.class.getName() + ". Mapping this field is impossible.");
        }

        final Class<?> declaringClassOfField = annotatedField.getDeclaringClass();
        final Table tableAnnotation = declaringClassOfField.getAnnotation(Table.class);
        if(tableAnnotation == null)
        {
            throw new ColumnAnnotationHandlingException("Class '" + declaringClassOfField.getName()
                    + "' isn't annotated by annotation '" + Table.class.getName() + "'.");
        }

        try
        {
            if(!this.fieldMapperToDataBase.isFieldAlreadyMapped(annotatedField, connectionToDataBase))
            {
                this.fieldMapperToDataBase.mapFieldWithoutConstraints(annotatedField, connectionToDataBase);
            }
            for(final ColumnModifierByNecessary columnModifierByNecessary : this.columnModifiersByNecessary)
            {
                columnModifierByNecessary.modifyIfNecessary(annotatedField, connectionToDataBase);
            }
        }
        catch(final FieldMappingException | ColumnModifyingException cause)
        {
            throw new ColumnAnnotationHandlingException(cause);
        }
    }
}
