//
// angular.module('app').controller("ViewEventPredictionsController", function ($scope,$http,$routeParams,$location,myservice) {
//     var vm=this;
//     vm.predictions=null;
//     vm.matchups=null;
//     vm.event=null;
//     // vm.getPredictions=getPredictions;
//
//     function getPredictions(id) {
//         var url="predictions/event/"+id;
//         var eventsPromise=$http.get(url);
//         eventsPromise.then(function (response) {
//             //maybe use a raw query get winners?
//             vm.predictions=response.data;
//             vm.event=myservice.getPersonArray();
//         })
//     }
//
//     vm.goToFighter = function (id) {
//         $location.path("viewFighter/"+id);
//     };
//
//     $scope.cellClass = function(id){
//         //check if fighter is in predictions
//         if(vm.predictions.indexOf(id)>=0)
//         {
//             return "success";
//         }
//         else{
//             return "danger";
//         }
//     }
//
//     getPredictions($routeParams.id);
//
// });
