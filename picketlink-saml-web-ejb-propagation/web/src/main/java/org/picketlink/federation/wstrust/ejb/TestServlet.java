package org.picketlink.federation.wstrust.ejb;

import org.picketlink.identity.federation.api.saml.v2.sig.SAML2Signature;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;
import org.picketlink.identity.federation.core.util.KeyStoreUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Hashtable;

/**
 * <p> Simple test {@code Servlet} that calls methods on a remote EJB3 beans and prints whether the client has access to
 * each method or not in the response. </p>
 *
 * @author Pedro Igor
 */
public class TestServlet extends HttpServlet {

    public static final String EJB_JNDI_URL = "jboss-as-picketlink-saml-ejb-propagation/jboss-as-picketlink-saml-ejb-propagation-ejb//EchoServiceImpl!org.picketlink.federation.wstrust.ejb.EchoService";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        invokeEJB(req, resp);
    }

    private void invokeEJB(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException {
        Context context = createInitialContext(req);

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
            out.println("You are not allowed to invoke method echoManager.");
        } catch (Exception ne) {
            throw new ServletException("Could not invoke EJB.", ne);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private String getSignedAssertion(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession();

        Document assertion = (Document) session.getAttribute(SAMLConstants.ASSERTION_SESSION_ATTRIBUTE_NAME);

        return DocumentUtil.asString(signAssertion(assertion));
    }

    private Document signAssertion(final Document assertion) {
        Document signedAssertion = null;

        try {
            Element assertionElement = assertion.getDocumentElement();
            SAML2Signature signature = new SAML2Signature();
            InputStream keyStoreIs = getClass().getResourceAsStream("/sts_keystore.jks");

            KeyStore keyStore = KeyStoreUtil.getKeyStore(keyStoreIs, "testpass".toCharArray());

            PrivateKey privateKey = (PrivateKey) keyStore.getKey("sts", "keypass".toCharArray());
            PublicKey publicKey = KeyStoreUtil.getPublicKey(keyStore, "sts", "keypass".toCharArray());

            String id = assertionElement.getAttribute("ID");

            signedAssertion = signature.sign(assertion, id, new KeyPair(publicKey, privateKey));
        } catch (Exception e) {
            throw new RuntimeException("Error signing assertion.", e);
        }
        return signedAssertion;
    }

    private Context createInitialContext(final HttpServletRequest request) throws ServletException {
        // JNDI environment configuration properties
        Hashtable<String, Object> env = new Hashtable<String, Object>();

        env.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
        env.put(Context.PROVIDER_URL, "remote://localhost:4447");
        env.put("jboss.naming.client.ejb.context", "true");
        env.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");
        env.put("javax.security.sasl.policy.noplaintext", "false");

//        SamlCredential samlCredential = getSamlCredential();

        try {
            // provide the user principal and credential. The credential is the previously issued SAML assertion
            env.put(Context.SECURITY_PRINCIPAL, request.getUserPrincipal().getName());
            env.put(Context.SECURITY_CREDENTIALS, getSignedAssertion(request));

            return new InitialContext(env);
        } catch (Exception e) {
            throw new ServletException("Could not create context.", e);
        }
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