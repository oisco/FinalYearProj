angular.module('app').controller("HomeController", function ($scope,$http, $location) {
    var vm=this;
    vm.events=[];
    vm.nextEvent=null;
    vm.index=0;

    vm.myInterval = 5000;
    vm.noWrapSlides = false;
    vm.activeSlide = 0;

    var url="/events/next"
    var eventsPromise=$http.get(url);
    eventsPromise.then(function (response) {
        vm.events=response.data;
    })

    vm.goToUpcomingEvent = function (eventId) {
        $location.path("upcomingEvents/"+eventId);
    };



});
