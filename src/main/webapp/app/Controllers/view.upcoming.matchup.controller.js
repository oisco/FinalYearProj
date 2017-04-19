angular.module('app').controller("ViewUpcomingMatchupController", function ($scope,$http, $location ,$routeParams) {

    var vm=this;
    vm.matchup=null;
    vm.getMatchup=getMatchup;
    vm.labels=null;
    vm.fighter1=null;
    vm.fighter2=null;
    vm.labels=[];
    vm.values=[];
    vm.wl=0;
    var cureentFighter;

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
                setUpGraphValues(vm.fighter1);
                cureentFighter=vm.fighter1.first_name+' '+vm.fighter1.last_name;
                setUpGraph(vm.fighter1,"fighter1Pie","lineChart1");
            }
            else
                vm.fighter2=response.data;
                setUpGraphValues(vm.fighter2);
                cureentFighter=vm.fighter2.first_name+' '+vm.fighter2.last_name;
                setUpGraph(vm.fighter2,"fighter2Pie","lineChart2");

        })
    }

    function setUpGraphValues(fighter) {
        //set up 2 fighter arrays here
         //set up labels
            vm.labels=[""];
            vm.values=[0];
            vm.wl=0;


        //need to sort matchups before iterating
            fighter.matchups.sort(function(a,b) {
                return (a.date > b.date) ? 1 : ((b.date > a.date) ? -1 : 0);} );

        //only want last 4
        fighter.matchups.splice(0, fighter.matchups.length - 5);


        var today = new Date();
            today.setHours(0, 0, 0, 0);
            angular.forEach(fighter.matchups, function (matchup)
            {
                //only do first 4 fights
                if(vm.values.length>5){
                    return
                }
                if(matchup.date>today){
                    //dont plot a fighters future matchups
                }
                else{
                    if(!matchup.fighter1_is_winner && !matchup.fighter2_is_winner){
                        //neither fighter won therefore win/loss graph stays the same
                    }
                    else if(matchup.fighter1_id==fighter.id){
                        if(matchup.fighter1_is_winner)
                        {
                            vm.labels.push(matchup.fighter2_first_name+" "+matchup.fighter2_last_name+" (Win)");
                            vm.wl++;
                            vm.values.push((vm.wl));
                        }else {
                            vm.labels.push(matchup.fighter2_first_name+" "+matchup.fighter2_last_name+" (Loss)");
                            vm.wl--;
                            vm.values.push((vm.wl));
                        }
                    }else if(matchup.fighter2_id==fighter.id){
                        if(matchup.fighter2_is_winner){
                            vm.labels.push(matchup.fighter1_first_name+" "+matchup.fighter1_last_name+" (Win)");
                            vm.wl++;
                            vm.values.push((vm.wl));
                        }
                        else {
                            vm.labels.push(matchup.fighter1_first_name+" "+matchup.fighter1_last_name+" (Loss)");
                            vm.wl--;
                            vm.values.push((vm.wl));
                        }
                    }
                }

            })
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
            },
            options:{
                legend: {labels:{fontColor:"#AACCFF", fontSize: 14}}
            }

        });

        var ctx2 = document.getElementById(lineChart).getContext('2d');
        var myChart2 = new Chart(ctx2, {
            type: 'line',
            data: {
                labels: vm.labels,
                datasets: [{
                    label: cureentFighter+' Performance History (Wins-Losses)',
                    data: vm.values,
                    backgroundColor: "rgba(153,255,51,0.4)"
                }]
            },
            options:{
                legend: {labels:{fontColor:"#AACCFF", fontSize: 18}},
                scales: {
                    yAxes: [{
                        ticks: {
                            fontColor: "#AACCFF",
                            suggestedMin: -2,   // minimum will be 0, unless there is a lower value.
                            suggestedMax: 2   // minimum will be 0, unless there is a lower value.
                            // OR //
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
                            labelString: 'Last 5 (or fewer) fights',
                            fontColor: "#AACCFF"
                        }
                    }]
                }
            }
        });
    }





    getMatchup($routeParams.id)
});
