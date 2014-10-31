'use strict';

japanAtHomeApp.factory('Zip', function ($resource) {
        return $resource('app/rest/zips/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
