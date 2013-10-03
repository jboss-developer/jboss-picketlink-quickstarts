<?xml version="1.0" encoding="UTF-8"?>
<!-- XSLT file to add the security domains to the standalone.xml 
	used during the integration tests. -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:as="urn:jboss:domain:1.2" xmlns:sd="urn:jboss:domain:security:1.1"
	version="1.0">

	<xsl:output method="xml" indent="yes" />

	<xsl:template match="//as:profile/sd:subsystem/sd:security-domains/sd:security-domain[@name='idp']" />
	<xsl:template match="//as:profile/sd:subsystem/sd:security-domains/sd:security-domain[@name='sp']" />

	<xsl:template match="as:profile/sd:subsystem/sd:security-domains">
		<security-domains>
			<security-domain name="idp" cache-type="default">
				<authentication>
					<login-module code="UsersRoles" flag="required">
						<module-option name="usersProperties" value="users.properties" />
						<module-option name="rolesProperties" value="roles.properties" />
					</login-module>
				</authentication>
			</security-domain>
			<security-domain name="sp" cache-type="default">
				<authentication>
					<login-module
						code="org.picketlink.identity.federation.bindings.jboss.auth.SAML2LoginModule"
						flag="required" />
				</authentication>
			</security-domain>
			<xsl:apply-templates select="@* | *" />
		</security-domains>
	</xsl:template>

	<!-- Copy everything else. -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>