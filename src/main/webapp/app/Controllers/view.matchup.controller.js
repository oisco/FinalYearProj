angular.module('app').controller("ViewMatchupController", function ($scope,$http,$routeParams,$location) {

    var vm=this;
    vm.matchup=null;
    vm.getMatchup=getMatchup;
    vm.labels=null;
    vm.donutData=[];

    function getMatchup(id) {
        var url="matchups/view/"+id;
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.matchup=response.data;
            createChart();
            //set the accuracy here
            createAccuracyDonuts("accuracyDoughnut1",vm.matchup.result.fighter1StrikesLanded,vm.matchup.result.fighter1StrikesAttempted,"#0066ff","donut"+vm.matchup.id);
            createAccuracyDonuts("accuracyDoughnut2",vm.matchup.result.fighter2StrikesLanded,vm.matchup.result.fighter2StrikesAttempted,"#ff5050","donut2"+vm.matchup.id);
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
                legend: {
                    display: false
                },                scales: {
                    yAxes: [{
                        ticks: {
                            // OR //
                            beginAtZero: true,   // minimum value will be 0.
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


    function createAccuracyDonuts(id,landed,attempted,color,pluginName) {

        var accuracy=((landed * 100.0) / (attempted));

        var data = {
            labels: [
                "Strikes Landed",
                "Strikes Attempted",
            ],
            datasets: [
                {
                    data: [landed,attempted-landed],
                    backgroundColor: [
                        "#3cba28",
                        "#f44141"
                    ],
                    hoverBackgroundColor: [
                        "#3cba28",
                        "#f44141"
                    ]
                }]
        };

        Chart.pluginService.register({
            beforeDraw: function(chart) {
                if (chart.config.options.optionsUsingPlugin1==pluginName) {
                    var width = chart.chart.width,
                        height = chart.chart.height,
                        ctx = chart.chart.ctx;

                    ctx.restore();
                    var fontSize = (height / 114).toFixed(2);
                    ctx.font = fontSize + "em sans-serif";
                    ctx.textBaseline = "middle";
                    // ctx.strokeStyle = "red";
                    var gradient=ctx.createLinearGradient(0,0,1,0);
                    gradient.addColorStop("1.0",color);
// Fill with gradient
                    ctx.fillStyle=gradient
                    var text = Math.round(accuracy * 100) / 100+'%',
                        textX = Math.round((width - ctx.measureText(text).width) / 2),
                        textY = height / 2;

                    ctx.fillText(text, textX, textY);
                    ctx.save();
                }
            }
        });

        var chart = new Chart(document.getElementById(id), {
            type: 'doughnut',
            data: data,
            options: {
                responsive: true,
                legend: {
                    display: false
                },
                optionsUsingPlugin1:pluginName
            }
        });

    }

    vm.goToFighter = function (id) {
        $location.path("viewFighter/"+id);
    };


    getMatchup($routeParams.id);
});

