angular.module('app').controller("ViewAllPredictionsController", function ($scope,$http, $location) {
    var vm=this;
    vm.predictions=[];

    // var url="/events/upcoming"
    // var eventsPromise=$http.get(url);
    // eventsPromise.then(function (response) {
    //     vm.events=response.data;
    //     debugger
    //
    // })
    //
    // vm.goToUpcomingEvent = function (eventId) {
    //     $location.path("upcomingEvents/"+eventId);
    // };


});
