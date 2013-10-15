package org.jboss.as.quickstarts.picketlink.authorization.drools;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.deltaspike.security.api.authorization.SecurityBindingType;

/**
 * A security binding type annotation that restricts access to the annotated method
 * to certain groups at certain times of the day
 *
 * @author Shane Bryzak
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@SecurityBindingType
public @interface TimeRestricted {

}
