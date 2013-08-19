/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
$.ajaxSetup({
	error : function(xhr, textStatus, errorThrown) {
	    alert("Status from server: " + errorThrown);
    }
});

function performUsernamePasswordLogin() {
	var username = $("#userName");
	var password = $("#password");
	
	if (username.is(":valid") && password.is(":valid")) {
		$.ajax({
            url: ("rest/authenticate"),
            type:"POST",
            dataType:"json",
            success: function(response) {
                if (response) {
                    $("#user-message").empty().append("Welcome <b>" + response.account.loginName + "</b> !");
                    $("#authenticatedUserContent").show();
                    $("#loginForms").hide();

                    for (i = 0; i < response.roles.length; i++) {
                        var roleName = response.roles[i].name;

                        if (roleName != 'ADMINISTRATOR') {
                            $("#systemAdministrationLink").text("System Administration is disabled, but you can try to hack and see what happens.");

                            if (roleName == 'DEVELOPER') {
                                $("#risksManagementLink").text("Risks Management is disabled, but you can try to hack and see what happens.");
                            }
                        }
                    }
                } else {
                    $("#login-message").empty().append("Invalid credentials. Please try again.");
                    $("#loginForms").show();
                }
            },
            contentType:"application/x-authc-username-password+json",
            data:JSON.stringify({ userId: username.val(), password: password.val()})
            }
		);
	}
}

function risksManagement() {
    $.ajax({url: ("rest/risksManagement"),
            type:"GET",
            success: function(context) {
                alert(context);
            }}
    );
}

function timesheet() {
    $.ajax({url: ("rest/timesheet"),
            type:"GET",
            success: function(context) {
                alert(context);
            }}
    );
}

function systemAdministration() {
    $.ajax({url: ("rest/systemAdministration"),
            type:"GET",
            success: function(context) {
                alert(context);
            }}
    );
}

function performLogout() {
	$.ajax({url: ("rest/logout"),
		type:"POST",
		dataType:"json",
		contentType:"application/json",
		success: function(context) {
            alert("You're now logged out.");
            window.location = '';
		}}
	);
}

$(document).ready(
    function() {
        $("#authenticatedUserContent").hide();
        $("#login-message").empty().append("");
        $("#user-message").empty().append("");

        $("#usernamePasswordBtn").click(function() {
            performUsernamePasswordLogin();
        });

        $("#logoutBtn").click(function() {
            performLogout();
        });

        $("#risksManagementLink").click(function() {
            risksManagement();
        });

        $("#timesheetLink").click(function() {
            timesheet();
        });

        $("#systemAdministrationLink").click(function() {
            systemAdministration();
        });
    }
);
