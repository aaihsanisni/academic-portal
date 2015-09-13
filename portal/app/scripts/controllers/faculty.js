'use strict';

/**
 * @ngdoc function
 * @name portalApp.controller:FacultyCtrl
 * @description
 * # FacultyCtrl
 * Controller of the portalApp
 */
angular.module('portalApp')
  .controller('FacultyCtrl', ['$scope', 'httpService', '$modal', function ($scope, httpService, $modal) {

    $scope.tableData = null;
    $scope.view = function() {
    	console.log("view");
    	httpService.facultyView().then (function(resp) {
    		$scope.tableData = resp;
    		console.log(resp);
    	});
    };
    $scope.view();

	$scope.animationsEnabled = true;
    $scope.add = function () {
    	var addModalInstance = $modal.open({
      	animation: $scope.animationsEnabled,
      	templateUrl: 'addFacultyContent.html',
      	controller: 'addFacultyCtrl',
      	resolve: {
  	 	}
    	});

	    addModalInstance.result.then(function (item) {
	      httpService.addFaculty(item.id, item.name, item.dept)
	      	.then(function(resp) {
	      		// $scope.tableData = resp;
	      		console.log("Refreshing users");
	      		$scope.view();

	      	});
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
  	};

  	$scope.showOptions = function(faculty) {
  		var optionModal = $modal.open({
  			animation:$scope.animationsEnabled,
  			templateUrl:'deleteModal.html',
  			controller:'deleteModalCtrl',
  			resolve: {
  				itemSelected: function() {
  					return faculty;
  				}
  			}

  		});

	    optionModal.result.then(function (toChange) {
	    	if (toChange.option === 1) {
		     httpService.deleteFaculty(faculty.id)
	      	.then(function(resp) {
	      		// $scope.tableData = resp;
	      		console.log("Refreshing users");
	      		$scope.view();

	      	});
	    	console.log("Deleting " + faculty.name);
	    	}
	    	else 
	    		console.log("deletefaculty");
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
  	};
  }]);

  angular.module('portalApp')
  .controller('addFacultyCtrl', function ($scope, $modalInstance) {

  $scope.ok = function (addFaculty) {
    $modalInstance.close(addFaculty);
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

  $scope.update = function (faculty) {
  	var resp = [];
  	resp.option = 0;
  	resp.name = faculty.name;
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
