package by.bsu.minihibernate.annotationhandler.entity;

import by.bsu.minihibernate.annotation.Entity;
import by.bsu.minihibernate.annotation.Id;
import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.entity.exception.EntityAnnotationHandlingException;
import by.bsu.minihibernate.fieldsofclassfounder.FieldsOfClassFounder;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Set;
import java.util.function.Predicate;

public final class EntityAnnotationHandler implements AnnotationHandler<Class<?>>
{
    private final FieldsOfClassFounder fieldsOfClassFounder;

    public static EntityAnnotationHandler createEntityAnnotationHandler()
    {
        if(EntityAnnotationHandler.entityAnnotationHandler == null)
        {
            synchronized(EntityAnnotationHandler.class)
            {
                if(EntityAnnotationHandler.entityAnnotationHandler == null)
                {
                    EntityAnnotationHandler.entityAnnotationHandler = new EntityAnnotationHandler();
                }
            }
        }
        return EntityAnnotationHandler.entityAnnotationHandler;
    }

    private static EntityAnnotationHandler entityAnnotationHandler = null;

    private EntityAnnotationHandler()
    {
        super();
        this.fieldsOfClassFounder = FieldsOfClassFounder.createFieldsOfClassFounder();
    }

    @Override
    public final void handleAnnotation(final Class<?> annotatedClass, final Connection connectionToDataBase)
            throws EntityAnnotationHandlingException
    {
        final Set<Field> fieldsOfAnnotatedClass = this.fieldsOfClassFounder.findFieldsOfType(annotatedClass);
        final Predicate<Field> predicateToBeFieldOfId = (final Field researchField) -> {
            final Id idAnnotation = researchField.getAnnotation(Id.class);
            return idAnnotation != null;
        };
        final boolean idFieldExists = fieldsOfAnnotatedClass.stream().anyMatch(predicateToBeFieldOfId);
        if(!idFieldExists)
        {
            throw new EntityAnnotationHandlingException("Class '" + annotatedClass.getName() + "' annotated by "
                    + " '" + Entity.class.getName() + "' should consist of field annotated by '" + Id.class.getName()
                    + "'.");
        }
    }
}
