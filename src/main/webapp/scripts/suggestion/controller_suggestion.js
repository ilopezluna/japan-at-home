'use strict';

japanAtHomeApp.controller('SuggestionController', function ($scope, resolvedSuggestion, Suggestion) {

        $scope.suggestions = resolvedSuggestion;

        $scope.create = function () {
            Suggestion.save($scope.suggestion,
                function () {
                    $scope.suggestions = Suggestion.query();
                    $('#saveSuggestionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.suggestion = Suggestion.get({id: id});
            $('#saveSuggestionModal').modal('show');
        };

        $scope.delete = function (id) {
            Suggestion.delete({id: id},
                function () {
                    $scope.suggestions = Suggestion.query();
                });
        };

        $scope.clear = function () {
            $scope.suggestion = {email: null, description: null, status: null, createdAt: null, id: null};
        };
    });
