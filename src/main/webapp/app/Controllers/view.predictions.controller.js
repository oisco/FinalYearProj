angular.module('app').controller("ViewAllPredictionsController", function ($scope,$http, $location) {
    var vm=this;
    vm.values=[];
    vm.labels=[];
    vm.predictions=[];
    vm.wl=0;
    vm.accuracy=0;
    vm.numCorrect=0;
    vm.totalFights=0;

    function getPredictions() {
        var url="predictions/all/";
        var eventsPromise=$http.get(url);
        eventsPromise.then(function (response) {
            vm.predictions=response.data;
            setUpGraphData();
            setUpGraph();
        })
    }

    function setUpGraphData(){
        angular.forEach(vm.predictions, function (prediction) {
            vm.totalFights++;
            if(prediction[6]) {
             vm.wl++;
             vm.values.push((vm.wl));
             vm.labels.push("");
             vm.numCorrect++;
         }else {
             vm.wl--;
             vm.values.push((vm.wl));
             vm.labels.push("");
         }

        });
        vm.accuracy=(vm.numCorrect/(vm.totalFights/100));
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
