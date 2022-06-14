package by.bsu.minihibernate.frameworkconfiguration;

import by.bsu.minihibernate.annotation.*;
import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.autoincrementprimarykey.AutoIncrementPrimaryKeyAnnotationHandler;
import by.bsu.minihibernate.annotationhandler.column.ColumnAnnotationHandler;
import by.bsu.minihibernate.annotationhandler.entity.EntityAnnotationHandler;
import by.bsu.minihibernate.annotationhandler.primarykey.PrimaryKeyAnnotationHandler;
import by.bsu.minihibernate.annotationhandler.table.TableAnnotationHandler;
import by.bsu.minihibernate.annotationhandlingrunner.AnnotationHandlingRunner;
import by.bsu.minihibernate.annotationhandlingrunner.exception.AnnotationHandlingRunningException;
import by.bsu.minihibernate.frameworkconfiguration.exception.FrameworkConfigurationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public final class FrameworkConfiguration
{
    private final AnnotationHandlingRunner annotationHandlingRunner;
    private final Set<Class<?>> annotatedClasses;

    public static FrameworkConfiguration createFrameworkConfiguration()
    {
        if(FrameworkConfiguration.frameworkConfiguration == null)
        {
            synchronized(FrameworkConfiguration.class)
            {
                if(FrameworkConfiguration.frameworkConfiguration == null)
                {
                    final AnnotationHandlingRunner annotationHandlingRunner
                            = AnnotationHandlingRunner.createAnnotationHandlingRunner();

                    Class<? extends Annotation> classOfCurrentAnnotation;
                    AnnotationHandler<Class<?>> currentHandlerOfAnnotationOfClass;
                    for(final Map.Entry<Class<? extends Annotation>, AnnotationHandler<Class<?>>> typeOfAnnotationAndItsHandler
                            : FrameworkConfiguration.TYPE_OF_REGISTERED_ANNOTATIONS_FOR_CLASS_AND_ITS_HANDLER.entrySet())
                    {
                        classOfCurrentAnnotation = typeOfAnnotationAndItsHandler.getKey();
                        currentHandlerOfAnnotationOfClass = typeOfAnnotationAndItsHandler.getValue();
                        annotationHandlingRunner.putTypeOfAnnotationOfClassAndItsHandler(classOfCurrentAnnotation,
                                currentHandlerOfAnnotationOfClass);
                    }

                    AnnotationHandler<Field> currentHandlerOfAnnotationOfField;
                    for(final Map.Entry<Class<? extends Annotation>, AnnotationHandler<Field>> typeOfAnnotationAndItsHandler
                            :  FrameworkConfiguration.TYPE_OF_REGISTERED_ANNOTATIONS_FOR_FIELD_AND_ITS_HANDLER.entrySet())
                    {
                        classOfCurrentAnnotation = typeOfAnnotationAndItsHandler.getKey();
                        currentHandlerOfAnnotationOfField = typeOfAnnotationAndItsHandler.getValue();
                        annotationHandlingRunner.putTypeOfAnnotationOfFieldAndItsHandler(classOfCurrentAnnotation,
                                currentHandlerOfAnnotationOfField);
                    }

                    FrameworkConfiguration.frameworkConfiguration = new FrameworkConfiguration(
                            annotationHandlingRunner);
                }
            }
        }
        return FrameworkConfiguration.frameworkConfiguration;
    }

    private static FrameworkConfiguration frameworkConfiguration = null;

    private FrameworkConfiguration(final AnnotationHandlingRunner annotationHandlingRunner)
    {
        this.annotationHandlingRunner = annotationHandlingRunner;
        this.annotatedClasses = new LinkedHashSet<Class<?>>();
    }

    private static final Map<Class<? extends Annotation>, AnnotationHandler<Class<?>>> TYPE_OF_REGISTERED_ANNOTATIONS_FOR_CLASS_AND_ITS_HANDLER
            = new LinkedHashMap<Class<? extends Annotation>, AnnotationHandler<Class<?>>>()
    {
        {
            this.put(Entity.class, EntityAnnotationHandler.createEntityAnnotationHandler());
            this.put(Table.class, TableAnnotationHandler.createTableAnnotationHandler());
        }
    };

    private static final Map<Class<? extends Annotation>, AnnotationHandler<Field>> TYPE_OF_REGISTERED_ANNOTATIONS_FOR_FIELD_AND_ITS_HANDLER
            = new LinkedHashMap<Class<? extends Annotation>, AnnotationHandler<Field>>()
    {
        {
            this.put(Column.class, ColumnAnnotationHandler.createColumnAnnotationHandler());
            this.put(PrimaryKey.class, PrimaryKeyAnnotationHandler.createPrimaryKeyAnnotationHandler());
            this.put(AutoIncrement.class,
                    AutoIncrementPrimaryKeyAnnotationHandler.createAutoIncrementPrimaryKeyAnnotationHandler());
            //this.put(Composite.class, new CompositeAnnotationHandler());
            //this.put(Unique.class, new UniqueAnnotationHandler());
            //this.put(CompositePrimaryKey.class, new CompositePrimaryKeyAnnotationHandler());
        }
    };

    public final void addAnnotatedClass(final Class<?> annotatedClass)
    {
        this.annotatedClasses.add(annotatedClass);
    }

    public final void configure()
            throws FrameworkConfigurationException
    {
        try
        {
            for(final Class<?> annotatedClass : this.annotatedClasses)
            {
                this.annotationHandlingRunner.runAnnotationHandling(annotatedClass);
            }
        }
        catch(final AnnotationHandlingRunningException cause)
        {
            throw new FrameworkConfigurationException(cause);
        }
    }
}
