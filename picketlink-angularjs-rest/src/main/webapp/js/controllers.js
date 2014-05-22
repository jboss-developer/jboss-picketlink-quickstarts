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
function HomeCtrl($scope, UsersResource, $location, LogoutResource, SecurityService, AdminResource, MessageService) {
    
    // Define a refresh function, that updates the data from the REST service
    $scope.refresh = function() {
        $scope.users = UsersResource.query();
    };

    // Define a reset function, that clears the prototype newMember object, and
    // consequently, the form
    $scope.reset = function() {
        // clear input fields
        $scope.newUser = {};
    };

    $scope.logout = function() {
	    LogoutResource.logout(function(resp) {
            SecurityService.endSession();
	        $location.path( "/login" );
	    });
    };
    
    $scope.enableAccount = function(rowData) {
	    AdminResource.enableAccount(rowData.user, function(resp) {
            MessageService.setMessages(resp.message)
	    });
    };

    $scope.disableAccount = function(rowData) {
        AdminResource.disableAccount(rowData.user, function(resp) {
            MessageService.setMessages(resp.message)
        });
    };

    // Call the refresh() function, to populate the list of members
    $scope.refresh();

    // Initialize newMember here to prevent Angular from sending a request
    // without a proper Content-Type.
    $scope.reset();

    // Set the default orderBy to the name property
    $scope.orderBy = 'name';
}
