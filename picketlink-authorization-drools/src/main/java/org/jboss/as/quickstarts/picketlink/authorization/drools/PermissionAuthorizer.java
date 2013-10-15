package org.jboss.as.quickstarts.picketlink.authorization.drools;

import java.io.InputStream;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.deltaspike.security.api.authorization.Secures;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.cdi.KSession;
import org.kie.api.io.KieResources;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.picketlink.Identity;
import org.picketlink.idm.drools.PermissionCheck;

/**
 * Performs the business logic required for the declared security binding annotations
 *
 * @author Shane Bryzak
 *
 */
@ApplicationScoped
public class PermissionAuthorizer {

//    private static final String SECURITY_RULES = "/META-INF/security-rules.drl";

    @Inject
    @KSession("ksession1")
    KieSession kSession;

    //private KieBase kBase;
    //private KieContainer kContainer;

    public PermissionAuthorizer() {
        //KieServices kServices = KieServices.Factory.get();

        /*KieResources kieResources = kieServices.getResources();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        InputStream in = getClass().getResourceAsStream(SECURITY_RULES);
        String path = "src/main/resources/optaplanner-kie-namespace/" + SECURITY_RULES;
        kieFileSystem.write(path, kieResources.newInputStreamResource(in, "UTF-8"));

        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            StringBuilder sb = new StringBuilder(); 
            for (Message msg : results.getMessages(Message.Level.ERROR)) {
                sb.append(msg.getText());
            }
            throw new RuntimeException("Error parsing security rules: " + sb.toString());
        }

        KieContainer kieContainer = kieServices.newKieContainer(kieBuilder.getKieModule().getReleaseId());
        */

        //KieContainer kContainer = kServices.getKieClasspathContainer();
        //kContainer = KieServices.Factory.get().getKieClasspathContainer();

//        kBase = kc.getKieBase("security");
        //KieSession ks = kc.newKieSession("ksession1");

        //KieBaseConfiguration kieBaseConfiguration = kieServices.newKieBaseConfiguration();
        //kBase = kieContainer.newKieBase(kieBaseConfiguration);
    }

    @Secures
    @TimeRestricted
    public boolean testTimeRestricted(Identity identity) {
        //return identity.hasPermission("TestAction", "invoke");

        PermissionCheck check = new PermissionCheck(identity.getAccount(), "TestAction", "invoke");

        //KieSession session = kContainer.newKieSession();
        //KieSession session = kBase.newKieSession();

        kSession.insert(check);
        kSession.fireAllRules();

        return check.isGranted();
    }
}
