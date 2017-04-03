angular.module('app').controller("ViewAllPredictionsController", function ($scope,$http, $location) {
    var vm=this;
    vm.values=[];
    vm.labels=[];
    vm.predictions=[];
    vm.wl=0;
    vm.accuracy=0;
    vm.numCorrect=0;
    vm.totalFights=0;
    var accuracyHistory=[];
    var limit;
    vm.foldResult=[];


    function getFoldResults() {
        var url="predictions/folds"
        var predictionPromise=$http.get(url);
        predictionPromise.then(function (response) {
            vm.foldResult = response.data;
        });

    }
    function getPredictions() {
        var url="predictions/all/";
        var predictionPromise=$http.get(url);
        predictionPromise.then(function (response) {
            vm.predictions=response.data;
            limit=vm.predictions.length;

            setUpGraphData();//chartjs

            setUpGraph();
            createDoughnutChart();


        })
    }

    function createDoughnutChart() {
        var pctCorrect=vm.accuracy;
        var pctIncorrect=100-vm.accuracy;
        var data = {
            labels: [
                "Correct",
                "Incorrect",
            ],
            datasets: [
                {
                    data: [pctCorrect,pctIncorrect],
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
                if (chart.config.options.optionsUsingPlugin1=="DONUT") {
                    var width = chart.chart.width,
                        height = chart.chart.height,
                        ctx = chart.chart.ctx;

                    ctx.restore();
                    var fontSize = (height / 114).toFixed(2);
                    ctx.font = fontSize + "em sans-serif";
                    ctx.textBaseline = "middle";

                    var text = Math.round(vm.accuracy * 100) / 100+'%',
                        textX = Math.round((width - ctx.measureText(text).width) / 2),
                        textY = height / 2;

                    ctx.fillText(text, textX, textY);
                    ctx.save();
                }
            }
        });

        var chart = new Chart(document.getElementById('accuracyDoughnut'), {
            type: 'doughnut',
            data: data,
            options: {
                responsive: true,
                legend: {
                    display: false
                },
                optionsUsingPlugin1:"DONUT"
            }
        });
    }

    function setUpGraphData(){
        angular.forEach(vm.predictions, function (prediction) {

            vm.accuracy=(vm.numCorrect/(vm.totalFights/100));
            accuracyHistory.push(vm.accuracy);

            vm.totalFights++;
            if(prediction[6]) {
             vm.wl++;
             vm.numCorrect++;
         }else {
             vm.wl--;
         }
        });

        vm.values.splice(0,vm.values.length);
        vm.values.splice(0,vm.values.length);
        for (var i=0;i<vm.foldResult.length;i++){
            vm.values.push(Math.round(vm.foldResult[i].accuracy * 100) / 100);
            vm.labels.push("set "+(i+1));
        }

    }
    function setUpGraph() {
        var horizonalLinePlugin = {
            afterDraw: function(chartInstance) {
                var yScale = chartInstance.scales["y-axis-0"];
                var canvas = chartInstance.chart;
                var ctx = canvas.ctx;
                var index;
                var line;
                var style;

                if (chartInstance.options.horizontalLine) {
                    for (index = 0; index < chartInstance.options.horizontalLine.length; index++) {
                        line = chartInstance.options.horizontalLine[index];

                        if (!line.style) {
                            style = "rgba(169,169,169, .6)";
                        } else {
                            style = line.style;
                        }

                        if (line.y) {
                            yValue = yScale.getPixelForValue(line.y);
                        } else {
                            yValue = 0;
                        }

                        ctx.lineWidth = 3;

                        if (yValue) {
                            ctx.beginPath();
                            ctx.moveTo(0, yValue);
                            ctx.lineTo(canvas.width, yValue);
                            ctx.strokeStyle = style;
                            ctx.stroke();
                        }

                        if (line.text) {
                            ctx.fillStyle = style;
                            ctx.fillText(line.text, 0, yValue + ctx.lineWidth);
                        }
                    }
                    return;
                };
            }
        };
        Chart.pluginService.register(horizonalLinePlugin);

        var ctx = document.getElementById('myChart').getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                 labels: vm.labels,
                datasets: [{
                    label: '% of fights predicted correctly on each test set',
                    data: vm.values,
                    backgroundColor: "rgba(153,255,51,0.4)"
                }]},
                options: {
                    "horizontalLine": [{
                        "y": 50,
                        "style": "rgba(255, 0, 0, .4)",
                        "text": "random"
                    }],
                    scales: {
                        yAxes: [{
                            display: true,
                            ticks: {
                                suggestedMin: 35,    // minimum will be 0, unless there is a lower value.
                                // OR //
                                // beginAtZero: true   // minimum value will be 0.
                            },
                            scaleLabel: {
                                display: true,
                                labelString: '% Correct'
                            }
                        }],
                        xAxes: [{
                            scaleLabel: {
                                display: true,
                                labelString: 'Fold'
                            }
                        }]
                    }
                }
        });
    }
    $scope.rowClass = function(prediction) {
            if (prediction[6]) {
                return "success";
            }
            else {
                return "danger";
            }
    }

    getPredictions();
    getFoldResults();

});
