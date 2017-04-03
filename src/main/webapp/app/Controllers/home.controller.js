angular.module('app').controller("HomeController", function ($scope,$http, $location) {
    var vm=this;
    vm.events=[];
    vm.nextEvent=null;
    vm.index=0;

    var url="/events/next"
    var eventsPromise=$http.get(url);
    eventsPromise.then(function (response) {
        vm.events=response.data;
    })

    vm.goToUpcomingEvent = function (eventId) {
        $location.path("upcomingEvents/"+eventId);
    };

    $scope.slide = function (dir) {
        debugger
        if(vm.index==vm.events.length-1 && dir>0){
            vm.index=0;
        }
        else if(vm.index==0 && dir<0){
            vm.index=vm.events.length-1;
        }
        else {
            vm.index+=dir;
        }
        $('#Carousel').carousel(dir);
        debugger;
    };



});
