var app = angular.module('app', ['ngRoute', 'ngResource', 'ngCookies']);

app.config(function ($routeProvider) {
    $routeProvider
        .when(
        '/form', {
            templateUrl: 'partials/form.html',
            controller: 'FormCtrl',
            authenticate: true
        })
        .when(
        '/submitted', {
            templateUrl: 'partials/submitted.html',
            controller: 'SubmittedCtrl',
            authenticate: true
        })
        .when(
        '/login', {
            templateUrl: 'partials/login.html',
            controller: 'LoginCtrl'
        })
        .otherwise({
            redirectTo: '/login'
        });
});

app.run(function ($rootScope, $location, Auth) {
    $rootScope.$on('$routeChangeStart', function (event, next) {

        if (next.authenticate && !Auth.isAuthenticated()) {
            console.log('Trying to access protected route: ' + next.$$route.originalPath);
            $location.path('login');
        }
    });
});

app.factory('CrowdLoginResource', function ($resource) {
    return $resource('rest/crowdLogin');
});

app.factory('CrowdLogoutResource', function ($resource) {
    return $resource('rest/secure/crowdLogout');
});

app.factory('JiraLoginResource', function ($resource) {
    return $resource('rest/secure/jiraLogin');
});

app.factory('PatResource', function ($resource) {
    return $resource('rest/secure/createIssue');
});


app.factory('PeopleSearchResource', function ($resource) {
    return $resource('rest/secure/people/search/:searchTerm', null,
        {
            getArray: {
                method:'GET',
                isArray: true
            }
        }
    )
});