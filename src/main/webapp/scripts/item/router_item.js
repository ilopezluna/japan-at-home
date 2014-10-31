'use strict';

japanAtHomeApp
    .config(function ($routeProvider, $httpProvider, $translateProvider, USER_ROLES) {
            $routeProvider
                .when('/item', {
                    templateUrl: 'views/items.html',
                    controller: 'ItemController',
                    resolve:{
                        resolvedItem: ['Item', function (Item) {
                            return Item.query().$promise;
                        }],
                        resolvedProduct: ['Product', function (Product) {
                            return Product.query().$promise;
                        }]
                    },
                    access: {
                        authorizedRoles: [USER_ROLES.all]
                    }
                })
        });
