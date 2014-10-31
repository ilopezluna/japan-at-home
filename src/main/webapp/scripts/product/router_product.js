'use strict';

japanAtHomeApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/product', {
                    templateUrl: 'views/products.html',
                    controller: 'ProductController',
                    resolve:{
                        resolvedProduct: ['Product', function (Product) {
                            return Product.query().$promise;
                        }],
                        resolvedRestaurant: ['Restaurant', function (Restaurant) {
                            return Restaurant.query().$promise;
                        }],
                        resolvedTag: ['Tag', function (Tag) {
                            return Tag.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
