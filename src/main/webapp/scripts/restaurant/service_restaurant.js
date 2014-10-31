'use strict';

japanAtHomeApp.factory('Restaurant', function ($resource) {
        return $resource('app/rest/restaurants/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
