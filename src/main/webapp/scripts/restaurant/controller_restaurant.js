'use strict';

japanAtHomeApp.controller('RestaurantController', function ($scope, resolvedRestaurant, Restaurant, resolvedZip) {

        $scope.restaurants = resolvedRestaurant;
        $scope.zips = resolvedZip;

        $scope.create = function () {
            Restaurant.save($scope.restaurant,
                function () {
                    $scope.restaurants = Restaurant.query();
                    $('#saveRestaurantModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.restaurant = Restaurant.get({id: id});
            $('#saveRestaurantModal').modal('show');
        };

        $scope.delete = function (id) {
            Restaurant.delete({id: id},
                function () {
                    $scope.restaurants = Restaurant.query();
                });
        };

        $scope.clear = function () {
            $scope.restaurant = {name: null, shortName: null, description: null, address: null, price: null, minPrice: null, status: null, telephone: null, closeAt: null, openAt: null, dayClosed: null, averageDeliveryTime: null, logo: null, id: null};
        };
    });
