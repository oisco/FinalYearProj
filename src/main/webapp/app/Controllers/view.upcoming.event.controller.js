
angular.module('app').controller("ViewUpcomingEventController", function ($scope,$http,$routeParams,$location,myservice) {
    var vm=this;
    vm.event=null;
    vm.predictions=[];
    vm.getEvent=getEvent;

    vm.labelClass = function(id){
        if(vm.predictions.length===0){
            return "hide";
        }
        //check if fighter is in predictions
        if(vm.predictions.indexOf(id)>=0)
        {
            return "ribbon";
        }
        else{
            return "loserribbon";
        }
    }

    vm.loserLabelClass = function(id){
        if(vm.predictions.length===0){
            return false;
        }
        //check if fighter is in predictions
        if(vm.predictions.indexOf(id)>=0)
        {
            return false;
        }
        else{
            return true;
        }
    }
    function getEvent(id) {
        var url="events/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.event=response.data;
        })
    }

    vm.getPredictions=function () {
        vm.event;
        var url="predictions/event/"+vm.event.id;
        var predictionPromise=$http.get(url);
        predictionPromise.then(function (response) {
            vm.predictions=response.data;
        })
    }



    vm.isFighterPredictedWinner=function(id) {
    return vm.predictions.indexOf(id)>=0;
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
