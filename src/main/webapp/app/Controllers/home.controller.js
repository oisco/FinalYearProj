angular.module('app').controller("HomeController", function ($scope,$http, $location,$window) {
    var vm=this;
    vm.events=[];
    vm.news=[];
    vm.nextEvent=null;
    vm.index=0;
    vm.index2=1;

    vm.myInterval = 10000;
    vm.myInterval2 = 7500;
    vm.noWrapSlides = false;
    vm.activeSlide = 0;
    vm.activeSlide2 = 0;

    var url="/events/next"
    var eventsPromise=$http.get(url);
    eventsPromise.then(function (response) {
        vm.events=response.data;
    })
    
    var url="/news/all"
    var newsPromise=$http.get(url);
    newsPromise.then(function (response) {
        vm.news=response.data;
    })

    vm.goToUpcomingEvent = function (eventId) {
        $location.path("upcomingEvents/"+eventId);
    };

    vm.goToArticle=function(article){
        $window.location.href=("http://www.ufc.com/news/"+article[3]);
    }


});
