var app = angular.module('app');

app.controller('RootCtrl', function ($scope, $location, Auth) {
    $scope.isAuthenticated = false;

    $scope.logout = function () {
        Auth.unauthenticate(function() {
            console.log("logged out");
            $location.path('/login');
        },
        function() {
            console.log('Logout failed!');
        });
    };

    $scope.$watch(function () {
        return Auth.isAuthenticated();
    },
    function (newValue) {
        $scope.isAuthenticated = newValue;
    });
});

app.controller('LoginCtrl', function ($scope, $location, Auth) {
    $scope.credentials = {
        submitted: false,
        submitBtnActive: true
    };

    $scope.submitLoginForm = function () {
        $scope.credentials.submitBtnActive = false;

        Auth.authorize($scope.credentials, function () {
            $scope.successAuth = true;
            $location.path('/form');
        }, function () {
            $scope.credentials.submitted = true;
            $scope.successAuth = false;
            $scope.credentials.submitBtnActive = true;
        });
    };
});

app.controller('SubmittedCtrl', function ($scope, $location, Auth) {
    $scope.logout = function () {
        Auth.unauthenticate(function() {
            $location.path('/login');
        },
        function() {
            console.log('Logout failed!');
        });
    };
});

app.controller('FormCtrl', function ($scope, $q, $location, UsersService, Auth, PatService) {
    var authenticatedUser = Auth.getUser();

    $scope.people = [];
    $scope.form = {
        fromName: authenticatedUser['firstName'] + ' ' + authenticatedUser['lastName'],
        fromUser: authenticatedUser.name,
        toName: '',
        toUser: '',
        description: ''
    };

    $scope.searchPeople = function (searchTerm) {
        if (searchTerm.length >= 1) {
            $scope.people = UsersService.search(searchTerm);
        } else {
            $scope.people = [];
        }
    };

    $scope.selectPerson = function(item) {
        $scope.term = item.name;
        $scope.form.toName = item.name;
        $scope.form.toUser = item.username;
    };

    $scope.imageSource = function(username) {
        if (!username) {
            username = 'default';
        }
        return 'rest/user/' + username; //+ '/thumbnail';
    };

    $scope.showResults = function() {
        return $scope.people.length > 0;
    };


    $scope.submitForm = function () {
        PatService.submit($scope.form, function(data) {
            $location.path('/submitted');
        }, function () {
            alert('An unknown problem occurred! Please try again');
        });
    };
});