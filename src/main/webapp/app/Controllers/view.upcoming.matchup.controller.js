angular.module('app').controller("ViewUpcomingMatchupController", function ($scope,$http, $location ,$routeParams) {

    var vm=this;
    vm.matchup=null;
    vm.getMatchup=getMatchup;
    vm.labels=null;
    vm.fighter1=null;
    vm.fighter2=null;
    vm.labels=["h","b","n"]
    vm.values=[1,2,3]

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
                setUpGraph(vm.fighter1,"fighter1Pie","lineChart1");
            }
            else
                vm.fighter2=response.data;
                setUpGraph(vm.fighter2,"fighter2Pie","lineChart2");

        })
    }

    function setUpGraphValues(fighter) {
        //set up 2 fighter arrays here
        for(var i=0;i<fighter.matchups.length;i++){
            vm.values.push()
        }

    }

    function setUpGraph(fighter,pieChart,lineChart) {
        var ctx = document.getElementById(pieChart);
        var myChart = new Chart(ctx, {
            type: 'pie',
            data: {
                labels: ["Wins","Draws", "Losses"],
                datasets: [{
                    backgroundColor: [
                        "#2ecc71",
                        "#95a5a6",
                        "#e74c3c",
                    ],
                    data: [fighter.wins,fighter.draws,fighter.losses]
                }]
            }
        });

        var ctx2 = document.getElementById(lineChart).getContext('2d');
        var myChart2 = new Chart(ctx2, {
            type: 'line',
            data: {
                labels: vm.labels,
                datasets: [{
                    label: 'UFC Performance History Wins-Losses',
                    data: vm.values,
                    backgroundColor: "rgba(153,255,51,0.4)"
                }]
            },
            options:{
                legend: {labels:{fontColor:"#AACCFF", fontSize: 18}},
                scales: {
                    yAxes: [{
                        ticks: {
                            fontColor: "#AACCFF"
                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Career UFC Wins- UFC Losses',
                            fontColor: "#AACCFF"
                        }
                    }],
                    xAxes: [{
                        ticks: {
                            fontColor: "#AACCFF"

                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Last 4 (or fewer) fights',
                            fontColor: "#AACCFF"
                        }
                    }]
                }
            }
        });
    }





    getMatchup($routeParams.id)
});
