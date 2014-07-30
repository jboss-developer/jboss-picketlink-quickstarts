package org.jboss.as.quickstarts.picketlink.angularjs.util;

import org.picketlink.annotations.PicketLink;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 * 
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {

    /**
     * Alias the persistence context
     */
    // use @SuppressWarnings to tell IDE to ignore warnings about field not being referenced directly
   @Produces
   @PersistenceContext
   private EntityManager em;

    @PersistenceContext
    @Produces
    @PicketLink
    private EntityManager entityManager;
}
