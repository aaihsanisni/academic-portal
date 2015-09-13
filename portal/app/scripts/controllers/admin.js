'use strict';

/**
 * @ngdoc function
 * @name portalApp.controller:AdminCtrl
 * @description
 * # AdminCtrl
 * Controller of the portalApp
 */
angular.module('portalApp')
  .controller('AdminCtrl', ['$scope', 'httpService', '$modal', function ($scope, httpService, $modal) {
    $scope.tableData = null;
    $scope.view = function() {
    	console.log("view");
    	httpService.courseOfferingData().then (function(resp) {
    		$scope.tableData = resp;
    		console.log(resp);
    	});
    };
    $scope.view();

    $scope.showCourse = function(item) {
 		var courseData = $modal.open({
  			animation:$scope.animationsEnabled,
  			templateUrl:'courseDetail.html',
  			controller:'courseDetailCtrl',
  			resolve: {
  				itemSelected: function() {
  					var courseData = [];
  					console.log("Getting course data for " + item.course_id + " " + item.faculty_id);
  					httpService.courseOfferingDetails(item.course_id, item.faculty_id)
  					.then(function(resp) {
  						courseData = resp;
  						console.log(resp);
  					});
  					console.log(courseData);
  					return courseData;
  				}
  			}

  		});

	    optionModal.result.then(function () {
	  		$scope.view();
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
  	};

    $scope.registerStudent = function() {
      var dialog = $modal.open({
        animation:$scope.animationsEnabled,
        templateUrl:'registerStudent.html',
        controller:'registerStudentCtrl',
        resolve: {

        }
      });

      dialog.result.then(function (item) {

      }, function() {
        console.log('Modal dismissed at: ' + new Date());
      });
    };


  }]);

  angular.module('portalApp')
  .controller('courseDetailCtrl', function ($scope, $modalInstance, courseData) {
    $scope.tableData = courseData;
  $scope.ok = function (addCourse) {
    $modalInstance.close(addCourse);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});