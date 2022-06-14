package by.bsu.minihibernate.creatorofnameofprimarykeyconstraint;

import by.bsu.minihibernate.annotation.PrimaryKey;
import by.bsu.minihibernate.fieldassociatedpropertiesidentifier.FieldAssociatedPropertiesIdentifier;

import java.lang.reflect.Field;

public final class CreatorOfNameOfPrimaryKeyConstraint
{
    private FieldAssociatedPropertiesIdentifier fieldAssociatedPropertiesIdentifier;

    public static CreatorOfNameOfPrimaryKeyConstraint createCreatorOfNameOfPrimaryKeyConstraint()
    {
        if(CreatorOfNameOfPrimaryKeyConstraint.creatorOfNameOfPrimaryKeyConstraint == null)
        {
            synchronized(CreatorOfNameOfPrimaryKeyConstraint.class)
            {
                if(CreatorOfNameOfPrimaryKeyConstraint.creatorOfNameOfPrimaryKeyConstraint == null)
                {
                    CreatorOfNameOfPrimaryKeyConstraint.creatorOfNameOfPrimaryKeyConstraint
                            = new CreatorOfNameOfPrimaryKeyConstraint();
                }
            }
        }
        return CreatorOfNameOfPrimaryKeyConstraint.creatorOfNameOfPrimaryKeyConstraint;
    }

    private static CreatorOfNameOfPrimaryKeyConstraint creatorOfNameOfPrimaryKeyConstraint = null;

    private CreatorOfNameOfPrimaryKeyConstraint()
    {
        super();
        this.fieldAssociatedPropertiesIdentifier
                = FieldAssociatedPropertiesIdentifier.createFieldAssociatedPropertiesIdentifier();
    }

    public final String createNameOfPrimaryKeyConstraint(final Field associatedField)
    {
        final String nameOfColumn = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedColumn(
                associatedField);
        final String nameOfTable = this.fieldAssociatedPropertiesIdentifier.findNameOfAssociatedTable(associatedField);
        return String.format(CreatorOfNameOfPrimaryKeyConstraint.TEMPLATE_OF_NAME_OF_PRIMARY_KEY_CONSTRAINT,
                nameOfColumn, nameOfTable);
    }

    private static final String TEMPLATE_OF_NAME_OF_PRIMARY_KEY_CONSTRAINT
            = PrimaryKey.PREFIX_OF_NAME_OF_PRIMARY_KEY_CONSTRAINT + "_%s_in_%s";  //first - name of column, second - name of table
}
