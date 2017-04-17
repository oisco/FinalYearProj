angular.module('app').controller("ViewUpcomingMatchupController", function ($scope,$http, $location ,$routeParams) {

    var vm=this;
    vm.matchup=null;
    vm.getMatchup=getMatchup;
    vm.labels=null;
    vm.fighter1=null;
    vm.fighter2=null;

    function getMatchup(id) {
        var url="matchups/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.matchup=response.data;

            //request both fighters profiles
            getFighter(1,vm.matchup.fighter1_id);
            getFighter(2,vm.matchup.fighter2_id);
        });

    }

    vm.goToFighter = function (id) {
        $location.path("viewFighter/"+id);
    };

    function getFighter(fighterToGet,id) {
        var url="fighters/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            if(fighterToGet==1){
                vm.fighter1=response.data;
            }
            else
                vm.fighter2=response.data;
        })
    }


    getMatchup($routeParams.id)
});
