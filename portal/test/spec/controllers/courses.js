'use strict';

describe('Controller: CoursesCtrl', function () {

  // load the controller's module
  beforeEach(module('portalApp'));

  var CoursesCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    CoursesCtrl = $controller('CoursesCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(CoursesCtrl.awesomeThings.length).toBe(3);
  });
});
