angular.module('app').controller("ViewAllUpcomingPredictionsController", function ($scope,$http, $location) {
    var vm=this;
    vm.nextEventPic="";
    //
    //
    // ////get the next upcoming event
    //
    // var url="/events/next"
    // var eventsPromise=$http.get(url);
    // eventsPromise.then(function (response) {
    //     vm.event=response.data;
    //     vm.nextEventPic=vm.event.feature_image;
    // })
    //
    // vm.goToUpcomingEvent = function (eventId) {
    //     $location.path("upcomingEvents/"+eventId);
    // };


});
