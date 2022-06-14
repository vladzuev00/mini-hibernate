package by.bsu.minihibernate.annotationhandlingrunner;

import by.bsu.minihibernate.annotationhandler.AnnotationHandler;
import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;
import by.bsu.minihibernate.annotationhandlingrunner.exception.AnnotationHandlingRunningException;
import by.bsu.minihibernate.databaseconnectionpool.DataBaseConnectionPool;
import by.bsu.minihibernate.databaseconnectionpool.exception.DataBaseConnectionPoolAccessConnectionException;
import by.bsu.minihibernate.fieldsofclassfounder.FieldsOfClassFounder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

public final class AnnotationHandlingRunner
{
    private final DataBaseConnectionPool dataBaseConnectionPool;
    private final Map<Class<? extends Annotation>, AnnotationHandler<Class<?>>> typesOfAnnotationsOfClassesAndTheirHandlers;
    private final RunnerAnnotationHandlingOfField fieldMapperToDataBase;
    private final FieldsOfClassFounder fieldsOfClassFounder;

    public static AnnotationHandlingRunner createAnnotationHandlingRunner()
    {
        if(AnnotationHandlingRunner.annotationHandlingRunner == null)
        {
            synchronized(AnnotationHandlingRunner.class)
            {
                if(AnnotationHandlingRunner.annotationHandlingRunner == null)
                {
                    AnnotationHandlingRunner.annotationHandlingRunner = new AnnotationHandlingRunner();
                }
            }
        }
        return AnnotationHandlingRunner.annotationHandlingRunner;
    }

    private static AnnotationHandlingRunner annotationHandlingRunner = null;

    private AnnotationHandlingRunner()
    {
        super();
        this.dataBaseConnectionPool = DataBaseConnectionPool.createDataBaseConnectionPool();
        this.typesOfAnnotationsOfClassesAndTheirHandlers
                = new HashMap<Class<? extends Annotation>, AnnotationHandler<Class<?>>>();
        this.fieldMapperToDataBase = new RunnerAnnotationHandlingOfField();
        this.fieldsOfClassFounder = FieldsOfClassFounder.createFieldsOfClassFounder();
    }

    public final void putTypeOfAnnotationOfClassAndItsHandler(final Class<? extends Annotation> typeOfAnnotation,
                                                              final AnnotationHandler<Class<?>> annotationHandler)
    {
        this.typesOfAnnotationsOfClassesAndTheirHandlers.put(typeOfAnnotation, annotationHandler);
    }

    public final void putTypeOfAnnotationOfFieldAndItsHandler(final Class<? extends Annotation> typeOfAnnotation,
                                                              final AnnotationHandler<Field> annotationHandler)
    {
        this.fieldMapperToDataBase.putTypeOfAnnotationOfFieldAndItsHandler(typeOfAnnotation, annotationHandler);
    }

    public final void runAnnotationHandling(final Class<?> annotatedClass)
            throws AnnotationHandlingRunningException
    {
        Connection connectionToDataBase = null;
        try
        {
            connectionToDataBase = this.dataBaseConnectionPool.findAvailableConnection();
            final Annotation[] annotationsOfClass = annotatedClass.getAnnotations();
            Class<? extends Annotation> typeOfCurrentAnnotation;
            AnnotationHandler<Class<?>> currentAnnotationHandler;
            for(final Annotation annotationOfClass : annotationsOfClass)
            {
                typeOfCurrentAnnotation = annotationOfClass.annotationType();
                currentAnnotationHandler = this.typesOfAnnotationsOfClassesAndTheirHandlers.get(
                        typeOfCurrentAnnotation);
                currentAnnotationHandler.handleAnnotation(annotatedClass, connectionToDataBase);
            }

            final Set<Field> fieldsOfMappedClass = this.fieldsOfClassFounder.findFieldsOfType(annotatedClass);
            for(final Field fieldOfMappedClass : fieldsOfMappedClass)
            {
                this.fieldMapperToDataBase.runAnnotationHandling(fieldOfMappedClass, connectionToDataBase);
            }
        }
        catch(final DataBaseConnectionPoolAccessConnectionException | AnnotationHandlingException
                | AnnotationHandlingRunningException cause)
        {
            throw new AnnotationHandlingRunningException(cause);
        }
        finally
        {
            if(connectionToDataBase != null)
            {
                this.dataBaseConnectionPool.returnConnectionToPool(connectionToDataBase);
            }
        }
    }

    private static final class RunnerAnnotationHandlingOfField
    {
        private final Map<Class<? extends Annotation>, AnnotationHandler<Field>> typesOfAnnotationsOfFieldsAndTheirHandlers;

        public RunnerAnnotationHandlingOfField()
        {
            super();
            this.typesOfAnnotationsOfFieldsAndTheirHandlers
                    = new LinkedHashMap<Class<? extends Annotation>, AnnotationHandler<Field>>(); //TODO: заменить на HashMap если будет возможно
        }

        public final void putTypeOfAnnotationOfFieldAndItsHandler(final Class<? extends Annotation> typeOfAnnotation,
                                                                  final AnnotationHandler<Field> annotationHandler)
        {
            this.typesOfAnnotationsOfFieldsAndTheirHandlers.put(typeOfAnnotation, annotationHandler);
        }

        public final void runAnnotationHandling(final Field mappedField, final Connection connectionToDataBase)
                throws AnnotationHandlingRunningException
        {
            try
            {
                final Annotation[] annotationsOfField = mappedField.getAnnotations();
                Class<? extends Annotation> typeOfCurrentAnnotation;
                AnnotationHandler<Field> currentAnnotationHandler;
                for(final Annotation annotationOfField : annotationsOfField)
                {
                    typeOfCurrentAnnotation = annotationOfField.annotationType();
                    currentAnnotationHandler = this.typesOfAnnotationsOfFieldsAndTheirHandlers.get(
                            typeOfCurrentAnnotation);
                    currentAnnotationHandler.handleAnnotation(mappedField, connectionToDataBase);
                }
            }
            catch(final AnnotationHandlingException cause)
            {
                throw new AnnotationHandlingRunningException(cause);
            }
        }
    }
}
