'use strict';

japanAtHomeApp.factory('Product', function ($resource) {
        return $resource('app/rest/products/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
