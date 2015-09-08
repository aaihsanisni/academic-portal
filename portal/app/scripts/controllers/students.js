'use strict';

/**
 * @ngdoc function
 * @name portalApp.controller:StudentsCtrl
 * @description
 * # StudentsCtrl
 * Controller of the portalApp
 */
angular.module('portalApp')
  .controller('StudentsCtrl', ['$scope', 'httpService', '$modal', function ($scope, httpService, $modal) {
    this.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma',
    ];
    $scope.tableData = null;
    $scope.view = function() {
    	console.log("view");
    	httpService.studentsView().then (function(resp) {
    		$scope.tableData = resp;
    		console.log(resp);
    	});
    };
    $scope.view();

	$scope.animationsEnabled = true;
    $scope.add = function () {
    	var addModalInstance = $modal.open({
      	animation: $scope.animationsEnabled,
      	templateUrl: 'addModalContent.html',
      	controller: 'addModalCtrl',
      	resolve: {
  	 	}
    	});

	    addModalInstance.result.then(function (addStudent) {
	      // $scope.addStudent = addStudent;
	      httpService.addStudent(addStudent.entryno, addStudent.name, addStudent.dept)
	      	.then(function(resp) {
	      		// $scope.tableData = resp;
	      		console.log("Refreshing users");
	      		$scope.view();

	      	});
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
  	};

  	$scope.showOptions = function(student) {
  		var optionModal = $modal.open({
  			animation:$scope.animationsEnabled,
  			templateUrl:'deleteModal.html',
  			controller:'deleteModalCtrl',
  			resolve: {
  				itemSelected: function() {
  					return student;
  				}
  			}

  		});

	    optionModal.result.then(function (toDelete) {
	     httpService.deleteStudent(toDelete.entryno)
	      	.then(function(resp) {
	      		// $scope.tableData = resp;
	      		console.log("Refreshing users");
	      		$scope.view();

	      	});
	    	console.log("Deleting " + toDelete.name);
	    }, function () {
	      console.log('Modal dismissed at: ' + new Date());
	    });
  	};
  }]);

  angular.module('portalApp')
  .controller('addModalCtrl', function ($scope, $modalInstance) {


  $scope.ok = function (addStudent) {
    $modalInstance.close(addStudent);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});

 angular.module('portalApp')
  .controller('deleteModalCtrl', function ($scope, $modalInstance, itemSelected) {

  $scope.itemSelected = itemSelected;

  $scope.ok = function () {
    $modalInstance.close(itemSelected);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});
