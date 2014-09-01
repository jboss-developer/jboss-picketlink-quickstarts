package org.jboss.as.quickstarts.picketlink.authentication.bussiness;

import javax.inject.Named;

import org.picketlink.authentication.levels.Level;
import org.picketlink.authentication.levels.internal.DefaultLevel;

/**
 * <p> Class to compare the level to the given int. It is used in JSF </p>
 *
 * @author Michal Trnka
 */
@Named
public class LevelComparator {

    public boolean hasLevel(Level l, int i){
        if(l.compareTo(new DefaultLevel(i))<0){
            return false;
        }return true;
    }
}
