angular.module('app').controller("ViewAllPredictionsController", function ($scope,$http, $location) {
    var vm=this;
    vm.values=[1,2,3,4];
    vm.labels=["h","h","h","j"];
    vm.predictions=[];
    vm.wl=0;

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
         if(prediction.correct) {
             vm.wl++;
             vm.values.push((vm.wl));
             vm.labels.push("correct");
         }else {
             vm.wl--;
             vm.values.push((vm.wl));
             vm.labels.push("incorrect");
         }
        });
    }
    function setUpGraph() {
        var ctx = document.getElementById('myChart').getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: vm.labels,
                datasets: [{
                    label: 'Prediction  History: Wins - Losses',
                    data: vm.values,
                    backgroundColor: "rgba(153,255,51,0.4)"
                }]
            }
        });
    }

    getPredictions();

});
