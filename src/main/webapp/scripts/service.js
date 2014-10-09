var app = angular.module('app');

app.service('Auth', function ($rootScope, $location ,CrowdLoginResource, CrowdLogoutResource, $cookieStore, JiraLoginResource) {

    this.authorize = function (credentials, successCallback, failureCallback) {
        var filteredCredentials = {
            username: credentials.username,
            password: credentials.password
        };

        CrowdLoginResource.save(filteredCredentials, function (response) {
            console.log('Authorize response');
            console.log(response);

            if (response.displayName) {
                $cookieStore.put('crowdUser', response);
                
                //connect to jira
                console.log("Connecting to jira");
                JiraLoginResource.save(filteredCredentials, function (response) {
                    console.log("Jira response");
                    if (response[0] == 1) {
                        successCallback();
                    } else {
                        failureCallback();
                    }
                });

            } else {
                failureCallback();
            }
        });
    };

    this.unauthenticate = function (successCallback, failureCallback) {
        if (!this.isAuthenticated()) {
            successCallback();
            return;
        }

        CrowdLogoutResource.save(function(response) {
            console.log('response');
            console.log(response);

            if (response[0] == 1) {
                $cookieStore.remove('crowdUser');
                successCallback();
                //should disconnect with jira
            } else {
                failureCallback();
            }
        });
    };

    this.isAuthenticated = function () {
        return !!this.getUser();
    };

    this.getUser = function () {
        return $cookieStore.get('crowdUser');
    };
});

app.service('PatService', function (PatResource) {
    this.submit = function (data, successCallback, failureCallback) {
        PatResource.save(data, function (response) {
            if (response.$resolved) {
                successCallback();
            } else {
                failureCallback();
            }
        });
    }
});

app.service('UsersService', function (PeopleSearchResource) {
    this.search = function (searchTerm) {
        return PeopleSearchResource.getAll({searchTerm: searchTerm});
    }
});