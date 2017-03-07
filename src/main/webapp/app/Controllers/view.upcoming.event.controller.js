
angular.module('app').controller("ViewUpcomingEventController", function ($scope,$http,$routeParams,$location,myservice) {
    var vm=this;
    vm.event=null;
    vm.getEvent=getEvent;

    function getEvent(id) {
        var url="events/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.event=response.data;
        })
    }

    vm.goToMatchup=function (id) {
        $location.path("viewUpcomingMatchup/"+id);
    };

    vm.viewPredictions=function(){
        myservice.setPersonArray(vm.event);
        $location.path("viewPredictions/"+vm.event.id);
    }

    vm.goToFighter = function (id) {
        $location.path("viewFighter/"+id);
    };




    getEvent($routeParams.id);
});
