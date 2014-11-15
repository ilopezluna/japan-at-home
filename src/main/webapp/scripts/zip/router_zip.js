'use strict';

japanAtHomeApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/zip', {
                    templateUrl: 'views/zips.html',
                    controller: 'ZipController',
                    resolve:{
                        resolvedZip: ['Zip', function (Zip) {
                            return Zip.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
