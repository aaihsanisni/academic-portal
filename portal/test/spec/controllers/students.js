'use strict';

describe('Controller: StudentsCtrl', function () {

  // load the controller's module
  beforeEach(module('portalApp'));

  var StudentsCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    StudentsCtrl = $controller('StudentsCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(StudentsCtrl.awesomeThings.length).toBe(3);
  });
});
