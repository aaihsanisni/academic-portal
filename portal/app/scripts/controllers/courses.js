'use strict';

/**
 * @ngdoc function
 * @name portalApp.controller:CoursesCtrl
 * @description
 * # CoursesCtrl
 * Controller of the portalApp
 */
angular.module('portalApp')
  .controller('CoursesCtrl', ['$scope', 'httpService', '$modal', function ($scope, httpService, $modal) {

    $scope.tableData = null;
    $scope.view = function() {
    	console.log("view");
    	httpService.courseView().then (function(resp) {
    		$scope.tableData = resp;
    		console.log(resp);
    	});
    };
    $scope.view();

	$scope.animationsEnabled = true;
    $scope.add = function () {
    	var addModalInstance = $modal.open({
      	animation: $scope.animationsEnabled,
      	templateUrl: 'addCoursesContent.html',
      	controller: 'addCoursesCtrl',
      	resolve: {
  	 	}
    	});

	    addModalInstance.result.then(function (item) {
	      httpService.addCourse(item.id, item.name, item.dept)
	      	.then(function(resp) {
	      		// $scope.tableData = resp;
	      		console.log("Refreshing users");
	      		$scope.view();

	      	});
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
  	};

  	$scope.showOptions = function(course) {
  		var optionModal = $modal.open({
  			animation:$scope.animationsEnabled,
  			templateUrl:'deleteModal.html',
  			controller:'deleteModalCtrl',
  			resolve: {
  				itemSelected: function() {
  					return course;
  				}
  			}

  		});

	    optionModal.result.then(function (toChange) {
	    	if (toChange.option === 1) {
		     httpService.deleteCourse(course.id)
	      	.then(function(resp) {
	      		// $scope.tableData = resp;
	      		console.log("Refreshing users");
	      		$scope.view();

	      	});
	    	console.log("Deleting " + course.name);
	    	}
	    	else 
	    		console.log("delete course");
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
  	};
  }]);

  angular.module('portalApp')
  .controller('addCoursesCtrl', function ($scope, $modalInstance) {

  $scope.ok = function (addCourse) {
    $modalInstance.close(addCourse);
  };

  $scope.cancel = function () {

    $modalInstance.dismiss('cancel');
  };
});

 angular.module('portalApp')
  .controller('deleteModalCtrl', function ($scope, $modalInstance, itemSelected) {

  $scope.itemSelected = itemSelected;
  $scope.option = 1;
  console.log("option is " + $scope.option);

  $scope.update = function (course) {
  	var resp = [];
  	resp.option = 0;
  	resp.name = course.name;
  	$modalInstance.close(resp);
  };

  $scope.delete = function() {
  	var resp = [];
  	resp.option = 1;
  	$modalInstance.close(resp);
  }
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };

  });
