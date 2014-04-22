package com.gr.project.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@ApplicationScoped
public class StringResourceProducer {

	@Produces @Named("default.encoding")
	public String getDefaultEncoding() {
		return "UTF-8";
	}
	
	@Produces @Named("default.return.message.parameter")
	public String getMessageReturnParameter() {
		return "message";
	}
	
	@Produces @Named("resource.wildcard")
	public String getWildcardResource() {
		return "*";
	}
	
	@Produces @Named("ACTIVATION_CODE_ATTRIBUTE_NAME")
	public String getActivatinAttribute() {
		return "ActivationCode";
	}
}
