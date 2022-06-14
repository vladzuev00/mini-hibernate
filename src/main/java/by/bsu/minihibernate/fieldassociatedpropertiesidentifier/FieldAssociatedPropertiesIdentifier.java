package by.bsu.minihibernate.fieldassociatedpropertiesidentifier;

import by.bsu.minihibernate.annotation.Column;
import by.bsu.minihibernate.annotation.Composite;
import by.bsu.minihibernate.annotation.Table;
import by.bsu.minihibernate.annotation.PrimaryKey;
import by.bsu.minihibernate.sqltype.SQLType;

import java.lang.reflect.Field;

public class FieldAssociatedPropertiesIdentifier
{
    public static FieldAssociatedPropertiesIdentifier createFieldAssociatedPropertiesIdentifier()
    {
        if(FieldAssociatedPropertiesIdentifier.fieldAssociatedPropertiesIdentifier == null)
        {
            synchronized(FieldAssociatedPropertiesIdentifier.class)
            {
                if(FieldAssociatedPropertiesIdentifier.fieldAssociatedPropertiesIdentifier == null)
                {
                    FieldAssociatedPropertiesIdentifier.fieldAssociatedPropertiesIdentifier
                            = new FieldAssociatedPropertiesIdentifier();
                }
            }
        }
        return FieldAssociatedPropertiesIdentifier.fieldAssociatedPropertiesIdentifier;
    }

    private static FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier = null;

    private FieldAssociatedPropertiesIdentifier()
    {
        super();
    }

    public final String findNameOfAssociatedTable(final Field field)
    {
        final Class<?> declaringClassOfField = field.getDeclaringClass();
        final Table tableAnnotationOfDeclaringClass = declaringClassOfField.getAnnotation(Table.class);
        return tableAnnotationOfDeclaringClass.name();
    }

    public final String findNameOfAssociatedColumn(final Field field)
    {
        final Column columnAnnotationOfField = field.getAnnotation(Column.class);
        return columnAnnotationOfField.name();
    }

    public final String findNameOfTypeOfAssociatedColumn(final Field field)
    {
        final Column columnAnnotationOfField = field.getAnnotation(Column.class);
        final SQLType sqlType = columnAnnotationOfField.type();
        return sqlType.getValue();
    }

    public final boolean isSimpleField(final Field field)
    {
        final Composite compositeAnnotation = field.getAnnotation(Composite.class);
        return compositeAnnotation == null;
    }

    public final boolean isCompositeField(final Field field)
    {
        final Composite compositeAnnotation = field.getAnnotation(Composite.class);
        return compositeAnnotation != null;
    }

    public final boolean isFieldAssociatedWithPrimaryKey(final Field field)
    {
        final PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
        return primaryKeyAnnotation != null;
    }

    public final boolean isAssociatedWithNullableColumn(final Field field)
    {
        final Column columnAnnotationOfField = field.getAnnotation(Column.class);
        return columnAnnotationOfField.isNullable();
    }
}
