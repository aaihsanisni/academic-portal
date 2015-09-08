'use strict';

/**
 * @ngdoc service
 * @name portalApp.httpService
 * @description
 * # httpService
 * Service in the portalApp.
 */
angular.module('portalApp')
  .factory('httpService', function ($http) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    return {

    	studentsView: function() {
    	return $http({
    		method: 'GET',
    		url: '/students/'
    	}).then(function(response) {
    		return response.data;
    	}, function(err) {
    		console.log("Query failed for viewing student " + err);
    	});
    },

    addStudent: function(entryno, name, dept) {
    	return $http({
    		method:'GET',
    		url:'/students/add',
    		params: {
    			entryno:entryno,
    			name:name,
    			dept:dept
    		}
    	}).then(function(resp) {
    		return resp.data;
    	}, function(err) {
    		console.log("Query failed for adding student " + err);
    	});
    },


    deleteStudent: function(entryno) {
    	return $http({
    		method:'GET',
    		url:'/students/delete',
    		params: {
    			entryno:entryno
    		}
    	}).then(function(resp) {
    		return resp.data;
    	}, function(err) {
    		console.log("Query failed for deleting student " + err);
    	});
    }
};
});
