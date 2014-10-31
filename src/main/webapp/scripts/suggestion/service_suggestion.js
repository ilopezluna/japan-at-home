'use strict';

japanAtHomeApp.factory('Suggestion', function ($resource) {
        return $resource('app/rest/suggestions/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': { method: 'GET'}
        });
    });
