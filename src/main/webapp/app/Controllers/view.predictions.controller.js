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


    var data = [];
    var limit;
    //
    // function createChartData() {
    //    // limit = 100000;
    //
    //     var y = 0;
    //      var dataSeries = { type: "line" };
    //     var dataPoints = [];
    //
    //     for (var i = 0; i < limit; i++) {
    //         y=accuracyHistory[i];
    //
    //         // y += (Math.random() * 10 - 5);
    //         dateTime = new Date(1960, 08, 15);
    //
    //         // dateTime.setMilliseconds(dateTime.getMilliseconds() + i);
    //         // dateTime.setSeconds(dateTime.getSeconds() + i);
    //         // dateTime.setMinutes(dateTime.getMinutes() + i);
    //         // dateTime.setHours(dateTime.getHours() + i);
    //         dateTime.setDate(dateTime.getDate() + i);
    //         // dateTime.setMonth(dateTime.getMonth() + i);
    //         // dateTime.setFullYear(dateTime.getFullYear() + i);
    //
    //         dataPoints.push({
    //             x: dateTime,
    //             y: accuracyHistory[i]
    //         });
    //     }
    //
    //     dataSeries.dataPoints = dataPoints;
    //     data.push(dataSeries);
    // }

    function getPredictions() {
        var url="predictions/all/";
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.predictions=response.data;
            limit=vm.predictions.length;

            setUpGraphData();//chartjs
            // createChartData();

            setUpGraph();
            createDoughnutChart();

            // displayChart();

        })
    }

    // function displayChart(){
    //     var chart = new CanvasJS.Chart("chartContainer",
    //         {
    //             zoomEnabled: true,
    //             animationEnabled: true,
    //             title:{
    //                 text: "Results on each fold"
    //             },
    //             axisX :{
    //                 labelAngle: -30
    //             },
    //             axisY :{
    //                 includeZero:false
    //             },
    //             data: data
    //         });
    //     chart.render();
    // }

    function createDoughnutChart() {
        var pctCorrect=vm.accuracy;
        var pctIncorrect=100-vm.accuracy;
        var ctx = document.getElementById("accuracyDoughnut");
        var myDoughnutChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ["correct", "incorrect"],
                datasets: [{
                    backgroundColor: [
                        "#2ecc71",
                        "#f4424e"
                    ],
                    data: [pctCorrect,pctIncorrect]
                }]
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
             // vm.values.push((vm.wl));
             vm.values.push((vm.accuracy));
             vm.labels.push("");
             vm.numCorrect++;
         }else {
             vm.wl--;
             // vm.values.push((vm.wl));
                vm.values.push((vm.accuracy));
                vm.labels.push("");
         }


        });

        vm.values.splice(0,vm.values.length);
        vm.values=[50,58,63,64,57,57,59,63,55,60,61,55,59,63,55,60,61,59];
        vm.labels=["","","","","","","","","","",""];
    }
    function setUpGraph() {


        var ctx = document.getElementById('myChart').getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                 labels: vm.labels,
                datasets: [{
                    label: 'Prediction  History',
                    data: vm.values,
                    backgroundColor: "rgba(153,255,51,0.4)"
                }]
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


});
