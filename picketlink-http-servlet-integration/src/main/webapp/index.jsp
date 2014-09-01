<!--
    JBoss, Home of Professional Open Source
    Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
    contributors by the @authors tag. See the copyright.txt in the
    distribution for a full listing of individual contributors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
<!-- Plain HTML page that kicks us into the app -->

<html>
<body>
    <%
        if (request.getUserPrincipal() == null) {
    %>
        <form method="post" action="<%= request.getContextPath() %>/servlet">
            Username: <input type="text" name="username"/><br/>
            Password: <input type="password" name="password"/>
            <br/>
            <br/>
            <input type="submit" value="Ok"/>
        </form>
    <%
        } else {
    %>
        Welcome, <%= request.getUserPrincipal().getName() %> !!
        <br/>
        <br/>
        Are you granted with the User Role ? request.isUserInRole("User") == <%= request.isUserInRole("User") %>.
        <br/>
        Are you an Administrator ? request.isUserInRole("Administrator") == <%= request.isUserInRole("Administrator") %>.
        <br/>
        <br/>
        Click here to <a href="<%= request.getContextPath() %>/servlet?logout=true">logout</a>.
    <%
        }
    %>
</body>
</html>
