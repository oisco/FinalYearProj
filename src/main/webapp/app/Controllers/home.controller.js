angular.module('app').controller("HomeController", function ($scope,$http, $location) {
    var vm=this;
    vm.nextEventPic="";
    //slideshow
    //create array of images for slideshow
    var slideimages = new Array()
    slideimages[0] = new Image()
    slideimages[0].src = "http://imagec.ufc.com/http%253A%252F%252Fmedia.ufc.tv%252FUFN_KansasCity%252F033720_KAN_UFCcom_Features_01.jpg?-mw500-mh500-tc1"
    slideimages[1] = new Image()
    slideimages[1].src =  "http://imagec.ufc.com/http%253A%252F%252Fmedia.ufc.tv%252FNashville%252F033531_NAS_UFCcom_Features_01.jpg?-mw500-mh500-tc1"
    slideimages[2] = new Image()
    slideimages[2].src =  "http://imagec.ufc.com/http%253A%252F%252Fmedia.ufc.tv%252FUFN_KansasCity%252F033720_KAN_UFCcom_Features_01.jpg?-mw500-mh500-tc1"
    slideimages[3] = new Image()
    slideimages[3].src =  "http://imagec.ufc.com/http%253A%252F%252Fmedia.ufc.tv%252FNashville%252F033531_NAS_UFCcom_Features_01.jpg?-mw500-mh500-tc1"
    slideimages[4] = new Image()
    slideimages[4].src = "http://imagec.ufc.com/http%253A%252F%252Fmedia.ufc.tv%252FUFN_KansasCity%252F033720_KAN_UFCcom_Features_01.jpg?-mw500-mh500-tc1"
    slideimages[5] = new Image()
    slideimages[5].src =  "http://imagec.ufc.com/http%253A%252F%252Fmedia.ufc.tv%252FNashville%252F033531_NAS_UFCcom_Features_01.jpg?-mw500-mh500-tc1"
    slideimages[6] = new Image()
    slideimages[6].src =  "http://imagec.ufc.com/http%253A%252F%252Fmedia.ufc.tv%252FNashville%252F033531_NAS_UFCcom_Features_01.jpg?-mw500-mh500-tc1"
    var step=0;



    ////get the next upcoming event

    var url="/events/next"
    var eventsPromise=$http.get(url);
    eventsPromise.then(function (response) {
        vm.event=response.data;
        vm.nextEventPic=vm.event.feature_image;
    })

    vm.goToUpcomingEvent = function (eventId) {
        $location.path("upcomingEvents/"+eventId);
    };

     slideshow=function(){

        document.getElementById('toChange').src = slideimages[step].src//update the current image being displayed
        if (step<6)//if image being shown is not the last image in the array
            step++//show the next one
        else
            step=0//otherwise go back to the beginning and show the first image

        //wait 2.5 seconds before continuing
        setTimeout("slideshow()",4500)
    }

    //begin the slideshow
    slideshow();

});
