'use strict';

japanAtHomeApp.factory('Order', function ($resource) {
        return $resource('app/rest/orders/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
