var app = angular.module('app');

app.directive('typeahead', ["$timeout", function ($timeout) {
    return {
        restrict: 'E',
        transclude: true,
        replace: true,
        template: '<div><input class="form-control typeahead" placeholder="To" name="to" ng-model="term" ng-change="query()" type="text" autocomplete="off" /><div ng-transclude></div></div>',
        scope: {
            search: "&",
            select: "&",
            items: "=",
            term: "="
        },
        controller: ["$scope", function ($scope) {
            $scope.items = [];
            $scope.hide = false;

            this.activate = function (item) {
                $scope.active = item;
            };

            this.activateNextItem = function () {
                var index = $scope.items.indexOf($scope.active);
                this.activate($scope.items[(index + 1) % $scope.items.length]);
            };

            this.activatePreviousItem = function () {
                var index = $scope.items.indexOf($scope.active);
                this.activate($scope.items[index === 0 ? $scope.items.length - 1 : index - 1]);
            };

            this.isActive = function (item) {
                return $scope.active === item;
            };

            this.selectActive = function () {
                this.select($scope.active);
            };

            this.select = function (item) {
                $scope.items = [];
                $scope.select({item: item});
            };

            $scope.query = function () {
                $scope.search({term: $scope.term});
            };
        }],

        link: function (scope, element, attrs, controller) {
            var $input = element.find('input');

            $input.bind('keyup', function (e) {
                if (e.keyCode === 9 || e.keyCode === 13) {
                    scope.$apply(function () {
                        controller.selectActive();
                    });
                }

                if (e.keyCode === 27) {
                    scope.$apply(function () {
                        scope.items = [];
                    });
                }
            });

            $input.bind('keydown', function (e) {
                if (e.keyCode === 13 || e.keyCode === 27) {
                    e.preventDefault();
                }

                if (e.keyCode === 9) {
                    scope.$apply(function () {
                        scope.items = [];
                    });
                }

                if (e.keyCode === 40) {
                    e.preventDefault();
                    scope.$apply(function () {
                        controller.activateNextItem();
                    });
                }

                if (e.keyCode === 38) {
                    e.preventDefault();
                    scope.$apply(function () {
                        controller.activatePreviousItem();
                    });
                }
            });
        }
    };
}]);

app.directive('typeaheadItem', function () {
    return {
        require: '^typeahead',
        link: function (scope, element, attrs, controller) {

            var item = scope.$eval(attrs.typeaheadItem);

            scope.$watch(function () {
                return controller.isActive(item);
            },
            function (active) {
                if (active) {
                    element.addClass('active');
                } else {
                    element.removeClass('active');
                }
            });

            element.bind('mouseenter', function (e) {
                scope.$apply(function () {
                    controller.activate(item);
                });
            });

            element.bind('mouseleave', function (e) {
                console.log("Mouseleave");
            });

            element.bind('click', function (e) {
                scope.$apply(function () {
                    controller.select(item);
                });
            });
        }
    };
});