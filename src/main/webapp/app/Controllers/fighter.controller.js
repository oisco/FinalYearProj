angular.module('app').controller("FighterController", function ($scope,$http, $location) {
    var vm=this;
    vm.fighters=[];

    var url="/fighters/all"
    var eventsPromise=$http.get(url);
    eventsPromise.then(function (response) {
        vm.fighters=response.data;

    })

    vm.goToFighter = function (id) {
        $location.path("viewFighter/"+id);
    };



});
