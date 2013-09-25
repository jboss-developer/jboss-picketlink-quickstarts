package org.picketlink.federation.wstrust.ejb;

import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

/**
 */
@Stateless
@Remote(EchoService.class)
@SecurityDomain("picketlink-saml-ejb-propagation-ejb")
public class EchoServiceImpl implements EchoService {

    @Resource
    private SessionContext sessionContext;

    @RolesAllowed("STSClient")
    public String echo(String echo) {
        return echo + sessionContext.getCallerPrincipal().getName();
    }

    @RolesAllowed("Manager")
    public String echoManager(String echo) {
        return echo + sessionContext.getCallerPrincipal().getName() + " is managing things. Echo: " + echo;
    }
}
