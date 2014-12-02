package org.jboss.as.quickstarts.picketlink.authentication.model;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.picketlink.authorization.annotations.RequiresLevel;

/**
 * <p>Entity representing userSettings.</p>
 *
 * <p>Because that example has just one user this entity is created ApplicationScoped without having own manager.</p>
 *
 * @author Michal Trnka
 */
@ApplicationScoped
public class UserSettings {
    private String ip;

    private String address;

    @PostConstruct
    public void setUp(){
        address = "Green avenue 3124, Kingstown, 33890, Awesome Republic";
        ip = "0.0.0.0";
    }

    public String getAddress() {
        return address;
    }

    @RequiresLevel("3")
    public void setAddress(String address) {
        this.address = address;
    }

    @RequiresLevel("3")
    public void setIp(String ip) {
        this.ip = ip.trim();
    }

    public String getIp(){
        return ip;
    }
}
