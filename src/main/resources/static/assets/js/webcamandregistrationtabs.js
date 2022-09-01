  var width = 270;    // We will scale the photo width to this
  var height = 270;     // This will be computed based on the input stream

  // |streaming| indicates whether or not we're currently streaming
  // video from the camera. Obviously, we start at false.

  var streaming = false;

  // The various HTML elements we need to configure or control. These
  // will be set by the startup() function.

  var video = null;
  var canvas = null;
  var photo = null;
  var startbutton = null;
  
  
$(document).ready(function(){
		
	$('#camselectstudents').on('change', function() {
		
		
			  
			  if ( this.value == '1'){					
				  
				  $("#videodivstudents").show();
				  startup(uploadimagesoption);
				  
			
			  }
			  else{			 
					$("#videodivstudents").hide();	
					const video = document.querySelector('video1students');
					const mediaStream = video.srcObject;
					if(mediaStream!==null){
						const tracks = mediaStream.getTracks(); 
						tracks[0].stop();
						tracks.forEach(track => track.stop())
					}
					
			  }
	});
	
	
	$('#camselectteachers').on('change', function() {
			
					
			  if ( this.value == '1'){					
				  
				  $("#videodivteachers").show();
				  startup(uploadimagesoption);
			  }
			  else{			 
					$("#videodivteachers").hide();	
					const video = document.querySelector('video1teachers');
					const mediaStream = video.srcObject;
					if(mediaStream!==null){
						const tracks = mediaStream.getTracks(); 
						tracks[0].stop();
						tracks.forEach(track => track.stop())
					}
					
			  }
	});
	
	$('#camselectstaff').on('change', function() {
			
			  if ( this.value == '1'){					
				  
				  $("#videodivstaff").show();
				  startup(uploadimagesoption);
			  }
			  else{			 
					$("#videodivstaff").hide();	
					const video = document.querySelector('video1staff');
					const mediaStream = video.srcObject;
					if(mediaStream!==null){
						const tracks = mediaStream.getTracks(); 
						tracks[0].stop();
						tracks.forEach(track => track.stop())
					}
					
			  }
	});
	
	$('#camselectboard').on('change', function() {
			
				
			  if ( this.value == '1'){					
				  
				  $("#videodivboard").show();
				  startup(uploadimagesoption);
			  }
			  else{			 
					$("#videodivstaff").hide();	
					const video = document.querySelector('video1board');
					const mediaStream = video.srcObject;
					if(mediaStream!==null){
						const tracks = mediaStream.getTracks(); 
						tracks[0].stop();
						tracks.forEach(track => track.stop())
					}
					
			  }
	});
	
		
		
	$('#simagestudents').click(function(){
		$('#myfile'+uploadimagesoption).click()
	})
	$('#simageteachers').click(function(){
		$('#myfile'+uploadimagesoption).click()
	})
	$('#simagestaff').click(function(){
		$('#myfile'+uploadimagesoption).click()
	})
	$('#simageboard').click(function(){
		$('#myfile'+uploadimagesoption).click()
	})
	
});


function readURL(input) {       
       
            if (input.files && input.files[0]) {
                var reader = new FileReader();

                reader.onload = function (e) {
                    $('#simage'+uploadimagesoption).attr('src', e.target.result);
                };

                reader.readAsDataURL(input.files[0]);
				uploadFile(input.files[0],uploadimagesoption);
            }
}
  

  function startup(uploadimagesoption) {
	  
	  console.log("mcee mmn   "+uploadimagesoption+"   "+ uniqueid);
	  
    video = document.getElementById('video1'+uploadimagesoption);
    canvas = document.getElementById('canvas'+uploadimagesoption);
    photo = document.getElementById('simage'+uploadimagesoption);
    startbutton = document.getElementById('startbutton'+uploadimagesoption);

    navigator.mediaDevices.getUserMedia({video: true, audio: false,width: { min: 270, max: 270 },    height: { min: 480, max: 480}}).then(function(stream) {
      video.srcObject = stream;
	  window.stream = stream;
      video.play();
    })
	
    .catch(function(err) {
      console.log("An error occurred: " + err);
    });
	

    video.addEventListener('canplay', function(ev){

		if (!streaming) {       
      
        video.setAttribute('width', width);
        video.setAttribute('height', height);
        canvas.setAttribute('width', width);
        canvas.setAttribute('height', height);
        streaming = true;
		
      }
    }, false);

    startbutton.addEventListener('click', function(ev){
      takepicture();
      ev.preventDefault();
    }, false);
    
    clearphoto();
  }



  // Fill the photo with an indication that none has been
  // captured.

  function clearphoto() {
    var context = canvas.getContext('2d');
    context.fillStyle = "#AAA";
    context.fillRect(0, 0, canvas.width, canvas.height);

    var data = canvas.toDataURL('image/png');
    photo.setAttribute('src', data);
  }
  
 

  function takepicture() {
    var context = canvas.getContext('2d');
    if (width && height) {
      canvas.width = width;
      canvas.height = height;
      context.drawImage(video, 0, 0, width, height);
    
      var data = canvas.toDataURL('image/png');
      photo.setAttribute('src', data);
	  
	  
	  var imgname;
	  if(document.getElementsByName(uniqueid)[0].value==""){
		  imgname="mcee";
	  }else{
		  imgname=document.getElementsByName(uniqueid)[0].value;
	  }

	    urltoFile(data, imgname+'.png','image/png')
       .then(function(file){uploadFile(file,uploadimagesoption);});
		
	  
    } else {
      clearphoto();
    }
  }
  
  
  
  function urltoFile(url, filename, mimeType){
        return (fetch(url)
            .then(function(res){return res.arrayBuffer();})
            .then(function(buf){return new File([buf], filename,{type:mimeType});})
        );
    }
  
  
  
