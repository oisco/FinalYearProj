angular.module('app').controller("ViewMatchupController", function ($scope,$http,$routeParams,$location) {

    var vm=this;
    vm.matchup=null;
    vm.getMatchup=getMatchup;
    vm.labels=null;

    function getMatchup(id) {
        var url="matchups/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.matchup=response.data;
            createChart();
        })
    }

    function createChart(){
        vm.labels= [vm.matchup.fighter1_first_name+' '+vm.matchup.fighter1_last_name,vm.matchup.fighter2_first_name+' '+vm.matchup.fighter2_last_name]
        var ctx = document.getElementById("myChart").getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels:[vm.matchup.fighter1_first_name+' '+vm.matchup.fighter1_last_name+" Strikes Landed",
                vm.matchup.fighter2_first_name+' '+vm.matchup.fighter2_last_name+" Strikes Landed"],
                datasets: [{
                    backgroundColor: [
                        "#0066ff",
                        "#ff5050",
                    ],
                    data: [vm.matchup.result.fighter1StrikesLanded, vm.matchup.result.fighter2StrikesLanded]
                }]
            },
            options:{
                legend: {labels:{fontColor:"#AACCFF", fontSize: 14}}
            }
        });

        var data = {
            labels: [
                vm.matchup.fighter1_first_name+' '+vm.matchup.fighter1_last_name+" Takedowns Landed",
                vm.matchup.fighter2_first_name+' '+vm.matchup.fighter2_last_name+" Takedowns Landed",
                vm.matchup.fighter1_first_name+' '+vm.matchup.fighter1_last_name+" Submissions Attempted",
                vm.matchup.fighter2_first_name+' '+vm.matchup.fighter2_last_name+" Submissions Attempted"
                ],
            datasets: [
                {
                    backgroundColor: [
                        "#0066ff",
                        "#ff5050",
                        "#0066ff",
                        "#ff5050"
                    ],
                    data: [vm.matchup.result.fighter1takedownsLanded, vm.matchup.result.fighter2takedownsLanded,
                    vm.matchup.result.fighter1submissionsAttempted, vm.matchup.result.fighter2submissionsAttempted],
                }
            ]
        };

        var ctx = document.getElementById("myChart2").getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: data,
            options:{
                legend: {labels:{fontColor:"#AACCFF", fontSize: 18}},
                scales: {
                    yAxes: [{
                        ticks: {
                            fontColor: "#AACCFF"
                        }
                    }],
                    xAxes: [{
                        ticks: {
                            fontColor: "#AACCFF"

                        }
                    }]
                }
            }
            // options: options
        });

        var result= vm.matchup.result.method;

        if(!result.includes("draw") ||!result.includes("decision"))
        {
            result="via "+result+" in round "+vm.matchup.result.endingRound+'('+vm.matchup.result.endingTime+')';
        }
        //set the outcome
        if(!result.includes("draw")){
            if(vm.matchup.fighter1_is_winner){
                var outcome=document.getElementById("outcome").innerHTML=(vm.matchup.fighter1_first_name+' '+vm.matchup.fighter1_last_name+" beats "
                +vm.matchup.fighter2_first_name+' '+vm.matchup.fighter2_last_name +" "+result+".");
            }
            else{
                var outcome=document.getElementById("outcome").innerHTML=(vm.matchup.fighter2_first_name+' '+vm.matchup.fighter2_last_name+" beats "+
                vm.matchup.fighter1_first_name+' '+vm.matchup.fighter1_last_name +" "+result+".");
            }
        }


    }

    vm.goToFighter = function (id) {
        $location.path("viewFighter/"+id);
    };


    getMatchup($routeParams.id);
});

