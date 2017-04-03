angular.module('app').controller("ViewLearningCurveController", function ($scope,$http) {
    var vm=this;
    vm.learningCurveData=[];
    vm.labels=[];
    vm.values=[];
    
    function setUpLearningCurve() {
            var url="predictions/learningCurve";
            var learningCurveDataPromise=$http.get(url);
        learningCurveDataPromise.then(function (response) {
                vm.learningCurveData=response.data;
            setUpGraphData();
        });
    }
    
    
    function setUpGraphData() {
        for(var i=0;i<vm.learningCurveData.length;i++){
            vm.values.push(vm.learningCurveData[i][2])
            vm.labels.push(vm.learningCurveData[i][3])

        }
        setUpGraph();
    }


    function setUpGraph() {

        var ctx = document.getElementById('myChart2').getContext('2d');
        var myChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: vm.labels,
                datasets: [{
                    label: '% of matchups predicted correctly per data set size',
                    data: vm.values,
                    backgroundColor: "rgba(153,255,51,0.4)"
                }]
            },
            options:{
            scales: {
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: '% Correct'
                    }
                }],
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: 'Data set size'
                    }
                }]
            }
        }
        });
    }


    setUpLearningCurve();

});


/**
 * Created by Oisín on 3/31/2017.
 */