function uploadFile(file,uploadimagesoption) {
	
	var myFormData = new FormData();
	myFormData.append('pictureFile', file);	
	
	if(uniqueid==='adm no'){
		myFormData.append('morepath', "studentimages");	
	}
	else if(uniqueid==='trnu'){
		myFormData.append('morepath', "teacherimages");	
	}
	else if(uniqueid==='idnu'){
		myFormData.append('morepath', "staffimages");	
	}
	
	    
		$.ajax({
            url: "/upload",
            type: "POST",
            data: myFormData,
            processData: false,
            contentType: false,
            cache: false,
            success: function (res) {                
				console.log(res);
				var obj = JSON.parse(res);
				console.log(obj);
				document.getElementById("myimg"+uploadimagesoption).value = obj['path'];
				
				console.log(obj['querystatus']);
				
				$(function () {
						$("#messageid").html(obj['querystatus']);
						$('#myModal').modal('show');
						setTimeout(function () {
							$('#myModal').modal('hide');
						}, 1500);
					});
					
					
				$("#videodiv"+uploadimagesoption).hide();	
					const video = document.querySelector('video');
					const mediaStream = video.srcObject;
					if(mediaStream!==null){
						const tracks = mediaStream.getTracks(); 
						tracks[0].stop();
						tracks.forEach(track => track.stop())
					}	
					
            },
            error: function (err) {
                $(function () {						    
						$("#change-me").html(err);
						$('#myModalError').modal('show');						
					});
            }
        });
    

	
	
}
  
  
  function openCity(evt, cityName) {	
		
          var i, x, tablinks;
          x = document.getElementsByClassName("city");
          for (i = 0; i < x.length; i++) {
            x[i].style.display = "none";
          }
		  
          tablinks = document.getElementsByClassName("tablink");
          for (i = 0; i < x.length; i++) {
            tablinks[i].className = tablinks[i].className.replace(" w3-red", "");
          }
		  
          document.getElementById(cityName).style.display = "block";
		  
          if(evt.currentTarget.className){
			  evt.currentTarget.className += " w3-red";			  
			 }
		  
		  // stop webcam if any
		   
		   
		   if(cityName!='cityanalysis' && cityName!='citymarks' && cityName!='cityreports' ){
		  
			const video = document.querySelector('video');
			const mediaStream = video.srcObject;
			
			if(mediaStream!==null){
				
				const tracks = mediaStream.getTracks(); 
				tracks[0].stop();
				tracks.forEach(track => track.stop())
				document.getElementById('camselect'+uploadimagesoption).value=2;			
			}
			
			$("#videodiv"+uploadimagesoption).hide();
		   
		   }else{

			if(cityName=='citymarks'){
				$('#buttonsubmitmarksasadmin').show();
			}else{
				$('#buttonsubmitmarksasadmin').hide();
			}				
			 
			var table2 = $('#example2').DataTable();			   
			table2.columns.adjust().responsive.recalc();
			var table1 = $('#example3').DataTable();			   
			table1.columns.adjust().responsive.recalc();
			
			md.initDashboardPageCharts();

		   }
			
}



  
