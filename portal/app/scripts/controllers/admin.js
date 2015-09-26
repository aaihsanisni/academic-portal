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
    var courseData = [];
    console.log("Getting course data for " + item.course_id + " " + item.faculty_id);
    httpService.courseOfferingDetails(item.course_id, item.faculty_id)
    .then(function(resp) {
      courseData = resp;
      console.log("course data is ")
      console.log(courseData);

      var courseData = $modal.open({
      animation:$scope.animationsEnabled,
      templateUrl:'courseDetail.html',
      controller:'courseDetailCtrl',
      resolve: {
      courseData: function() {
          return courseData;
      }
      }
      });

      courseData.result.then(function () {
      $scope.view();
      }, function () {
      console.log('Modal dismissed at: ' + new Date());
      });
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
        console.log("Registering " + item.entryno + " course " + item.course_id);
        httpService.registerStudent(item.entryno, item.course_id)
        .then(function(resp) {

        });
      }, function() {
        console.log('Modal dismissed at: ' + new Date());
      });
    };

    $scope.registerFaculty = function() {
      var dialog = $modal.open({
        animation:$scope.animationsEnabled,
        templateUrl:'registerFaculty.html',
        controller:'registerFacultyCtrl',
        resolve: {

        }
      });

      dialog.result.then(function (item) {
        console.log("Registering faculty" + item.id + " course " + item.course_id + " " + item.course_limit);
        httpService.registerCourseOffering(item.id, item.course_id, item.course_limit)
        .then(function(resp) {
          $scope.view();
        }, function(err){
          console.log("Error:" + err.error);
        });
      }, function() {
        console.log('Modal dismissed at: ' + new Date());
      });
    };


  }]);

  angular.module('portalApp')
  .controller('courseDetailCtrl', function ($scope, $modalInstance, courseData) {
    $scope.tableData = courseData;
                  console.log($scope.tableData);
                                console.log(courseData);


  $scope.ok = function (addCourse) {
    $modalInstance.close(addCourse);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});

  angular.module('portalApp')
  .controller('registerStudentCtrl', function ($scope, $modalInstance) {
  $scope.ok = function (item) {
    $modalInstance.close(item);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});

  angular.module('portalApp')
  .controller('registerFacultyCtrl', function ($scope, $modalInstance) {
  $scope.ok = function (item) {
    $modalInstance.close(item);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});