(function(){
    'use strict';
    var app=angular.module('app',["ngRoute"]);

    app.config(function($routeProvider) {
        $routeProvider

            .when("/allEvents", {
                templateUrl : "/templates/AllEvents.html",
                controller : "EventController as vm"
            })
            .when("/viewEvent/:id", {
                templateUrl : "/templates/EventInfo.html",
                controller : "ViewEventController as vm"
            })
            .when("/allFighters", {
                templateUrl : "/templates/AllFighters.html",
                controller : "FighterController as vm"
            })
            .when("/viewFighter/:id", {
                templateUrl : "/templates/ViewFighter.html",
                controller : "ViewFighterController as vm"
            })
            .when("/viewMatchup/:id", {
                templateUrl : "/templates/ViewMatchup.html",
                controller : "ViewMatchupController as vm"
            })
            .when("/upcomingEvents", {
                templateUrl : "/templates/UpcomingEvents.html",
                controller : "ViewUpcomingEventsController as vm"
            })
            .when("/upcomingEvents/:id", {
                templateUrl : "/templates/ViewUpcomingEvent.html",
                controller : "ViewUpcomingEventController as vm"
            })
            .when("/viewPredictions/:id", {
                templateUrl : "/templates/ViewEventPredictions.html",
                controller : "ViewEventPredictionsController as vm"
            })
            .when("/viewAllPredictions", {
                templateUrl : "/templates/ViewAllPredictions.html",
                controller : "ViewAllPredictionsController as vm"
            })
            .when("/viewUpcomingMatchup/:id", {
                templateUrl : "/templates/ViewUpcomingMatchup.html",
                controller : "ViewUpcomingMatchupController as vm"
            })
            .when("/HowItWorks", {
                templateUrl : "/templates/HowItWorks.html",
            })
            .otherwise({
                templateUrl : "/templates/Home.html",
                controller : "HomeController as vm"
            })
    });

    // service for sharing objects between controllers
    app.service('myservice', function(){
        var obj;

        this.setPersonArray = function(event) {
            this.obj=event;
        };
        this.getPersonArray = function(){
            return this.obj;
        };

        this.clear = function() {
            this.obj = null;
        };
    });


})();


