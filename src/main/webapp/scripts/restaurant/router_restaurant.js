'use strict';

japanAtHomeApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/restaurant', {
                    templateUrl: 'views/restaurants.html',
                    controller: 'RestaurantController',
                    resolve:{
                        resolvedRestaurant: ['Restaurant', function (Restaurant) {
                            return Restaurant.query().$promise;
                        }],
                        resolvedZip: ['Zip', function (Zip) {
                            return Zip.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
