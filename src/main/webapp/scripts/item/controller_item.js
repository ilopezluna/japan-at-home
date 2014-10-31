'use strict';

japanAtHomeApp.controller('ItemController', function ($scope, resolvedItem, Item, resolvedProduct) {

        $scope.items = resolvedItem;
        $scope.products = resolvedProduct;

        $scope.create = function () {
            Item.save($scope.item,
                function () {
                    $scope.items = Item.query();
                    $('#saveItemModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.item = Item.get({id: id});
            $('#saveItemModal').modal('show');
        };

        $scope.delete = function (id) {
            Item.delete({id: id},
                function () {
                    $scope.items = Item.query();
                });
        };

        $scope.clear = function () {
            $scope.item = {quantity: null, id: null};
        };
    });
