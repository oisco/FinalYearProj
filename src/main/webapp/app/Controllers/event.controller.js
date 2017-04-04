/**
 * Created by Oisín on 10/24/2016.
 */

angular.module('app').controller("EventController", function ($scope,$http, $location) {
    var vm=this;
    vm.events=[];

    var url="/events/previous"
    var eventsPromise=$http.get(url);
    eventsPromise.then(function (response) {
        vm.events=response.data;

    })

    vm.goToEvent = function (eventId) {
        $location.path("viewEvent/"+eventId);
    };
});
