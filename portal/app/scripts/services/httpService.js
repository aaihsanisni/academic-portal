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
    },

    facultyView: function() {
        return $http({
            method: 'GET',
            url: '/faculty/'
        }).then(function(response) {
            return response.data;
        }, function(err) {
            console.log("Query failed for viewing faculty " + err);
        });
    },

    addFaculty: function(id, name, dept) {
        return $http({
            method:'GET',
            url:'/faculty/add',
            params: {
                id:id,
                name:name,
                dept:dept
            }
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Query failed for adding faculty " + err);
        });
    },


    deleteFaculty: function(id) {
        return $http({
            method:'GET',
            url:'/faculty/delete',
            params: {
                id:id
            }
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Query failed for deleting faculty " + err);
        });
    },

    courseView: function() {
        return $http({
            method: 'GET',
            url: '/course/'
        }).then(function(response) {
            return response.data;
        }, function(err) {
            console.log("Query failed for viewing faculty " + err);
        });
    },

    addCourse: function(id, name, dept) {
        return $http({
            method:'GET',
            url:'/course/add',
            params: {
                id:id,
                name:name,
                dept:dept
            }
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Query failed for adding course " + err);
        });
    },


    deleteCourse: function(id) {
        return $http({
            method:'GET',
            url:'/course/delete',
            params: {
                id:id
            }
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Query failed for deleting course " + err);
        });
    },

    courseOfferingData: function(id) {
        return $http({
            method:'GET',
            url:'/admin/courseOfferingData'
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Error while loading course offering");
        });
    },

    courseOfferingDetails: function(course_id, faculty_id) {
        return $http({
            method:'GET',
            url:'/admin/courseOfferingDetails',
            params: {
                course_id:course_id,
                faculty_id:faculty_id
            }
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Error while getting course offering for " + course_id +  " " + faculty_id);
        });
    },

    registerStudent: function(entryno, course_id) {
        return $http({
            method:'GET',
            url:'/admin/registerStudent',
            params: {
                entryno:entryno,
                course_id:course_id
            }
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Error while registering student " + err.data.error);
            return err;
        });
    },

    registerCourseOffering: function(id, course_id, course_limit) {
        return $http({
            method:'GET',
            url:'/admin/registerCourseOffering',
            params: {
                faculty_id:id,
                course_id:course_id,
                course_limit:course_limit
            }
        }).then(function(resp) {
            return resp.data;
        }, function(err) {
            console.log("Error while registering course offering " + err.data.error);
        });
    }

};
});
