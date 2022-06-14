package by.bsu.minihibernate.creatorofnameofforeignkeyconstraint;

import by.bsu.minihibernate.annotation.Table;
import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;

import java.lang.reflect.Field;

public final class CreatorOfNameOfForeignKeyConstraint
{
    private final FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;

    public static CreatorOfNameOfForeignKeyConstraint createCreatorOfNameOfForeignKeyConstraint()
    {
        if(CreatorOfNameOfForeignKeyConstraint.creatorOfNameOfForeignKeyConstraint == null)
        {
            synchronized(CreatorOfNameOfForeignKeyConstraint.class)
            {
                if(CreatorOfNameOfForeignKeyConstraint.creatorOfNameOfForeignKeyConstraint == null)
                {
                    CreatorOfNameOfForeignKeyConstraint.creatorOfNameOfForeignKeyConstraint
                            = new CreatorOfNameOfForeignKeyConstraint();
                }
            }
        }
        return CreatorOfNameOfForeignKeyConstraint.creatorOfNameOfForeignKeyConstraint;
    }

    private static CreatorOfNameOfForeignKeyConstraint creatorOfNameOfForeignKeyConstraint = null;

    private CreatorOfNameOfForeignKeyConstraint()
    {
        super();
        this.fieldAssociatedPropertiesIdentifier
                = FieldAssociatedPropertiesIdentifier.createFieldAssociatedPropertiesIdentifier();
    }

    public final String createNameOfForeignKeyConstraint(final Field associatedField)
    {
        final String nameOfFirstTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(
                associatedField);

        final Class<?> typeOfAssociatedField = associatedField.getType();
        final Table tableAnnotationOfTypeOfAssociatedField = typeOfAssociatedField.getAnnotation(Table.class);
        final String nameOfSecondTable = tableAnnotationOfTypeOfAssociatedField.name();

        return String.format(CreatorOfNameOfForeignKeyConstraint.TEMPLATE_OF_NAME_OF_FOREIGN_KEY_CONSTRAINT,
                nameOfFirstTable, nameOfSecondTable);
    }

    /*
        first %s - name of first table
        second %s - name of second table
     */
    private static final String TEMPLATE_OF_NAME_OF_FOREIGN_KEY_CONSTRAINT = "foreign_key_%s_to_%s";
}

