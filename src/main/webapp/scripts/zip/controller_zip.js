'use strict';

japanAtHomeApp.controller('ZipController', function ($scope, resolvedZip, Zip) {

        $scope.zips = resolvedZip;

        $scope.create = function () {
            Zip.save($scope.zip,
                function () {
                    $scope.zips = Zip.query();
                    $('#saveZipModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.zip = Zip.get({id: id});
            $('#saveZipModal').modal('show');
        };

        $scope.delete = function (id) {
            Zip.delete({id: id},
                function () {
                    $scope.zips = Zip.query();
                });
        };

        $scope.clear = function () {
            $scope.zip = {code: null, id: null};
        };
    });
