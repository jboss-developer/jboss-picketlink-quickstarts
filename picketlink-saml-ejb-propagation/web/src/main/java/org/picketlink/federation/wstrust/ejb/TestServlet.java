package org.picketlink.federation.wstrust.ejb;

import org.jboss.security.SecurityContextAssociation;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;
import org.picketlink.identity.federation.core.wstrust.SamlCredential;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Set;

/**
 * <p> Simple test {@code Servlet} that calls methods on a remote EJB3 beans and prints whether the client has access to
 * each method or not in the response. </p>
 *
 * @author <a href="mailto:sguilhen@redhat.com">Stefan Guilhen</a>
 */
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 2195802688711027241L;
    public static final String EJB_JNDI_URL = "jboss-as-picketlink-saml-ejb-propagation/jboss-as-picketlink-saml-ejb-propagation-ejb//EchoServiceImpl!org.picketlink.federation.wstrust.ejb.EchoService";

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // if logout was passed as a get parameter, perform the logout by invalidating the session.
        if (req.getParameter("logout") != null) {
            req.getSession().invalidate();
            resp.sendRedirect("test");
            return;
        }

        invokeEJB(req, resp);
    }

    private void invokeEJB(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
        Context context = createInitialContext();

        PrintWriter out = null;

        try {
            out = resp.getWriter();

            out.println("Hello " + req.getRemoteUser());

            // lookup the EJB
            EchoService object = (EchoService) context.lookup(EJB_JNDI_URL);

            out.println("Calling EJB that requires a SAML assertion credential and a Role STSClient...");
            out.println("Response: " + object.echo("Hi "));

            out.println("");

            out.println("Calling EJB that requires a SAML assertion credential and a Role Manager...");
            out.println("Response: " + object.echoManager("Hi "));
        } catch (EJBAccessException ejbae) {
            out.println("You are not allows to invoke method echoManager.");
        } catch (Exception ne) {
            throw new ServletException("Could not invoke EJB.", ne);
        } finally {
            if (context != null) {
                try {
                    context.close();
                } catch (NamingException e) {
                }
            }

            if (out != null) {
                out.close();
            }
        }
    }

    private Context createInitialContext() throws ServletException {
        // JNDI environment configuration properties
        Hashtable<String, Object> env = new Hashtable<String, Object>();

        env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        env.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
        env.put("java.naming.provider.url", "remote://localhost:4447");
        env.put("jboss.naming.client.ejb.context", "true");
        env.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        env.put("javax.security.sasl.policy.noplaintext", "false");

        SamlCredential samlCredential = getSamlCredential();

        try {
            // provide the user principal and credential. The credential is the previously issued SAML assertion
            env.put(Context.SECURITY_PRINCIPAL, "admin");
            env.put(Context.SECURITY_CREDENTIALS, DocumentUtil.getNodeAsString(samlCredential.getAssertionAsElement()));

            return new InitialContext(env);
        } catch (Exception e) {
            throw new ServletException("Could not create context.", e);
        }
    }

    private SamlCredential getSamlCredential() {
        Subject subject = SecurityContextAssociation.getSubject();

        Set<SamlCredential> credentials = subject.getPublicCredentials(SamlCredential.class);

        if (credentials.isEmpty()) {
            throw new RuntimeException("SAML Credential not found for Subject.");
        }

        return credentials.iterator().next();
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}