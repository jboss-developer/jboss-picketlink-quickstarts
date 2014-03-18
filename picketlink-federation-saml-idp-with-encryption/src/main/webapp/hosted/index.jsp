<!--
~ JBoss, Home of Professional Open Source.
~ Copyright (c) 2011, Red Hat, Inc., and individual contributors
~ as indicated by the @author tags. See the copyright.txt file in the
~ distribution for a full listing of individual contributors.
~
~ This is free software; you can redistribute it and/or modify it
~ under the terms of the GNU Lesser General Public License as
~ published by the Free Software Foundation; either version 2.1 of
~ the License, or (at your option) any later version.
~
~ This software is distributed in the hope that it will be useful,
~ but WITHOUT ANY WARRANTY; without even the implied warranty of
~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
~ Lesser General Public License for more details.
~
~ You should have received a copy of the GNU Lesser General Public
~ License along with this software; if not, write to the Free
~ Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
~ 02110-1301 USA, or see the FSF site: http://www.fsf.org.
-->
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
    <title>Welcome to PicketLink Identity Provider</title>
    <link rel="StyleSheet" href="<%= request.getContextPath() %>/css/idp.css" type="text/css">
</head>

<body>
<img src="<%= request.getContextPath() %>/images/picketlink-banner-1180px.png"
     style="margin-top: -10px; margin-left: -10px; opacity: 0.4; filter: alpha(opacity = 40);" />
    <p>
        Welcome to the <b>PicketLink Identity Provider !</b>
    </p>
    <p>
        <ul>
            <li>SAML 2 Unsolicited Response: <a id="saml_2_sales_link" href="?SAML_VERSION=2.0&TARGET=http%3A//localhost%3a8080/sales-post-enc/">Sales SAML 2.0</a> (<i>make sure you have deployed the sales-post-sig application</i>)</li>
        </ul>
    </p>
</div>
</body>
</html>