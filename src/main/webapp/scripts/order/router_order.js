'use strict';

japanAtHomeApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/order', {
                    templateUrl: 'views/orders.html',
                    controller: 'OrderController',
                    resolve:{
                        resolvedOrder: ['Order', function (Order) {
                            return Order.query().$promise;
                        }],
                        resolvedRestaurant: ['Restaurant', function (Restaurant) {
                            return Restaurant.query().$promise;
                        }],
                        resolvedZip: ['Zip', function (Zip) {
                            return Zip.query().$promise;
                        }],
                        resolvedItem: ['Item', function (Item) {
                            return Item.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
