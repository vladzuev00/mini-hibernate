package by.bsu.minihibernate.annotationhandler;

import by.bsu.minihibernate.annotationhandler.exception.AnnotationHandlingException;

import java.lang.reflect.AnnotatedElement;
import java.sql.Connection;

@FunctionalInterface
public interface AnnotationHandler<TypeOfAnnotatedElement extends AnnotatedElement>
{
    public abstract void handleAnnotation(final TypeOfAnnotatedElement annotatedElement,
                                          final Connection connectionToDataBase)
            throws AnnotationHandlingException;
}
