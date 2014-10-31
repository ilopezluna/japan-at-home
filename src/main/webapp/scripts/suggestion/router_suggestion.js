'use strict';

japanAtHomeApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/suggestion', {
                    templateUrl: 'views/suggestions.html',
                    controller: 'SuggestionController',
                    resolve:{
                        resolvedSuggestion: ['Suggestion', function (Suggestion) {
                            return Suggestion.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
