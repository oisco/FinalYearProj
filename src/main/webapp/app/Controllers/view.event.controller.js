
angular.module('app').controller("ViewEventController", function ($scope,$http,$routeParams,$location) {
    var vm=this;
    vm.event=null;
    vm.getEvent=getEvent;

    function getEvent(id) {
        var url="events/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.event=response.data;
            debugger;
        })
    }

    vm.goToMatchup=function (id) {
        $location.path("viewMatchup/"+id);
    };

    vm.goToFighter = function (id) {
        $location.path("viewFighter/"+id);
    };

    $scope.cellClass = function(id,iswinner){
        //check if fighter is winner or loser and colour cell accordingly
        // if(vm.predictions.indexOf(id)>=0)
        if(iswinner)
        {
            return "success";
        }
        else{
            return "danger";
        }
    }

    getEvent($routeParams.id);
});
