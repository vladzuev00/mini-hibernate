package by.bsu.minihibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PrimaryKey
{
    public static final String PREFIX_OF_NAME_OF_PRIMARY_KEY_CONSTRAINT = "primary_key";
}
