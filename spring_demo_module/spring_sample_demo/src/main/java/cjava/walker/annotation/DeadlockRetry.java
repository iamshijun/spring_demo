package cjava.walker.annotation;
 
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface DeadlockRetry {
 
    int maxTries() default 21;
    int tryIntervalMillis() default 100;
     
}