/*
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
// Define top level routes for the app, security related views are declated in the security.js
// Note that this app is a single page app, and each partial is routed to using the URL fragment. For example, to select the 'home' route, the URL is http://localhost:8080/Project/#/home
var appModule = angular.module('PLAngular',
	[ 'MessageModule', 'PicketLinkSecurityModule']).config(
	[ '$routeProvider', function($routeProvider) {
	    $routeProvider.when('/home', {
		templateUrl : 'partials/home.html',
		controller : HomeCtrl,
		isFree : false
	    // Add a default route
	    }).when('/', {
		templateUrl : 'partials/login.html',
		controller : 'LoginCtrl',
		access : {
		    isFree : true
		}
	    }).when('/about', {
		templateUrl : 'partials/about.html',
		access : {
		    isFree : true
		}
	    }).when('/contact', {
		templateUrl : 'partials/contact.html',
		access : {
		    isFree : true
		}
	    }).otherwise({
		redirectTo : 'login'
	    });
	} ]).factory('authHttpResponseInterceptor', ['$q', '$rootScope', '$location', 'SecurityService', 'MessageService', function($q, $rootScope, $location, SecurityService, MessageService) {
	    return {
		'request' : function(config) {
		    SecurityService.secureRequest(config);
		    return config || $q.when(config);
		},

		'response' : function(response) {
		    return response || $q.when(response);
		},
		
		'responseError' : function(rejection) {
                    console.log("Server Response Status: " + rejection.status);
                    console.log(rejection);
    
                    if (rejection.data && rejection.data.message) {
                        MessageService.setMessages(rejection.data.message);
                    } else {
                        MessageService.setMessages(["Unexpected error from server."]);
                    }
        
                    if (rejection.status === 401) {
                        console.log("[INFO] Unauthorized response.");
                        SecurityService.endSession();
                        $location.path('/login');
                        MessageService.setMessages(["Please, provide your credentials."]);
                    } else if (rejection.status == 400) {
                        console.log("[ERROR] Bad request response from the server.");
                    } else if (rejection.status == 500) {
                        console.log("[ERROR] Internal server error.");
                    } else {
                        console.log("[ERROR] Unexpected error from server.");
                    }

		    return $q.reject(rejection);
		}
	    }
	} ]).config([ '$httpProvider', function($httpProvider) {
            //Http Intercpetor to check auth failures for xhr requests
            $httpProvider.interceptors.push('authHttpResponseInterceptor');
        } ]).run(function($rootScope, $location, MessageService) {

            // register listener to watch route changes
            $rootScope.$on("$routeChangeStart", function(event, next, current) {
                MessageService.clearMessages();
            });
});

appModule.controller('MessageCtrl', function(MessageService, $scope) {
    $scope.hasMessages = function() {
        return MessageService.hasMessages();
    };

    $scope.clearMessages = function() {
        MessageService.clearMessages();
    };
    
});
