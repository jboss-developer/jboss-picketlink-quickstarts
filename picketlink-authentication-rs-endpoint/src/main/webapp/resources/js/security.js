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
    url: ("rest/authenticate"),
    type:"POST",
    dataType:"json",
    success: function(user) {
        if (user) {
            $("#message").empty().append("Welcome " + user.loginName + " !");
            $("#logoutBtn").show();
            $("#loginForms").hide();
        } else {
            $("#message").empty().append("Invalid credentials. Please try again.");
            $("#loginForms").show();
        }
    },
	error : function(xhr, textStatus, errorThrown) {
	    alert("Could not authenticate.")
    }
});

function performUsernamePasswordLogin() {
	var username = $("#userName");
	var password = $("#password");
	
	if (username.is(":valid") && password.is(":valid")) {
		$.ajax({
            contentType:"application/x-authc-username-password+json",
            data:JSON.stringify({ userId: username.val(), password: password.val()})
            }
		);
	}
}

function performTokenBasedLogin() {
    var token = $("#token");

    if (token.is(":valid")) {
        $.ajax({
            contentType:"application/x-authc-token",
            data: token.val()
            }
        );
    }
}

function performLogout() {
	$.ajax({url: ("rest/logout"),
		type:"POST",
		dataType:"json",
		contentType:"application/json",
		success: function(context) {
            alert("You're now logged out.");
            $("#loginForms").show();
            $("#logoutBtn").hide();
            $("#message").empty().append("");
		}}
	);
}

$(document).ready(
    function() {
        $("#logoutBtn").hide();

        $("#usernamePasswordBtn").click(function() {
            performUsernamePasswordLogin();
        });

        $("#tokenBtn").click(function() {
            performTokenBasedLogin();
        });

        $("#logoutBtn").click(function() {
            performLogout();
        });
    }
);
