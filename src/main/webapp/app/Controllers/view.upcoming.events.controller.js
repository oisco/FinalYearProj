angular.module('app').controller("ViewUpcomingEventsController", function ($scope,$http, $location) {
    var vm=this;
    vm.events=[];

    var url="/events/upcoming"
    var eventsPromise=$http.get(url);
    eventsPromise.then(function (response) {
        vm.events=response.data;
        debugger

    })

    vm.goToUpcomingEvent = function (eventId) {
        $location.path("upcomingEvents/"+eventId);
    };


});
