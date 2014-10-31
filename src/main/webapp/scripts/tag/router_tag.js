'use strict';

japanAtHomeApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/tag', {
                    templateUrl: 'views/tags.html',
                    controller: 'TagController',
                    resolve:{
                        resolvedTag: ['Tag', function (Tag) {
                            return Tag.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
