angular.module('app').controller("ViewFighterController", function ($location,$scope,$http,$routeParams) {
    var vm=this;
    vm.fighter=null;
    vm.getFighter=getFighter;
    vm.labels=[];
    vm.values=[];
    vm.fighterImage="";
    vm.wl=0;
    vm.currentDate=new Date();


    function getFighter(id) {
        var url="fighters/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.fighter=response.data;
            if(vm.fighter.left_full_body_image==null || vm.fighter.left_full_body_image.length==0){
                vm.fighterImage=vm.fighter.thumbnail;
            }
            else{
                vm.fighterImage=vm.fighter.left_full_body_image;
            }
            setUpGraphData();
            setUpGraph();
        })
    }

    $scope.rowClass = function(matchup){
        if(matchup.fighter1_is_winner){
            if(matchup.fighter1_id==vm.fighter.id){
                return "win";
            }
            else {
                return "loss";
            }
        }
        else if(matchup.fighter2_is_winner){
            if(matchup.fighter2_id==vm.fighter.id){
                return "win";
            }
            else {
                return "loss";
            }
        }
        else if((!matchup.fighter2_is_winner)&&(!matchup.fighter2_is_winner))
        {
            return "active";
        }


    }
    vm.goToMatchup=function (id) {
        $location.path("viewMatchup/"+id);
    };

    vm.goToUpcomingMatchup=function (id) {
        $location.path("viewUpcomingMatchup/"+id);
    };


    function setUpGraphData(){
        //set up labels
        vm.labels=["Joined the UFC"];
        vm.values=[0];
        //need to sort matchups before iterating
        vm.fighter.matchups.sort(function(a,b) {
            return (a.date > b.date) ? 1 : ((b.date > a.date) ? -1 : 0);} );

        var today = new Date();
        today.setHours(0, 0, 0, 0);
        angular.forEach(vm.fighter.matchups, function (matchup) {
            if(matchup.date>today){
            //dont plot a fighters future matchups
            }
            else{
                if(!matchup.fighter1_is_winner && !matchup.fighter2_is_winner){
                    //neither fighter won therefore win/loss graph stays the same
                }
                else if(matchup.fighter1_id==vm.fighter.id){
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
                }else if(matchup.fighter2_id==vm.fighter.id){
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

    function setUpGraph(){
        var ctx = document.getElementById("myChart");
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
                    data: [vm.fighter.wins,vm.fighter.draws,vm.fighter.losses]
                }]
            },
            options:{
                legend: {labels:{fontColor:"#AACCFF", fontSize: 14}}
            }
        });

        var ctx = document.getElementById('myChart2').getContext('2d');
        var myChart = new Chart(ctx, {
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
                            labelString: 'Matchup History',
                            fontColor: "#AACCFF"
                        }
                    }]
                }
            }
        });
    }

    vm.goToFighter = function (id) {
        $location.path("viewFighter/"+id);
    };

    vm.goToMatchup=function (id) {
        $location.path("viewMatchup/"+id);
    };
    vm.goToUpcomingMatchup=function (id) {
        $location.path("viewUpcomingMatchup/"+id);
    };


    getFighter($routeParams.id);

});