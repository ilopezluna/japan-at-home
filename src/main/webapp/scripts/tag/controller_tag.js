'use strict';

japanAtHomeApp.controller('TagController', function ($scope, resolvedTag, Tag) {

        $scope.tags = resolvedTag;

        $scope.create = function () {
            Tag.save($scope.tag,
                function () {
                    $scope.tags = Tag.query();
                    $('#saveTagModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.tag = Tag.get({id: id});
            $('#saveTagModal').modal('show');
        };

        $scope.delete = function (id) {
            Tag.delete({id: id},
                function () {
                    $scope.tags = Tag.query();
                });
        };

        $scope.clear = function () {
            $scope.tag = {name: null, id: null};
        };
    });
