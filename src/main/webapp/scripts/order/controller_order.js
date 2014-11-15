'use strict';

japanAtHomeApp.controller('OrderController', function ($scope, resolvedOrder, Order, resolvedRestaurant, resolvedZip, resolvedItem) {

        $scope.orders = resolvedOrder;
        $scope.restaurants = resolvedRestaurant;
        $scope.zips = resolvedZip;
        $scope.items = resolvedItem;

        $scope.create = function () {
            Order.save($scope.order,
                function () {
                    $scope.orders = Order.query();
                    $('#saveOrderModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.order = Order.get({id: id});
            $('#saveOrderModal').modal('show');
        };

        $scope.delete = function (id) {
            Order.delete({id: id},
                function () {
                    $scope.orders = Order.query();
                });
        };

        $scope.clear = function () {
            $scope.order = {createdAt: null, address: null, code: null, paymentType: null, status: null, id: null};
        };
    });
