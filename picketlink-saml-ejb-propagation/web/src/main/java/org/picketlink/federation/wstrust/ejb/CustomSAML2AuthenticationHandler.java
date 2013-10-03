package org.picketlink.federation.wstrust.ejb;

import org.picketlink.common.exceptions.ProcessingException;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerRequest;
import org.picketlink.identity.federation.core.saml.v2.interfaces.SAML2HandlerResponse;
import org.picketlink.identity.federation.core.saml.v2.util.AssertionUtil;
import org.picketlink.identity.federation.core.saml.v2.util.DocumentUtil;
import org.picketlink.identity.federation.saml.v2.SAML2Object;
import org.picketlink.identity.federation.saml.v2.assertion.AssertionType;
import org.picketlink.identity.federation.saml.v2.protocol.ResponseType;
import org.picketlink.identity.federation.web.core.HTTPContext;
import org.picketlink.identity.federation.web.handlers.saml2.SAML2AuthenticationHandler;

import java.util.List;

import static org.picketlink.federation.wstrust.ejb.SAMLConstants.*;

/**
 * <p>Custom implementation of {@link SAML2AuthenticationHandler}.</p> <p/> <p>This class is only necessary to get the
 * SAML assertion properly stored into the session. For PicketLink 2.1.8 and beyond, this is done automatically.</p>
 *
 * @author Pedro Igor
 */
public class CustomSAML2AuthenticationHandler extends SAML2AuthenticationHandler {

    @Override
    public void handleStatusResponseType(final SAML2HandlerRequest request, final SAML2HandlerResponse response) throws ProcessingException {
        super.handleStatusResponseType(request, response);
        storeAssertionIntoSession(request);
    }

    private void storeAssertionIntoSession(final SAML2HandlerRequest request) {
        HTTPContext httpContext = (HTTPContext) request.getContext();
        SAML2Object saml2Object = request.getSAML2Object();

        if (ResponseType.class.isInstance(saml2Object)) {
            ResponseType responseType = (ResponseType) saml2Object;
            List<ResponseType.RTChoiceType> assertions = responseType.getAssertions();

            AssertionType assertion = assertions.get(0).getAssertion();

            try {
                httpContext.getRequest().getSession().setAttribute(ASSERTION_SESSION_ATTRIBUTE_NAME, DocumentUtil.getDocument(AssertionUtil.asString(assertion)));
            } catch (Exception e) {
                throw new RuntimeException("Error storing assertion into session.", e);
            }
        }
    }
}
