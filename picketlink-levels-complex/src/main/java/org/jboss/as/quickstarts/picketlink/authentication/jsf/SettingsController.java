package org.jboss.as.quickstarts.picketlink.authentication.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.picketlink.authentication.model.UserSettings;

/**
 * @author Michal Trnka
 */
@Named
@SessionScoped
public class SettingsController implements Serializable{

    private static final long serialVersionUID = 1L;
    private String address;
    private String ip;

    @Inject
    UserSettings settings;

    @PostConstruct
    public void setUp(){
        address = settings.getAddress();
        ip = settings.getIp();
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public void save(){
        settings.setAddress(address);
        settings.setIp(ip);
    }
}
