var idcol;
var id;
var Myurl;
var actionurl;
var rowindex;
var viewtitle;
var tablebeingedited;
var maintable;
var searchitems  =[];
var dataarray;
var rows_selected = [];
var searchedphones  =[];
var img;
var schoollogo;	
var myform;
var schoollogo;
var modaltoopen;
var uniqueid;
var actionsaveorupdate;
var uploadimagesoption;

var collumns;







	$.ajax({
				url : '/api/log/getschoollogo',
		contentType : "application/json",
			 success: function(data){             
			 var object = JSON.parse(data)[0];						
			 schoollogo=object['logo'];
				toDataURL(schoollogo, function(dataUrl) {
				  img=dataUrl;
				});
			 
			}		
	});	


function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
       var ascii = binaryString.charCodeAt(i);
       bytes[i] = ascii;
    }
    return bytes;
 }
 
 

const createdCell = function(cell) {
	let original;
  cell.setAttribute('contenteditable', true)
  cell.setAttribute('spellcheck', false)
  cell.addEventListener("focus", function(e) {
		original = e.target.textContent
	})
  cell.addEventListener("blur", function(e) {
		if (original !== e.target.textContent) {
	    const row = maintable.row(e.target.parentElement)
    	row.invalidate()
		}
  })
}

					
function dynamictable(data,Mydiv,url,tablee,tabletoasignnewrow,Modal,Form) {
	searchitems.length = 0;
	rows_selected.length = 0;				  	
	tablebeingedited=tabletoasignnewrow;	
	maintable=tablee;	
	dataarray=data;	
	myform=Form;
	modaltoopen=Modal;
	
	
	
	
        collumns = Object.keys(data[0]);

		Myurl=url;
		

		$(Mydiv).empty();

		
		if(url=='poststudentdataforreg'){
			for (var i = 0; i < data.length; i++) {
				searchitems.push(data[i].Adm+" "+data[i].Name);
			}						
		}else if(url=='postteacherdataforreg'){
			for (var i = 0; i < data.length; i++) {
				searchitems.push(data[i].Trnu+" "+data[i].Name);
			}						
		}			 		
		else if(url=='poststaffdataforreg'){
			for (var i = 0; i < data.length; i++) {
				searchitems.push(data[i].Idnu+" "+data[i].Name);
			}						
		}
		else if(url=='postboarddataforreg'){
			for (var i = 0; i < data.length; i++) {
				searchitems.push(data[i].Idnu+" "+data[i].Name);
			}						
		}
		else if(url=='getsearchstudents'){
			for (var i = 0; i < data.length; i++) {
				searchitems.push(data[i].Adm+" "+data[i].Name);
			}						
		}
		
		else if(url=='loadlistofstudents'){
			for (var i = 0; i < data.length; i++) {
				searchitems.push(data[i].Name);
			}						
		}


		
		
				
		    var html = '<table  id='+tablee.substring(1)+' class="table table-hover">';
					
					html +='<thead style="" class="text-primary">';
						
						html +='<tr>';	

								if(url!='postexameditmarksforreg' && url!='posttogetperformanceperstream' && url!='postgetsubjectselection' && url!='posttogetmarksasadmin'){
									html +='<td style="width:5px"> <div class="checkbox tiny"><div class="checkbox-container"><input id="select_all" value="1" type="checkbox" /><div class="checkbox-checkmark"></div></div></div> </td>'
								}
								
								for (i=0; i<collumns.length; i++) {														
									
									if(collumns[i]=="id"){									
										html +='<td class="id_event">' + collumns[i]+ '</td>';							
									}
									else if(collumns[i]=="action"){									
										html +='<td class="text-center">' + collumns[i]+ '</td>';																				
									}
									
									else if(collumns[i].includes("mceesub:: ")){									
											html +='<td class="text-left">' + collumns[i].substring(10)+ '</td>';																				
									}									
									else{
										
										if(url=='posttogetmarksasadmin' && collumns[i]!="Name" && collumns[i]!="Adm no" && collumns[i]!="Id"){
											html +='<td class="text-center">' + collumns[i]+ '</td>';
										}else{
											html +='<td>' + collumns[i]+ '</td>';	
										}
																				
									}
							
								}			
							
							
						
						html +='</tr>';
						
					html +='</thead>';					
				
					html +='<tbody>';
					
					
								
								var dtasize=data.length;
								   
								 if(url=="poststudentdataforreg"){
									 
									 viewtitle="View student  profile";
									 
									 if(data.length>50){
									   dtasize=50;
									   }else{
									   dtasize=data.length;
									   }
									 
									}else if(url=="getsearchstudents"){
									 	dtasize=0;
									}  
								
								for (var i = 0; i < dtasize; i++) {
									
									
									html +='<tr>';
										
													if(url!='postexameditmarksforreg'  && url!='posttogetperformanceperstream'  && url!='postgetsubjectselection' && url!='posttogetmarksasadmin'){
														html +='<td></td>'
													}	
										
										      var object = data[i];
											  
											  
											  for (var property in object) {
												
												if(property=="id"){									
													html +='<td class="id_event">' + object[property]+ '</td>';						
												}
												else if(property=="action" || property=="Action"){
													if(url=='postclasstogetfeestructure'){	
														html +="<td class='project-actions text-center'>  <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";																									
													}
													else if(Myurl=='postexammarksforreg'){	
														html +="<td class='project-actions text-center'>  <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>    </td>";																									
													}
													else if(Myurl=='postexamdataforreg'){	
														html +="<td class='project-actions text-center'>    <a id='btnsendsms' class='btn btn-primary btn-sm' href='#' title='"+viewtitle+"'><i class='fas fa-sms'></i></i></a>     <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>     <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";																									
													}
													else{
														html +="<td class='project-actions text-center'>    <a id='btnView' class='btn btn-primary btn-sm' href='#' title='"+viewtitle+"'><i class='fas fa-eye'></i></i></a>     <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>     <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";																									
													}
												}
												else if(property=="status"){									
													html +='<td class="project-state text-left"><span class="badge badge-success">'+object[property]+'</span></td>';																																											
												}else if(property=="profile"){									
													html +='<td class="project_progress"><div class="progress progress-sm"><div class="progress-bar bg-green" role="progressbar" aria-valuenow="57" aria-valuemin="0" aria-valuemax="100" style="width: 57%"></div></div><small>'+object[property]+'</small> </td>';																												
												}
												else if(property=="simage"){									
													html +='<td> <img  style= "width:50px;height:50px; border-radius: 50%; " src='+object[property]+'> </td>';																													
												}
												
												else if(property.includes("mceesub:: ")){									
													html +='<td><input type="number" value="'+object[property]+'" style="max-width:50px;text-align: center"/></td>';										
												}
												
												else {
													
													if(url=='postexamdataforreg' && property=="Exam Name"){
														html +='<td><a href="#" onclick=processandviewexam(this.innerText)>' + object[property]+ '</a></td>';
													}else if(url=='posttogetmarksasadmin' && property!="Name" && property!="Adm no" && property!="Id"){
														html +='<td> <input style="max-width:40px;text-align: center" type="number" value='+object[property]+'></td>';
													}else{											
														html +='<td>' +object[property]+ '</td>';		
													}
										
												}
											  
											  }
									
									html +='</tr>';
									
									
									
								}
					
							
					
					html += '</tbody>';
				
			html += '</table>';
  

				
				$(html).appendTo(Mydiv);
				
			
				if((url=='poststudentdataforreg' || url=='postteacherdataforreg'  || url=='poststaffdataforreg'  || url=='postboarddataforreg') && tablee=='#example1' ){					
				
				   $(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false, "autoWidth": false,pageLength: 8,
					  	  'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,
					 'width':'1%',
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
							 	 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }	
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox" '+((row[2] == '1') ? 'checked' : '')+' /><div class="checkbox-checkmark"></div></div></div>';
					 }}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');
				}
				
				
				
				else if(url=='postteacherdataforreg' && tablee=='#example2'){					
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"aaSorting": [[ 2, 'desc' ]] , "autoWidth": false,pageLength: 5,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
		
				
				}
				
				
				else if(url=='postsmslisttogetphones'){					
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '5', "autoWidth": false,pageLength: 3,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}
				
				else if(url=='getsearchstudents'){					
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"bInfo" : false,"rowHeight": '5', "autoWidth": false,pageLength: 4,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}],
						
						initComplete: function () {
							  var api = this.api();							  
							  if($('#bothorsingleparentselect').val()=="One parent"){
								api.column(7).visible( false );
							 	api.column(8).visible( false );
							  }
							  
							  if($('#bothorsingleparentselect').val()=="Both parents"){
							 	api.column(8).visible( false );
							  }
							  
							}
						
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}
				
				else if(url=='postclasstogetfeestructure'){						
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false, "autoWidth": false,pageLength: 9,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}
				
				 else if( url=='postgetstudentstoinvoice'){
					 
					$(tablee).DataTable({
						  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": true, "autoWidth": false,pageLength: 8,
							'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
						}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');		 
						 
					 
					 
				 }
				 
				 
				 else if(url=='postexamcategorydataforreg' || url=='postsubjectgroupsdataforreg' || url=='postgradesdataforreg'  || url=='postsubjectsdataforreg' ){
					 
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '5', "autoWidth": false,pageLength: 8,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}else if(url=='postremarksdataforreg' ){
					 
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '5', "autoWidth": false,pageLength: 8,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}else if(url=='postgradingsystemforreg' ){					 
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '5', "autoWidth": false,pageLength: 8,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}else if(url=='postexamcategoriesdataforreg' ){					 
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '5', "autoWidth": false,pageLength: 8,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}else if(url=='postexamdataforreg'){					
					
						$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '5', "autoWidth": false,pageLength: 8,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3,4]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}],
					 'rowCallback': function(row, data, index){
						 
							if(data[11]=='Partially Sent'){
									$(row).find('td:nth-child(8)').css('color', 'red');									
									$(row).find('td:nth-child(8)').css('font-weight', 'bold');									
							}
							if(data[11]=='Sent'){
									$(row).find('td:nth-child(8)').css('color', 'green');									
									$(row).find('td:nth-child(8)').css('font-weight', 'bold');									
							}
							if(data[11]=='Not Sent'){
									$(row).find('td:nth-child(8)').css('color', 'orange');									
									$(row).find('td:nth-child(8)').css('font-weight', 'bold');									
							}
							
							if(data[10]=='Open'){
									$(row).find('td:nth-child(7)').css('color', 'grey');									
									$(row).find('td:nth-child(7)').css('font-weight', 'bold');									
							}
							if(data[10]=='Approved'){
									$(row).find('td:nth-child(7)').css('color', 'grey');									
									$(row).find('td:nth-child(7)').css('font-weight', 'bold');									
							}
							
							
						  }
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}
				
				
				else if(Myurl=='postexammarksforreg'){					
					
					$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '5', "autoWidth": false,pageLength: 8,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
						
						
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');	

						var table2 = $(tablee).DataTable();
						
						$(tablee+' tbody').on( 'contextmenu', 'tr', function () {
							
							var data = table2.row( this ).data()[2];
							var examid = table2.row( this ).data()[1];
							
							
							var array = data.split(',');
							var menu =  [];	
							for(var i=0;i<array.length;i++) {
							   menu[i] =  { title: array[i], cmd: array[i]};
						    }
							
							dTRightClick(menu,tablee,url,examid,Modal);
							
						} );
						
					
				
				}
				
				
				
				else if(url=='postexameditmarksforreg'){
					
					
					$(tablee).DataTable({
					  "responsive": true,  "lengthChange": false,"bDestroy": true,"bFilter": false,"rowHeight": '2', "autoWidth": false,pageLength: 7,
						columnDefs: [{
						  targets:'1',
						  className: 'inputs',
						  orderDataType: "dom-text", type: 'string',
						  render: function (data, type, row, meta) {
							if (type === 'filter') {
							  return $(data).val();
							}
							return data;
						  }
						  },{ 'visible': false, 'targets':[0,1,2]}]
					  }).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');	;
					
									
					
					$(tablee).on('keyup', 'input', function(e){						
						
					  if (e.keyCode == 13) {
     
							var table = $(tablee).DataTable();
							var cell = $(this).closest('td');

							
							if($('#outofbox').val().replace(/^\s+|\s+$/g, "").length == 0){
								$(function () {
											$("#change-me").html("please provide out of value");
											$('#myModalError').modal('show');
											setTimeout(function () {
												$('#myModalError').modal('hide');
											}, 2000);
										});
								
								table.cell(cell).data('<input type="number" style="max-width:50px"/>').draw()
							}
							
							else if(parseInt($('#outofbox').val().replace(/^\s+|\s+$/g, ""))<parseInt($(this).val())){
								$(function () {
											$("#change-me").html("Invalid marks");
											$('#myModalError').modal('show');
											setTimeout(function () {
												$('#myModalError').modal('hide');
											}, 2000);
										});
								table.cell(cell).data('<input type="number" style="max-width:50px"/>').draw()
							}
							
							else{
	 
							  //update the input value
							  $(this).attr('value', $(this).val());
							
							  //invalidate the DT cache
							  table.cell($(cell)).invalidate().draw(false);
  

							  var col = table.cell(cell).index().column;
							  $(this).closest('tr').next('tr').find('td').eq(2).find('input').focus();
							  
						   }
					  }
					  
					});

				}
				
				
				
				
				
				else if(url=='posttogetmarksasadmin'){
					
				 
					$(tablee).DataTable({
					   "responsive": false, "lengthChange": false,scrollX: true,'fixedColumns':{left: 2},"bDestroy": true,"bFilter": false, "autoWidth": true,pageLength: 6,
						'columnDefs': [{ 'visible': false, 'targets': [0]},{ "width": "110px", "targets": [2] }						
						]
					  }).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');	;
					
					$(tablee).on('keyup', 'input', function(e){						
						
					  if (e.keyCode == 13) {
     
							var table = $(tablee).DataTable();
							var cell = $(this).closest('td');

							
							 if(parseInt(100)<parseInt($(this).val())){
								$(function () {
											$("#change-me").html("Invalid marks");
											$('#myModalError').modal('show');
											setTimeout(function () {
												$('#myModalError').modal('hide');
											}, 2000);
										});
								table.cell(cell).data('<input type="number" style="max-width:50px"/>').draw()
							}
							
							else{
	 
							  //update the input value
							  $(this).attr('value', $(this).val());
							
							  //invalidate the DT cache
							  table.cell($(cell)).invalidate().draw(false);  

							  var col = table.cell(cell).index().column;
							  $(this).closest('tr').next('tr').find('td').eq((col-1)).find('input').focus();
							  
						   }
					  }
					  
					});

				}
				
				
				
				
				else if(url=='postgetsubjectselection'){					
				
				    $(tablee).DataTable({
					  "responsive": false, "lengthChange": false,scrollX: true,'fixedColumns':{left: 2},"bDestroy": true,"bFilter": false, "autoWidth": false,pageLength: 8,
					  	  'columnDefs' : [{ 'visible': false, 'targets': [0]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,
					 'width':'1%',
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
							 	 	
						 return '';
					 }}],
					 'rowCallback': function(row, data, index){
						 
						 
						 for(var i=3, l = data.length; i < l; i++){
							
								if(data[i]=='NO'){
									$(row).find('td:nth-child('+(i)+')').css('color', 'red');									
									$(row).find('td:nth-child('+(i)+')').css('font-weight', 'bold');									
								}
								if(data[i]=='YES'){
									$(row).find('td:nth-child('+(i)+')').css('color', 'green');
									$(row).find('td:nth-child('+(i)+')').css('font-weight', 'bold');									
								}
							}
							
							
						  }
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');
				}
				
				else if(url=='posttogetperformanceperstream'){					
				
				  $(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,searching: false,sortable: false, info: false,"bInfo" : false, "autoWidth": false,pageLength: 4,
					    
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');	
				}
				
				
				
				
				else{					
					
					$(tablee).DataTable({
					  "responsive": true, "lengthChange": false,"bDestroy": true,"aaSorting": [[ 2, 'desc' ]] , "autoWidth": false,pageLength: 8,
					    'columnDefs' : [{ 'visible': false, 'targets': [1,2,3]},{
					 'targets': 0,
					 'searchable':false,
					 'orderable':false,					 
					 'width':'1%',					 
					 'className': 'dt-body-center',
					 'render': function ( data, type, row ){
								 if(row[2] == '1'){									 
									 var rowId = row[3];									
									 var index = $.inArray(rowId, rows_selected);
									if(index === -1){
										rows_selected.push(rowId);									
									 }
								 }								 
						 return '<div class="checkbox tiny"><div class="checkbox-container"><input type="checkbox"  '+((row[2] == '1') ? 'checked' : '')+'  /><div class="checkbox-checkmark"></div></div></div>';
						}}]
					}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');					
				
				}
				
				
				
				
				  var table = $(tablee).DataTable();
				  $(tablee+' tbody').on('click', 'td', function() {					  
					  
					  var tr = $(this.closest('tr'));
					  
					 if(url=='postgetsubjectselection' && $(this).index()>1){
							if(table.cell( this ).data()=='NO'){
								
							  table.cell(this).data("YES");
							  $(tr).find('td:nth-child('+($(this).index()+1)+')').css('color', 'green');									
																  
							}else{
								
							  table.cell(this).data("NO");	
							  $(tr).find('td:nth-child('+($(this).index()+1)+')').css('color', 'red');
							}
					  }
					
				   });
				   
				   
				   
				   
				   
				   
				   
				   
				   
				
				 // Handle click on one checkbox
				 
				   $(tablee+' tbody').on('click', 'input[type="checkbox"]', function(e){
					  
					  var $row = $(this).closest('tr');

				 
					  // Get row data
					  var data = $(tablee).DataTable().row($row).data();

					  // Get row ID
						var rowId;
						if (data != null){
							 rowId = data[3];
						}
					  
					  // Determine whether row ID is in the list of selected row IDs 
					  var index = $.inArray(rowId, rows_selected);

					  // If checkbox is checked and row ID is not in list of selected row IDs
					  if(this.checked && index === -1){
						 rows_selected.push(rowId);						
								
								
								if(url=='postsmslisttogetphones'){					
									openTestModal()								 
								}
								
								if(url=='postexamdataforreg'){					
									$("#moreactonscomboboxdiv").show();												 
								}
								
					  // Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
					  } else if (!this.checked && index !== -1){
						 rows_selected.splice(index, 1);
							if(url=='postsmslisttogetphones'){					
									openTestModal()								 
							}
							
							if(url=='postexamdataforreg'){					
									$("#moreactonscomboboxdiv").hide();												 
							}
					  }

					  if(this.checked){
						 $row.addClass('selected');
					  } else {
						 $row.removeClass('selected');
					  }

					  // Update state of "Select all" control
					  if(url!='postexameditmarksforreg'){
						updateDataTableSelectAllCtrl($(tablee).DataTable());
					  }
					  // Prevent click event from propagating to parent
					  e.stopPropagation();
				   });
						
						
					 // Handle click on table cells with checkboxes
						$(tablee).on('click', 'tbody td, thead th:first-child', function(e){
						  $(this).parent().find('input[type="checkbox"]').trigger('click');
					   });	
				
				
						
				// Handle click on "Select all" control
					   $('thead input[id="select_all"]', $(tablee).DataTable().table().container()).on('click', function(e){
						  if(this.checked){
							 $(tablee+' tbody input[type="checkbox"]:not(:checked)').trigger('click');
						  } else {
							 $(tablee+' tbody input[type="checkbox"]:checked').trigger('click');
						  }

						  // Prevent click event from propagating to parent
						  e.stopPropagation();
					   });
					   
					   
					   // Handle table draw event
					   $(tablee).DataTable().on('draw', function(){
						  if(url!='postexameditmarksforreg' && url!='posttogetmarksasadmin'){
							updateDataTableSelectAllCtrl($(tablee).DataTable());
						  }
					   });
				
				
				
				
				
				
				$(tablee).on('click','#btnsendsms',function(){
					
						rowindex=$(this).closest('tr').index();
						var table = $(tablee).DataTable();
						var tr = $(this.closest('tr'));
						if(tr.hasClass('child')){
							tr = tr.prev();
						}
						rowindex=table.row(tr).index();						
						var data = table.row(tr).data();						
						data.forEach(function(d, index, arr) {
							d = $('<div>').html(d);
							arr[index] = d.val() || d.text()
						  })					  
						
						id=data[3];
						
						Myurl='sendexamresults';
						openTestModalsendsms();	


						
									
				});
				
				
				
				$(tablee).on('click','#btnEdit',function(){
					
						rowindex=$(this).closest('tr').index();
						var table = $(tablee).DataTable();
						var tr = $(this.closest('tr'));
						if(tr.hasClass('child')){
							tr = tr.prev();
						}
						rowindex=table.row(tr).index();						
						var data = table.row(tr).data();						
						data.forEach(function(d, index, arr) {
							d = $('<div>').html(d);
							arr[index] = d.val() || d.text()
						  })
						  
						
						id=data[3];
						
						
						if(url=='poststudentdataforreg'){
							dynamicformsubmit(Form,"/api/reg/getstudent",id,'Londonstudents',Modal);
						}else if(url=='postteacherdataforreg'){
							dynamicformsubmit(Form,"/api/reg/getteacher",id,'Londonteachers',Modal);
						}else if(url=='poststaffdataforreg'){
							dynamicformsubmit(Form,"/api/reg/getstaff",id,'Londonstaff',Modal);
						}else if(url=='postboarddataforreg'){
							dynamicformsubmit(Form,"/api/reg/getboard",id,'Londonboard',Modal);
						}
						
						
						
						else if(url=='postexamdataforreg'){
							dynamicformsubmit(Form,"/api/exam/getexam",id,'searchdata',Modal);
						}else if(url=='postgradesdataforreg'){
							dynamicformsubmit(Form,"/api/exam/getgrade",id,'searchdata',Modal);
						}else if(url=='postsubjectgroupsdataforreg'){
							dynamicformsubmit(Form,"/api/exam/getsubjectgroup",id,'searchdata',Modal);
						}else if(url=='postsubjectsdataforreg'){
							dynamicformsubmit(Form,"/api/exam/getsubject",id,'searchdata',Modal);
						}else if(url=='postexamcategoriesdataforreg'){
							dynamicformsubmit(Form,"/api/exam/getexamcategory",id,'searchdata',Modal);
						}else if(url=='postremarksdataforreg'){
							dynamicformsubmit(Form,"/api/exam/getremark",id,'searchdata',Modal);
						}else if(url=='postgradingsystemforreg'){
							dynamicformsubmit(Form,"/api/exam/getgradingsystem",id,'searchdata',Modal);
						}
						
						
						
									
				});
					
				
				
				$(tablee+' tbody').on('click', '#btnDelete', function(){						
						var table = $(tablee).DataTable();
						var tr = $(this.closest('tr'));
						if(tr.hasClass('child')){
							tr = tr.prev();
						}
						
						rowindex=table.row(tr).index();						
						var data = table.row(tr).data();						
						data.forEach(function(d, index, arr) {
							d = $('<div>').html(d);
							arr[index] = d.val() || d.text()
						  })
						
						id=data[3];												
					
					  	$('#myModaldelete').modal({
							backdrop: 'static',
							keyboard: false
						});	
				});
				
				
				
				
				$(tablee+' tbody').on('click', '#btnView', function(){
						
						var table = $(tablee).DataTable();
						var tr = $(this.closest('tr'));
						if(tr.hasClass('child')){
							tr = tr.prev();
						}
						rowindex=table.row(tr).index();						
						var data = table.row(tr).data();						
						data.forEach(function(d, index, arr) {
							d = $('<div>').html(d);
							arr[index] = d.val() || d.text()
						  })						
						id=data[3];					 


					// view profile


				});
				
				
				  $(tablee).on('click', '#btnremovestudentfromtableonly', function() {
							var table = $(tablee).DataTable();
							var row = $(this).parents('tr');
						 
							if ($(row).hasClass('child')) {
							  table.row($(row).prev('tr')).remove().draw();
							} else {
							  table
								.row($(this).parents('tr'))
								.remove()
								.draw();
							}
						 
						 });
						

				
				
			
}


function  stopsendexam(){

				var indexed_array = {};
					indexed_array['stop'] = 1
							

						 $.ajax({
							 
								type : "POST",
								contentType : "application/json",
								url : "/api/exam/stopsendng",
								data : JSON.stringify(indexed_array),
								dataType : 'json',
								success : function(data) {
										   
											var object = data[0];
										
											$("#modalprogresssendsms").modal("hide");
						
									
											var table = $(maintable).DataTable();
											table.cell({row:rowindex, column:11}).data('Partially sent');
								
											$(table.cell(rowindex,11).node()).css({'color':'red'})
											


											$(function () {
															$("#errormessage").html(object['querystatus']);
															$('#myModalErrorstop').modal('show');
															setTimeout(function () {
																$('#myModalErrorstop').modal('hide');
															}, 2000);
											});
										
									
									
								}		
						});	 
	   

}




function  selectexammarksbystream(){	
	Myurl='selectexammarksbystream';
	openTestModal();
}


var examname;

function  processandviewexam(exam){
	
		examname=exam;
		Myurl='loadexamdetails';
		openTestModal();
		openCity(event,'cityanalysis');
		
		
		$('#popupeexamdetails').modal({
			backdrop: 'static',
			keyboard: false
		});
	
}








function  dTRightClick(menu2,tablee,Myurl,examid,Modal){
	
	$(document).ready( function () {

	var menu=menu2;
	
	$(document).contextmenu({
								delegate: ".dataTable td",
								menu,
								select: function(event, ui) {
									
									
									if(Myurl=='postexammarksforreg'){
										var indexed_array = {};
											indexed_array['examid'] = examid;
											indexed_array['classandstream'] = ((ui.cmd).substring(1));
	
										fetchdynamicdata("/api/exam/getexamstudents",indexed_array,Modal);							
									}
											
								},
								beforeOpen: function(event, ui) {
									var $menu = ui.menu,
										$target = ui.target,
										extraData = ui.extraData;
									ui.menu.zIndex(9999);
								}
							  });
						
	 });
	
}






function fetchdynamicdata(actionurl,indexed_array,Modal){
	
					
		$.ajax({
			
			type : "POST",
			contentType : "application/json",
			url : actionurl,
			data : JSON.stringify(indexed_array),
			dataType : 'json',
			success : function(result) {
	
				
				if(Myurl=='postexammarksforreg'){
					
					$("#example2").DataTable().destroy(true);																											
					dynamictable(result,'#div1entermarks','postexameditmarksforreg','#example2','#example2');
				
					$(Modal).modal({
						backdrop : 'static',
						keyboard : false
					});
					
				}
				
				Myurl='postexammarksforreg'
	
	
													
			},
			error : function(e) {
				console.log("ERROR: ", e);
			}
		});
	
}









function updateDataTableSelectAllCtrl(table){	
	
   var $table             = table.table().node();
   var $chkbox_all        = $('tbody input[type="checkbox"]', $table);
   var $chkbox_checked    = $('tbody input[type="checkbox"]:checked', $table);
   var chkbox_select_all  = $('thead input[id="select_all"]', $table).get(0);

   // If none of the checkboxes are checked
   if($chkbox_checked.length === 0){
      chkbox_select_all.checked = false;
      if('indeterminate' in chkbox_select_all){
         chkbox_select_all.indeterminate = false;
      }

   // If all of the checkboxes are checked
   } else if ($chkbox_checked.length === $chkbox_all.length){
      chkbox_select_all.checked = true;
      if('indeterminate' in chkbox_select_all){
         chkbox_select_all.indeterminate = false;
      }

   // If some of the checkboxes are checked
   } else {
		 chkbox_select_all.checked = false;
      if('indeterminate' in chkbox_select_all){
         chkbox_select_all.indeterminate = true;
      }
   }
}




$("#search").autocomplete({
	
	
		source: function (request, response) {
                    var results = $.ui.autocomplete.filter(searchitems, request.term);
                    response(results.slice(0, 10));
                },
		minLength: 1,
		select: function (event, ui) {
		
				let object ;
				
				if(Myurl=='poststudentdataforreg'){		
					object= dataarray.find(o => o.Adm === (ui.item.value).split(' ')[0]);
				}else if(Myurl=='postteacherdataforreg'){		
					object= dataarray.find(o => o.Trnu === (ui.item.value).split(' ')[0]);					
				}
				else if(Myurl=='poststaffdataforreg'){		
					object= dataarray.find(o => o.Idnu === (ui.item.value).split(' ')[0]);					
				}
				else if(Myurl=='postboarddataforreg'){		
					object= dataarray.find(o => o.Idnu === (ui.item.value).split(' ')[0]);					
				}
				else if(Myurl=='getsearchstudents'){		
					object= dataarray.find(o => o.Adm === (ui.item.value).split(' ')[0]);					
				}
				
				var html ='<tr>';
										
												html +='<td></td>'
											
											  for (var property in object) {
												
												if(property=="id"){									
													html +='<td style="display:none" class="id_event">' + object[property]+ '</td>';						
												}
												else if(property=="action"){									
													html +="<td class='project-actions text-center'>    <a id='btnView' class='btn btn-primary btn-sm' href='#' title='"+viewtitle+"'><i class='fas fa-eye'></i></i></a>     <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>     <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";																									
												}
												else if(property=="status"){									
													html +='<td class="project-state text-left"><span class="badge badge-success">'+object[property]+'</span></td>';																																											
												}else if(property=="profile"){									
													html +='<td class="project_progress"><div class="progress progress-sm"><div class="progress-bar bg-green" role="progressbar" aria-valuenow="57" aria-valuemin="0" aria-valuemax="100" style="width: 57%"></div></div><small>'+object[property]+'</small> </td>';																												
												}
												else if(property=="simage"){									
													html +='<td> <img  style= "width:50px;height:50px; border-radius: 50%; " src='+object[property]+'> </td>';																													
												}
												else{ 	
													html +='<td>' +object[property]+ '</td>';										
												}											  
											  
											  }
									
						html +='</tr>';
						
					 var dataTable = $(tablebeingedited).DataTable();
						dataTable.clear();
						const tr = $(html);
						dataTable.row.add(tr[0]).draw();
						$("#search").blur(); 
						$('#search').val('');
			return false;
		  }
		  
});	  
$("#search").autocomplete("widget").attr('style', 'max-height: 400px;max-width: 188px;font-size:12px;overflow-y: auto; overflow-x: hidden;')


$("#searchstudents").autocomplete({
		source: function (request, response) {
                    var results = $.ui.autocomplete.filter(searchitems, request.term);
                    response(results.slice(0, 10));
                },
		minLength: 1,		
		appendTo : "#popupsearchstudent",
		select: function (event, ui) {
			  
				
				let object ;
				var string;
				var substring;
				
				 if(Myurl=='getsearchstudents'){		
					object= dataarray.find(o => o.Adm === (ui.item.value).split(' ')[0]);
				 }
				 
				 string = $(tablebeingedited).DataTable().search(object['Adm']).row({search: 'applied'}).data()+"";
				 substring = object['Adm']+"";
				
				if(!string.includes(substring)){
						
				var html ='<tr>';
										
												html +='<td></td>'
											
											  for (var property in object) {
												
												if(property=="action"){									
													html +="<td class='project-actions text-center'>  <a id='btnremovestudentfromtableonly'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";																									
												}
												
												else{ 	
													html +='<td>' +object[property]+ '</td>';										
												}											  
											  
											  }
									
						html +='</tr>';
						
					 var dataTable = $(tablebeingedited).DataTable();
						//dataTable.clear();
						const tr = $(html);
						dataTable.row.add(tr[0]).draw();
						$("#searchstudents").blur(); 
					
				}
					
					$(this).val(''); return false;
		
		}		
});	  
$("#searchstudents").autocomplete("widget").attr('style', 'max-height: 400px;max-width: 265px;font-size:12px;overflow-y: auto; overflow-x: hidden;')









$("#searchstudentreport").autocomplete({
		source: function (request, response) {
                    var results = $.ui.autocomplete.filter(searchitems, request.term);
                    response(results.slice(0, 10));
                },
		minLength: 1,		
		appendTo : "#modalgeneratereports",
		select: function (event, ui) {
			    Myurl="fetchleavingcert";

			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			indexed_array['admno'] = (ui.item.value).split(' ')[0];				
		
			$.ajax({
					
					type : "POST",
					contentType : "application/json",
					url : "/api/reg/getreports/leavingcert",
					data : JSON.stringify(indexed_array),
					success: function(data){						
						
						var sampleArr = base64ToArrayBuffer(data);
						var file = new Blob([sampleArr], {type: 'application/pdf'});
						var fileURL = URL.createObjectURL(file);
						window.open(fileURL);						
						
						$("#modalgeneratereports").modal("hide");

					},
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError+"  mceee  "+xhr);
					}		
			});

		}		
});	  
$("#searchstudentreport").autocomplete("widget").attr('style', 'max-height: 400px;max-width: 265px;font-size:12px;overflow-y: auto; overflow-x: hidden;')




var requestdateandtransnu;
var allowbackdating;
var includebalinrct;
var typeofreceipt;

$("#searchstudentsfeepayment").autocomplete({
		source: function (request, response) {
                    var results = $.ui.autocomplete.filter(searchitems, request.term);
                    response(results.slice(0, 10));
                },
		minLength: 1,		
		appendTo : modaltoopen,
		select: function (event, ui) {
			
			Myurl="fetchstudentfeebalance";

			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			indexed_array['studentid'] = (ui.item.value).split('  ')[0];				
		
			$.ajax({
					
					type : "POST",
					contentType : "application/json",
					url : "fetchstudentfeebalance",
					data : JSON.stringify(indexed_array),
					success: function(data){						
						var object = JSON.parse(data)[0];					
							$("#studentname").html(object['name']);
							$("#studentclasss").html(object['cls']);
							$("#studentrctnu").val(object['reference']);
							$("#studentamount").html(numberWithCommas(object['BAL']));
							
							
							$("#studentbf").html(numberWithCommas(object['BF']));
							$("#studenttermfees").html(numberWithCommas(object['TermFees']));
							$("#studenttpayable").html(numberWithCommas(object['Payable']));
							$("#studentfeespaid").html(numberWithCommas(object['PAID']));
							//$("#studentfeesrefunds").html(numberWithCommas(object['BAL']));
							$("#studentfeebal").html(numberWithCommas(object['BAL']));
							
							requestdateandtransnu=object['requestdateandtransnu'];
							allowbackdating=object['allowbackdating'];
							includebalinrct=object['includebalinrct'];
							typeofreceipt=object['typeofreceipt'];
							
					
					},
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError);
					}		
			});

		}		
});	  
$("#searchstudentsfeepayment").autocomplete("widget").attr('style', 'max-height: 400px;max-width: 185px;font-size:12px;overflow-y: auto; overflow-x: hidden;')




var reportTitle;
var listofstudentsautocomplete;


var extrareportpath;

function myFunction(reporttitle) {
	
	 reportTitle=reporttitle;
	 $('#toglebutt').click();
	 $('#reporttitle').html(reporttitle);
  
  if(reporttitle=="class lists report"){
	  
	  
	  
	$('#reportmodalbutt').show();
	$('#selectyeardiv').hide(); 
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	
	$('#reservationdiv').hide();
	$('#selectclassdiv').show()
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="classlists";
	
  }
  
  
  else if(reporttitle=="empty class lists"){
	  
	$('#reportmodalbutt').show();
	$('#selectyeardiv').hide(); 
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	
	$('#reservationdiv').hide();
	$('#selectclassdiv').show()
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="emptyclasslists";
	
  }
  
  
  
  else if(reporttitle=="detailed list of students"){
	$('#reportmodalbutt').show();
	$('#selectyeardiv').hide(); 
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	
	$('#reservationdiv').hide();
	$('#selectclassdiv').show()
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="detailedlist";
	
  }
  
  
  
  else if(reporttitle=="parents list"){
	$('#reportmodalbutt').show();
	$('#selectyeardiv').hide(); 
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	
	$('#reservationdiv').hide();
	$('#selectclassdiv').show()
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="parentlist";
	
  }
  
  
  else if(reporttitle=="Transfered students"){
	  
  	$('#reportmodalbutt').show();
	$('#selectyeardiv').hide();  
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
    $('#reservationdiv').show();  
	$('#selectclassdiv').hide();
	$('select[name=selectreportval] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="transferedstudents";
	
	
  }
  
  else if(reporttitle=="List of teachers"){
 	$('#reportmodalbutt').show();
	$('#selectyeardiv').hide();  
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	$('#reservationdiv').hide();  
	$('#selectclassdiv').hide();
	$('select[name=selectreportval] option:eq(2)').attr('selected', 'selected');
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	
	extrareportpath="listofteachers";
	
	
  }
  
  
  else if(reportTitle=="Support staff"){
 	$('#reportmodalbutt').show();
	$('#selectyeardiv').hide();  
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	$('#reservationdiv').hide();  
	$('#selectclassdiv').hide();
	$('select[name=selectreportval] option:eq(2)').attr('selected', 'selected');
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	
	extrareportpath="listofstaff";
	
	
  }
  
  else if(reporttitle=="List of allumni"){
 	$('#reportmodalbutt').show();
	$('#selectyeardiv').show();  
	$('#hosteldiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	$('#reservationdiv').hide();  
	$('#selectclassdiv').hide();
	$('select[name=selectreportval] option:eq(2)').attr('selected', 'selected');
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="allumni";
	
	
  }
  
  else if(reporttitle=="Hostel"){
	  
 	$('#reportmodalbutt').show();
	$('#hosteldiv').show();  
	$('#selectyeardiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#religiondiv').hide(); 
	
	$('#reservationdiv').hide();  
	$('#selectclassdiv').hide();
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=selectreportval] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="hostel";
	
						
  }
  
 
  
  else if(reporttitle=="group list"){ 
  
	$('#reportmodalbutt').show();
	$('#selectclassdiv').show();
	$('#religiondiv').show();  
	$('#selectyeardiv').hide(); 
	$('#searchstudentreportdiv').hide(); 
	$('#hosteldiv').hide(); 
	
	$('#reservationdiv').hide();  
	$('#selectyear').val(new Date().toDateInputValue());
	$('select[name=selectreportval] option:eq(2)').attr('selected', 'selected');
	$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
	$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
	$('#searchstudentreport').val("456");
	
	extrareportpath="listperdenomination";
	
  
  }  
	 
	 
	 
 else if(reporttitle=="Generate leaving cert"){ 	  	  
 	
	searchitems.length = 0;
	  
	  $.ajax({
				type : "POST",
				contentType : "application/json",
				url : '/getstudentautocompletedata',
						success: function(data){ 
						listofstudentsautocomplete = JSON.parse(data);
						for (var i = 0; i < listofstudentsautocomplete.length; i++) {
							searchitems.push(listofstudentsautocomplete[i].adm);
						}
						
						$('#searchstudentreportdiv').show(); 
						$('#hosteldiv').hide();  
						$('#selectyeardiv').hide();
						$('#reportmodalbutt').hide();
						$('#religiondiv').hide(); 
	
						
						$('#reservationdiv').hide();  
						$('#selectclassdiv').hide();
						$('#selectyear').val(new Date().toDateInputValue());
						$('select[name=hostel] option:eq(2)').attr('selected', 'selected');
						$('select[name=selectreportval] option:eq(2)').attr('selected', 'selected');
						$('select[name=religionreportselect] option:eq(2)').attr('selected', 'selected');
						
						
						extrareportpath="leavingcert";
	
	
					}		
	   });
	   
  }	 




	$('#modalgeneratereports').modal({
		backdrop: 'static',
		keyboard: false
	});
		
			
		
}



$(document).ready(function() {	
	$('#confirmdeletebutt').click(function(e){
			
		if(Myurl=='poststudentdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/reg/deletestudent");
		}else if(Myurl=='postteacherdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/reg/deleteteacher");
		}else if(Myurl=='poststaffdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/reg/deletestaff");
		}else if(Myurl=='postboarddataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/reg/deleteboard");
		}
		
		
		else if(Myurl=='postexamdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/exam/deleteexam");
		}else if(Myurl=='postgradesdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/exam/deletegrade");
		}else if(Myurl=='postsubjectgroupsdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/exam/deletesubjectgroup");
		}else if(Myurl=='postsubjectsdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/exam/deletesubject");
		}else if(Myurl=='postexamcategoriesdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/exam/deleteexamcategory");
		}else if(Myurl=='postremarksdataforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/exam/deleteremark");
		}else if(Myurl=='postgradingsystemforreg'){
			dynamicdeleteitem(id,tablebeingedited,"/api/exam/deletegradingsystem");
		}
		
		

	   
	   e.preventDefault(); 
	});
});




$( document ).ready(function() {
$('#showreportForm').submit(function(event) {		
    Myurl="showreports";
	openTestModal();
	event.preventDefault();	
	$('#modalgeneratereports').modal('toggle'); 	
	});  	
})



function fetchreport(extrapath) {	        


		var indexed_array = {};
			
			indexed_array['value'] = $('#selectval').val();	
			indexed_array['daterange'] =new Date($('#reservation').val().split(' - ')[0]).toISOString().split('T')[0]+" - "+new Date($('#reservation').val().split(' - ')[1]).toISOString().split('T')[0];		
			indexed_array['year'] = $('#selectyear').val();	
			indexed_array['hostel'] = $('#hostel').val();	
			indexed_array['admno'] = $('#searchstudentreport').val();	
			indexed_array['group'] = $('#religionreportselect').val();	
			
			$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "/api/reg/getreports/"+extrapath,
					data : JSON.stringify(indexed_array),
					dataType : 'json',
					success: function(data){ 
							
								var object = data[0];;					
							
								if(object['querystatus']=="No records found"){						

										$("#modalprogress2").modal("hide");
					
										$(function () {
											$("#messageid").html(object['querystatus']);
											$('#myModal').modal('show');
											setTimeout(function () {
												$('#myModal').modal('hide');
											}, 2000);
										});
								
								}
								
								else{
											$("#modalshowreporttitle").html($("#selectval option:selected").html()+" "+document.getElementById("reporttitle").innerHTML);
																
											$('#modalshowreport').modal({
												backdrop: 'static',
												keyboard: false
											});	
											 $('#reporttitlemain').html(document.getElementById("reporttitle").innerHTML);

											dynamictablereport(data,'#divtoholdreport','#example2');
											$("#modalprogress2").modal("hide");
											
								}
								
						 
							
					}		
			});

}









function exammoreactions() {
			
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};			
			
			
			if($('#exammoreactionscombo').val()==5){
			indexed_array['exams'] = rows_selected;			
			
			$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "/api/exam/refreshexam",
					data : JSON.stringify(indexed_array),
					dataType : 'json',
					success: function(data){
							        	
							var object = data[0];

								console.log(object+"    mcee");
							
										if(object['querystatus']=="Operation successfull"){

											$(function () {
												$(modaltoopen).modal('hide');
												$("#messageid").html(object['querystatus']);
												$('#myModal').modal('show');
												setTimeout(function () {
													$('#myModal').modal('hide');
												}, 1000);
											});
						
										}
								
								

							}		
			});
			
			}

}





function fetchsmsphonenumbers() {
			
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};			
			
			indexed_array['groups'] = rows_selected;			
			indexed_array['bothorsingleparents'] = $('#bothorsingleparentselect').val();;			
			
			$.ajax({
					type : "POST",
					contentType : "application/json",
					url : "/api/sms/postsmslisttogetphones",
					data : JSON.stringify(indexed_array),
					success: function(data){
							        	
									var words = data.split(",").filter(item => item);
									$('#smstotalrecipients').html(numberWithCommas(words.length));
								
									$('#phonenumberstextarea').val(data); 

									$("#modalprogress2").modal("hide");

							}		
			});

}






function openTestModal(){
    $('#modalprogress2').modal({
        keyboard: false,
        backdrop: 'static'
    });
}



function openTestModalsendsms(){
    $('#modalprogresssendsms').modal({
        keyboard: false,
        backdrop: 'static'
    });
}




$('#modalprogresssendsms').on('shown.bs.modal', function (e) {

		if(Myurl=='sendexamresults'){ 
	  
	   
				var indexed_array = {};
					indexed_array['id'] = id
					indexed_array['stop'] = 0
					
							

						 $.ajax({
							 
								type : "POST",
								contentType : "application/json",
								url : "/api/exam/sendexam",
								data : JSON.stringify(indexed_array),
								dataType : 'json',
								success : function(data) {
										   
									var object = data[0];
									
											$("#modalprogresssendsms").modal("hide");
				
										if(object['querystatus']=="Message sent successfully"){						

											$(function () {
												$(modaltoopen).modal('hide');
												$("#messageid").html(object['querystatus']);
												$('#myModal').modal('show');
												setTimeout(function () {
													$('#myModal').modal('hide');
												}, 1000);
											});
						
											var table = $(maintable).DataTable();
											table.cell({row:rowindex, column:11}).data('Sent');
											$(table.cell(rowindex,11).node()).css({'color':'green'})
											
										}	
									
								}		
						});	 
	   
	   
	   
	   }



});




$('#modalprogress2').on('shown.bs.modal', function (e) {
   
   
   
   
   
		if(Myurl=='showreports'){
			fetchreport(extrareportpath);		   	   
		}
	   
	   else if(Myurl=='postsmslisttogetphones'){		   
		fetchsmsphonenumbers();		   
		}
	   
	   else if(Myurl=='sendthattext'){
				
					var 	indexed_array = {};
							indexed_array['sms'] = $('#messagetextarea').val();	
							indexed_array['recipients'] = $('#messagetextarea').val();	
			
		
				$.ajax({
					
					
					type : "POST",
					contentType : "application/json",
					url : "/api/sms/sendthattext",
					data : JSON.stringify(indexed_array),
					success: function(data){						
						var object = JSON.parse(data)[0];					
						
						$('#modalprogress2').modal('hide');
						$('#smstotalsent').html(object['alredysent']); 

						if(object['querystatus']=="Message sent successfully"){						

								$('#phonenumberstextarea').val(''); 
								$('#messagetextarea').val(''); 
								$('#smstotalsent').val(''); 
								$('#smstotalrecipients').val(''); 
								$('#smsperperson').val(''); 
								
								$(tablebeingedited).DataTable().$("input[type=checkbox]").prop("checked", false);


							$(function () {
								$("#messageid").html(object['querystatus']);
								$('#myModal').modal('show');
								setTimeout(function () {
									$('#myModal').modal('hide');
								}, 2000);
							});
						
						Myurl='postsmslisttogetphones';
 
						}else{
												
							$(function () {
								$("#change-me").html(object['querystatus']);
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
							});
						
						}
						


					},
					error:function (xhr, ajaxOptions, thrownError) {
						$(function () {
							$("#change-me").html(thrownError);
							$('#myModalError').modal('show');
							setTimeout(function () {
								$('#myModalError').modal('hide');
							}, 5000);
						});
					}		
			});
				
	   }
	   
	   else if(Myurl=="checksmsaccbalance"){
		   		   
		  $.ajax({
				url : '/api/sms/getaccoutbal',
				contentType : "application/json",
				success: function(data){
					Myurl='postsmslisttogetphones';
					$('#modalprogress2').modal('hide');
					$('#accountbal').html('BAL | '+JSON.parse(data)[0].smsaccbal);
				},
					error:function (xhr, ajaxOptions, thrownError) {
					Myurl='postsmslisttogetphones';

				}	

					
				
			});			   
		   
	   }
	   
	   else if(Myurl=='getsearchstudents'){

											   
						
			$.ajax({
							url : '/api/sms/getsearchstudents',
							contentType : "application/json",
									success: function(data){ 

									$("#example2").DataTable().destroy(true);												
													
										$('#popupsearchstudent').modal({
											backdrop: 'static',
											keyboard: false
										});	
										
										dynamictable(JSON.parse(data),'#searchstudentsdiv','getsearchstudents','#example2','#example2');
										$("#modalprogress2").modal("hide");
								
									
								}		
			});
			

	   }
	   
	   // Finance    
	   
	   else if(Myurl=='postclasstogetfeestructure'){			
				
			
							var indexed_array = {};
							indexed_array['classs'] = $('#feestructureselectclasscombo').val();	
							indexed_array['term'] = $('#feestructureterm').val();	
							indexed_array['year'] = $('#feestructureyear').val();	
							
								
						$.ajax({
							
							
							type : "POST",
							contentType : "application/json",
							url : Myurl,
							data : JSON.stringify(indexed_array),
							dataType : 'json',
							success: function(data){  
									
									$("#example2").DataTable().destroy(true);																											
										dynamictable(data,'#div1feestructure','postclasstogetfeestructure','#example2','#example2');
										$("#modalprogress2").modal("hide");
										var totalAmount = 0;
										for (var i = 0; i < $(tablebeingedited).DataTable().column(5).data().length; i++) {
											totalAmount += parseFloat($(tablebeingedited).DataTable().column(5).data()[i]);
										}
										$("#totalfeestructureamt").html('Total  '+numberWithCommas(totalAmount));
							
									
								}		
						});
			

	   }
	   
	   else if(Myurl=='postaddinvoiceitem'){
		   
		   var indexed_array = {};
							indexed_array['classs'] = $('#feestructureselectclasscombo').val();	
							indexed_array['term']   = $('#feestructureterm').val();	
							indexed_array['year']   = $('#feestructureyear').val();	
							indexed_array['account']= $('#feestructureaccount').val();	
							indexed_array['amount'] = $('#feestructureamount').val();	
							
								
						$.ajax({
							
							
							type : "POST",
							contentType : "application/json",
							url : Myurl,
							data : JSON.stringify(indexed_array),
							dataType : 'json',
							success: function(data){
								
									$("#myModaladdinvoiceitem").modal("hide");
									$("#example2").DataTable().destroy(true);																											
									dynamictable(data,'#div1feestructure','postclasstogetfeestructure','#example2','#example2');
									$("#modalprogress2").modal("hide");
									Myurl='postclasstogetfeestructure';
										var totalAmount = 0;
										for (var i = 0; i < $(tablebeingedited).DataTable().column(5).data().length; i++) {
											totalAmount += parseFloat($(tablebeingedited).DataTable().column(5).data()[i]);
										}
										$("#totalfeestructureamt").html('Total  '+numberWithCommas(totalAmount));								
								}		
						});	   
		   
		   
		   
	   }
	   
	   else if(Myurl=='postgetstudentstoinvoice'){ 
	   
							var indexed_array = {};
							indexed_array['classs'] = $('#feestructureselectclasscombo').val();	
	   
		   $.ajax({		
							type : "POST",
							contentType : "application/json",
							url : Myurl,
							data : JSON.stringify(indexed_array),
							dataType : 'json',
							success: function(data){
								
									$("#feestructuremaindiv").hide();
									$("#divselectstudentsforbilling").show();
									$("#example3").DataTable().destroy(true);																											
									dynamictable(data,'#divselectstudentsforbilling','postgetstudentstoinvoice','#example3','#example3');									
									
									$("#modalprogress2").modal("hide");
									Myurl='postclasstogetfeestructure';
									
															
								}		
						});
		   
	   }
	   
	   else if(Myurl=='poststudentstoinvoice'){ 
	      
						var indexed_array = {};
							indexed_array['studentstoinvoice'] = rows_selected;	
							indexed_array['classs'] = $('#feestructureselectclasscombo').val();	
							indexed_array['term']   = $('#feestructureterm').val();	
							indexed_array['year']   = $('#feestructureyear').val();	
							
						$.ajax({
							
							type : "POST",
							contentType : "application/json",
							url : Myurl,
							data : JSON.stringify(indexed_array),
							success: function(data){
								
									var object = JSON.parse(data)[0];

								
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
									
									
									$("#modalprogress2").modal("hide");
									$("#popupfeestructure").modal("hide");
									
									
									Myurl='postclasstogetfeestructure';
									
															
								}		
						});
		     
		   
		   
		   
	   }
	   
	   
	   else if(Myurl=='postcopyvoteheads'){ 
	      
						var indexed_array = {};
							indexed_array['classfrom']   = $('#classtocpyfromcombo').val();	
							indexed_array['classto']   = $('#classtocopytocombo').val();	
							indexed_array['term']   = $('#feestructureterm').val();	
							indexed_array['year']   = $('#feestructureyear').val();	
							
						$.ajax({
							
							type : "POST",
							contentType : "application/json",
							url : Myurl,
							data : JSON.stringify(indexed_array),
							success: function(data){
								
									var object = JSON.parse(data)[0];
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
									
									
									$("#modalprogress2").modal("hide");
									$("#copyvotesmodal").modal("hide");
									
									
									Myurl='postclasstogetfeestructure';
									
															
								}		
						});
		     
		   
		   
		   
	   }
	   
	   else if(Myurl=='postvotestoreplicate'){ 
	    
		
		var indexed_array = {};
							
							indexed_array['classs'] = $('#feestructureselectclasscombo').val();	
							indexed_array['term']   = $('#feestructureterm').val();	
							indexed_array['year']   = $('#feestructureyear').val();	
							indexed_array['votecombo']   = $('#replicatevotescombo').val();	
							indexed_array['replicateamt']   = $('#replicatevotesaddamt').val();	
							
						$.ajax({
							
							type : "POST",
							contentType : "application/json",
							url : Myurl,
							data : JSON.stringify(indexed_array),
							success: function(data){
								
									var object = JSON.parse(data)[0];
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
									
									
									$("#modalprogress2").modal("hide");
									$("#copyvotesmodal").modal("hide");
									
									
									Myurl='postclasstogetfeestructure';
									
															
								}		
						});
		     
		
	   
	   }
	   
	    else if(Myurl=='processandloadstudentreceipt'){ 
	    
		
		var indexed_array = {};
							
							indexed_array['studentid'] = $('#searchstudentsfeepayment').val().split('  ')[0];	
							indexed_array['amount']   = $('#feepaymentrctamt').val();	
							indexed_array['date']   = $('#feepaymentdate').val();	
							indexed_array['paymentmethod']   = $('#feepaymentpaymentmethod').val();	
							indexed_array['depodate']   = $('#feepaymentdepodate').val();	
							indexed_array['transnu']   = $('#feepaymenttransnu').val();	
							indexed_array['totalbal'] = document.getElementById("studentfeebal").innerHTML;				
			
						$.ajax({
							
							type : "POST",
							contentType : "application/json",
							url : Myurl,
							data : JSON.stringify(indexed_array),
							success: function(data){
								
									var object = JSON.parse(data)[0];
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
									
									
									$("#modalprogress2").modal("hide");
									$("#copyvotesmodal").modal("hide");
									
									
									Myurl='postclasstogetfeestructure';
									
															
								}		
						});
		     
		
	   
	   }
	   else if(Myurl=='loadremarks'){ 
	       
		   
		    var listofstudents;
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			indexed_array['category'] = $('#selectremarkcategorycombo').val();
			indexed_array['teacher'] = $('#selectremarkteachercombofrommainpage').val();		

		   
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : 'api/exam/loadremarks',
						data : JSON.stringify(indexed_array),
						success: function(data){
						$("#example1").DataTable().destroy(true);
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postremarksdataforreg','#example1','#example1','#popupcreatenewremark','#customerFormremark');
						
						$("#modalprogress2").modal("hide");
						$("#copyvotesmodal").modal("hide");
													 
					}		
				});	 
				
	   }
	   
	   else if(Myurl=='loadgradingsystem'){ 
	       
		   
		    var listofstudents;
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			indexed_array['category'] = $('#selectgradingsystemcategorycombo').val();		
			indexed_array['classs'] = $('#selectgradingclasscombo').val();		

		   
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : '/api/exam/loadgradingsystem',
						data : JSON.stringify(indexed_array),
						success: function(data){
						$("#example1").DataTable().destroy(true);
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postgradingsystemforreg','#example1','#example1','#popupcreatenewgradingsystem','#customerFormgradingsystem');
						
						$("#modalprogress2").modal("hide");
						$("#copyvotesmodal").modal("hide");
													 
					}		
				});	 
				
	   }
	   
	   
	   
	   else if(Myurl=='loadmoresettings'){ 
	       
		   
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			indexed_array['classs'] = $('#reportformdatesclass').val();		
			
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : '/api/exam/loadmoresettings',
						data : JSON.stringify(indexed_array),
						success: function(data){
						var object = JSON.parse(data)[0];
						
						
						$('#nexttermddates').val(object['nexttermddates'])
						$('#midtermdate').val(object['midtermdate'])
						$('#clinicdates').val(object['clinicdates'])
						$('#cloasingdates').val(object['cloasingdates'])
						
						$("#modalprogress2").modal("hide");
						$("#copyvotesmodal").modal("hide");
													 
					}		
				});	 
				
	   }
	   
	   
	   
	   else if(Myurl=='savereportformdates'){ 
	       
		   
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			
			indexed_array['classs'] = $('#reportformdatesclass').val();		
			indexed_array['nexttermddates'] = $('#nexttermddates').val();		
			indexed_array['midtermdate'] = $('#midtermdate').val();		
			indexed_array['clinicdates'] = $('#clinicdates').val();		
			indexed_array['cloasingdates'] = $('#cloasingdates').val();		

		   	
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : '/api/exam/savereportformdates',
						data : JSON.stringify(indexed_array),
						success: function(data){
						var object = JSON.parse(data)[0];
						
						$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
						
						$("#modalprogress2").modal("hide");
						$("#copyvotesmodal").modal("hide");
													 
					}		
				});	 
				
	   }
	   
	   
	   
	   else if(Myurl=='api/exam/copygrading'){ 
	       
		   
		    var listofstudents;
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			indexed_array['categorytocopyfrom'] = $('#selectgradingsystemcategorycombo').val();		
			indexed_array['categorytocopyto'] = $('#categorytocopygradingto').val();		
						
		   
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : 'api/exam/copygrading',
						data : JSON.stringify(indexed_array),
						success: function(data){
						
						var object = JSON.parse(data)[0];
									
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
						
						$("#modalprogress2").modal("hide");
						
													 
					}		
				});	 
				
	   }
	   
	   
	   
	   else if(Myurl=='api/exam/copyremarks'){ 
	       
		   
		    var listofstudents;
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			
			
			if ($('#selectteacherremarkdivteacher').is(':visible')) {
				indexed_array['categorytocopyto'] = $('#selectremarkteachercombo').val();	
				indexed_array['categoryto'] = 'Teachers';					
			}else{				
				indexed_array['categorytocopyto'] = $('#categorytocopyremarkto').val();		
				indexed_array['categoryto'] = 'category';														
			}
			
			
			if ($('#selectteacherremarkdivcombo').is(':visible')) {			
				indexed_array['categorytocopyfrom'] = $('#selectremarkteachercombofrommainpage').val();
				indexed_array['categoryfrom'] = 'Teachers';								
			}else{
				indexed_array['categorytocopyfrom'] = $('#selectremarkcategorycombo').val();	
				indexed_array['categoryfrom'] = 'category';														
			}				
		   
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : 'api/exam/copyremarks',
						data : JSON.stringify(indexed_array),
						success: function(data){
						
						var object = JSON.parse(data)[0];
									
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
						
						$("#modalprogress2").modal("hide");
						
													 
					}		
				});	 
				
	   }
	   
	   
	   
	   
	   
	   
	   
	   else if(Myurl=='addexam'){ 
	       
		   
		    var listofstudents;
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			indexed_array['Date'] = $('#examdate').val();		
			indexed_array['Category'] = $('#examcategory').val();		
			indexed_array['Class'] = $('#examclass').val();		
			indexed_array['Term'] = $('#examterm').val();
			
			if($("#includeallclassescheck").prop('checked') == true){
				indexed_array['includeallclass'] = 'true';
			}
			if($("#customexamnamecheck").prop('checked') == true){
				indexed_array['customexamname'] = 'true';
				indexed_array['examname'] = $('#customexamname').val();
			}
			
			
		   
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : 'api/exam/addexam',
						data : JSON.stringify(indexed_array),
						success: function(data){
						
						$("#modalprogress").modal("hide");
						
						var object = JSON.parse(data)[0];
									
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
						
						if(object['querystatus']==='Operation successfull'){
							$("#example1").DataTable().destroy(true);						
							listofexams = JSON.parse(data);
							dynamictable(listofexams,'#div1','postexamdataforreg','#example1','#example1','#popupcreatenewexam','#customerForm');
						}			

									
						$("#modalprogress2").modal("hide");
						$("#copyvotesmodal").modal("hide");
													 
					}		
				});	 
				
	   }
	   
	   
	    else if(Myurl=='updateexam'){ 
	       
		   
		    var listofstudents;
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			
			indexed_array['id'] = id;
			indexed_array['Date'] = $('#examdate').val();		
			indexed_array['Category'] = $('#examcategory').val();		
			indexed_array['Class'] = $('#examclass').val();		
			indexed_array['Term'] = $('#examterm').val();
			
			if($('#customexamname').val()!=''){
				indexed_array['exam name'] = $('#customexamname').val();
			}
			
			
		   
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : 'api/exam/updateexam',
						data : JSON.stringify(indexed_array),
						success: function(data){
						
						$("#modalprogress").modal("hide");
						
						var object = JSON.parse(data)[0];
									
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 2000);
									});
						
						if(object['querystatus']==='Operation successfull'){
							
							$("#example1").DataTable().destroy(true);						
							listofexams = JSON.parse(data);
							dynamictable(listofexams,'#div1','postexamdataforreg','#example1','#example1','#popupcreatenewexam','#customerForm');
							
						}			

									
						$("#modalprogress2").modal("hide");
						$("#copyvotesmodal").modal("hide");
													 
					}		
				});	 
				
	   }
	   
	    else if(Myurl=='loadexamsby'){ 
	       
		   
		    var listofstudents;
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			
			indexed_array['Date'] = $('#filterbyyear').val();		
			indexed_array['Class'] = $('#filterbyclass').val();		
			indexed_array['Term'] = $('#filterbyterm').val();
			
		   
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : 'api/exam/loadexamsby',
						data : JSON.stringify(indexed_array),
						success: function(data){
						
							$("#example1").DataTable().destroy(true);						
							listofexams = JSON.parse(data);
							dynamictable(listofexams,'#div1','postexamdataforreg','#example1','#example1','#popupcreatenewexam','#customerForm');
								
						$("#modalprogress").modal("hide");
						$("#modalprogress2").modal("hide");
						$("#copyvotesmodal").modal("hide");
													 
					}		
				});	 
				
	   }
	   
	   
	   else if(Myurl=='loadsubjectsby'){ 
	       
		   
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			
			indexed_array['class'] = $('#filtersubjectsbyclass').val();		
			
		  
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : '/api/exam/loadsubjectsbyclass',
						data : JSON.stringify(indexed_array),
						success: function(data){
						
							$("#example1").DataTable().destroy(true);						
							listofexams = JSON.parse(data);
							dynamictable(listofexams,'#div1','postsubjectsdataforreg','#example1','#example1','#popupcreatenewsubject','#customerFormsubject');
							 
							 $("#modalprogress2").modal("hide");
						
					}		
				});	 
				
	   }
	   
	   
	   
	   else if(Myurl=='loadsubjectselection'){ 
	       
		   
			var unindexed_array = $( this ).serializeArray();
			var indexed_array = {};
			
			indexed_array['class'] = $('#subjectselectionclass').val();		
			
		  
		   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : '/api/exam/getsubselection',
						data : JSON.stringify(indexed_array),
						success: function(data){
						
							$("#example1").DataTable().destroy(true);						
							listofexams = JSON.parse(data);
							dynamictable(listofexams,'#div1','postgetsubjectselection','#example1','#example1','#popupcreatenewsubject','#customerFormsubject');
							 
							 $("#modalprogress2").modal("hide");
						
					}		
				});	 
				
	   }
	   
	   
	   
	   
	   else if(Myurl=='students'){ 
	   
		uniqueid='adm no';
		uploadimagesoption="students";
		
		
		$('#regsearchbut').show();
		$('#regmenuname').hide(); 
	
		
		  $.post("students").done(function (fragment) {				   
			
			$.ajax({
						url : '/api/reg/getallstudents',
						contentType : "application/json",
								success: function(data){             
								console.log(data);
								listofstudents = JSON.parse(data);
								dynamictable(listofstudents,'#div1','poststudentdataforreg','#example1','#example1','#popup1students','#customerFormstudents');
								$("#modalprogress2").modal("hide");
							}		
						});	  
					   
					   $("#output").replaceWith(fragment);
					   $("#regloadedlist").html("STUDENTS");
						
						
		 });
	   
	   }
	   
	   else if(Myurl=='teachers'){ 	   
	   
			uniqueid='trnu';
			  uploadimagesoption="teachers";
			  $('#regsearchbut').show();
				$('#regmenuname').hide(); 
			
					$.post("students").done(function (fragment) {
					
				   
						   $.ajax({
							url : '/api/reg/getallteachers',
							contentType : "application/json",
									success: function(data){             
									listofstudents = JSON.parse(data);
									dynamictable(listofstudents,'#div1','postteacherdataforreg','#example1','#example1','#popup1teachers','#customerFormteachers');
									$("#modalprogress2").modal("hide");
								}		
							});	  
						   
						   $("#output").replaceWith(fragment);
						   $("#regloadedlist").html("TEACHERS");
						
						
		  
			 });
	
	
	   }
	   
	   else if(Myurl=='staff'){ 
	   
			uniqueid='idnu';
			uploadimagesoption="staff";
			$('#regsearchbut').show();
				$('#regmenuname').hide(); 
			
			
			  $.post("students").done(function (fragment) {
					
				   
						   $.ajax({
							url : '/api/reg/getallstaff',
							contentType : "application/json",
									success: function(data){             
									listofstudents = JSON.parse(data);
									dynamictable(listofstudents,'#div1','poststaffdataforreg','#example1','#example1','#popup1supportstaff','#customerFormstaff');
									$("#modalprogress2").modal("hide");
								}		
							});	  
						   
						   $("#output").replaceWith(fragment);
							$("#regloadedlist").html("SUPPORT STAFF");
			
							
		  
			 });
	 
	
	   }
	   
	   
	   else if(Myurl=='board'){ 
	   
	   
			uniqueid='idnu';
			uploadimagesoption="board";
			$('#regsearchbut').show();
				$('#regmenuname').hide(); 
			
			  $.post("students").done(function (fragment) {
					
							
						
						   $.ajax({
							url : '/api/reg/getallboard',
							contentType : "application/json",
									success: function(data){             
									listofstudents = JSON.parse(data);
									dynamictable(listofstudents,'#div1','postboarddataforreg','#example1','#example1','#popup1board','#customerFormboard');
									$("#modalprogress2").modal("hide");
								}		
							});	  
						   
						   $("#output").replaceWith(fragment);
						   $("#regloadedlist").html("BOARD MEMBERS");
			
						   
		  
			 });
	 
	   }
	   
	   
	   
	   
	   else if(Myurl=='loadnewsms'){ 
	   
	   
			$.post("newmessage").done(function (fragment) {	


			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{

	   
			
			$.ajax({
				url : '/api/sms/getaccoutbal',
				contentType : "application/json",
				success: function(data){		
					 $('#accountbal').html('BAL | '+JSON.parse(data)[0].smsaccbal);
				}		
			});	
			
			$.ajax({
							url : '/api/sms/loadlisttosendsms',
							contentType : "application/json",
									success: function(data){  
									dynamictable(JSON.parse(data),'#div1','postsmslisttogetphones','#example1','#example1');
									$("#modalprogress2").modal("hide");
								}		
				});	     
					
			$("#output").replaceWith(fragment);
			
			}
			
			
       });
	 
	   }
	   
	   
	   
	   
	   
	 else if(Myurl=='loadexamdetails'){ 
	     
	   
	   
			document.getElementById("cityanalysis").style.display = "block";
          

			var indexed_array = {};
			indexed_array['examname'] = examname;
			

				$.ajax({
					
					type : "POST",
					contentType : "application/json",
					url : "/api/exam/getperformanceperstream",
					data : JSON.stringify(indexed_array),
					dataType : 'json',
					success: function(data){	

								var object = data[0];									
								
								if(object['querystatus']=="an error occured"){
								
									$("#modalprogress2").modal("hide");
									
								    $(function () {
											$("#errormessage").html(object['querystatus']);
												$('#myModalErrorstop').modal('show');
												setTimeout(function () {
												$('#myModalErrorstop').modal('hide');
											}, 2000);
									});						
										
								
								
								}
								
								else{
									dynamictable(data,'#div1scoreperstream','posttogetperformanceperstream','#example3','#example3','#popupcreatenewgradingsystem','#customerFormgradingsystem');
								}
					
					
					$.ajax({
					
							type : "POST",
							contentType : "application/json",
							url : "/api/exam/getallenteredmarksasadmin",
							data : JSON.stringify(indexed_array),
							dataType : 'json',
							success : function(result) {												
								
								var object = result[0];									
								
								if(object['querystatus']=="an error occured"){
								
									$("#modalprogress2").modal("hide");
									
								    $(function () {
											$("#errormessage").html(object['querystatus']);
												$('#myModalErrorstop').modal('show');
												setTimeout(function () {
												$('#myModalErrorstop').modal('hide');
											}, 2000);
									});						
										
								
								
								}
								
								else{
									dynamictable(result,'#div1marksasadmin','posttogetmarksasadmin','#example2','#example2','#popupcreatenewgradingsystem','#customerFormgradingsystem');
								}
									
									$.ajax({
					
										type : "POST",
										contentType : "application/json",
										url : "/api/exam/getexamstreams",
										data : JSON.stringify(indexed_array),
										dataType : 'json',
										success : function(streams) {										
											
											
											var object = streams[0];					
											if(object['querystatus']=="an error occured"){
											
												$("#modalprogress2").modal("hide");
												
												$(function () {
														$("#errormessage").html(object['querystatus']);
															$('#myModalErrorstop').modal('show');
															setTimeout(function () {
															$('#myModalErrorstop').modal('hide');
														}, 2000);
												});						
													
											
											
											}
											
											else{
											
													var data=streams;											
													$('#selectexamstreams').find('option').remove().end();																				
													var $dropdown = $("#selectexamstreams");
														$dropdown.append($("<option />").val('All').text('All'));																						
													for (var i = 0; i < data.length; i++) {
														$dropdown.append($("<option />").val(data[i].streanid).text(data[i].stream));
													}	

											}

													$.ajax({
									
														type : "POST",
														contentType : "application/json",
														url : "/api/exam/getclassmeanandgrade",
														data : JSON.stringify(indexed_array),
														dataType : 'json',
														success : function(streams) {																								
															
															
															var object = streams[0];					
															if(object['querystatus']=="an error occured"){
															
																$("#modalprogress2").modal("hide");
																
																$(function () {
																		$("#errormessage").html(object['querystatus']);
																			$('#myModalErrorstop').modal('show');
																			setTimeout(function () {
																			$('#myModalErrorstop').modal('hide');
																		}, 2000);
																});						
																	
															
															
															}
															
															else{
															
																	var data=streams;
																	
																	
																	const classstreams = [];
																	const streamvalues = [];
																	
																	console.log();
																	
																	for (var i = 0; i < data.length; i++) {
																		$("#exammeanmarks").html(data[i]['MN MKS']);
																		$("#exammeangrade").html(data[i].Grade);
																		$("#exammeanpoints").html(data[i]['MN PTS']);
																		$("#examnumberofstudents").html(data[i].ENTRY+" Students");	
																		classstreams[i]= data[i].stream.replace("Aggregate", "Tot");
																		streamvalues[i]= data[i]['MN PTS'];																	
																	}
																	   

																		dataCompletedTasksChart  = {
																					labels: classstreams,
																					series: [
																					  streamvalues
																					]
																				  };

																		var charteaxmscoreperstream = new Chartist.Line('#charteaxmscoreperstream', dataCompletedTasksChart, null);
																			md.startAnimationForLineChart(charteaxmscoreperstream);
																	
															}
															 
															 
															 
															 $.ajax({
									
																type : "POST",
																contentType : "application/json",
																url : "/api/exam/getscorepersubject",
																data : JSON.stringify(indexed_array),
																dataType : 'json',
																success : function(streams) {																								
																	
																	var object = streams[0];				
																	if(object['querystatus']=="an error occured"){
																	
																		$("#modalprogress2").modal("hide");
																		
																		$(function () {
																				$("#errormessage").html(object['querystatus']);
																					$('#myModalErrorstop').modal('show');
																					setTimeout(function () {
																					$('#myModalErrorstop').modal('hide');
																				}, 2000);
																		});						
																			
																	
																	
																	}
																	
																	else{
																	
																		var data=streams;
																		const classstreams = [];
																		const streamvalues = [];
																		
																		for (var i = 0; i < data.length; i++) {
																			classstreams[i]= data[i].sub;
																			streamvalues[i]= data[i].avg;																	
																		}
																																	
																		
																		 dataCompletedTasksChart  = {
																				labels: classstreams,
																				series: [
																				  streamvalues
																				]
																			  };	  
														  
																			var scorepersubbarchart = Chartist.Bar('#scorepersubbarchart', dataCompletedTasksChart, null, null);
																				md.startAnimationForBarChart(scorepersubbarchart);
																		
																		  $("#examdetails").html(examname+" - ANALYSIS");
																		  console.log("yoh mcee");
																		  $("#modalprogress2").modal("hide");
																	}
																	
																},
																error:function (xhr, ajaxOptions, thrownError) {
																	console.log(thrownError+"  mceee  "+xhr);
																}		
														});
															 
															
															
														},
														error:function (xhr, ajaxOptions, thrownError) {
															console.log(thrownError+"  mceee  "+xhr);
														}		
													});
															
												
												
										},
										error:function (xhr, ajaxOptions, thrownError) {
											console.log(thrownError+"  mceee  "+xhr);
										}		
									});
									
									
									
							},
							error:function (xhr, ajaxOptions, thrownError) {
								console.log(thrownError+"  mceee  "+xhr);
							}		
						});
						
					},
					
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError+"  mceee  "+xhr);
					}		
				});


}




else if(Myurl=='listofexams'){ 
	 

		$('#regsearchbut').show();
		$('#regmenuname').hide(); 
	
		
		
      $.post("listofexams").done(function (fragment) {
		  
		  
		  if(fragment.includes("change-me")){
				
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
		  
		
			   $.ajax({
					url : '/api/exam/loadlistofexams',
					contentType : "application/json",
							success: function(data){             
							listofexams = JSON.parse(data);
							dynamictable(listofexams,'#div1','postexamdataforreg','#example1','#example1','#popupcreatenewexam','#customerFormexam');
							$("#modalprogress2").modal("hide");
						}		
					});	  
				   
				   $("#output").replaceWith(fragment);
	
			}
				   
		});
	}

	
	else if(Myurl=='allowedexams'){
		
		$('#regsearchbut').show();
		$('#regmenuname').hide(); 
	  
	  
		   
			$.post("allowedexams").done(function (fragment) {
			
				   $.ajax({
					url : '/api/exam/getallowedexams',
					contentType : "application/json",
							success: function(data){             
							listofexams = JSON.parse(data);
							dynamictable(listofexams,'#div1','postexammarksforreg',tablemain,'#example1','#popupentermarks','#customerForm');
							$("#modalprogress2").modal("hide");
						}		
					});	  
				   
				 $("#output").replaceWith(fragment);
				
		});
	}
	
	
	
	else if(Myurl=='subjectselection'){

		$('#regsearchbut').hide();
		$('#regmenuname').hide(); 
	
		    		   
				  
      $.post("subjectselection").done(function (fragment) {
		  
		  
		  if(fragment.includes("change-me")){
					
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
		  
		  
			  
				   $("#output").replaceWith(fragment);
				   
				   var indexed_array = {};
					indexed_array['class'] = $('#subjectselectionclass').val();		

				   
				    $.ajax({
							type : "POST",
							contentType : "application/json",
							url : '/api/exam/getsubselection',
							data : JSON.stringify(indexed_array),
							success: function(data){             
							listofstudents = JSON.parse(data);
							dynamictable(listofstudents,'#div1','postgetsubjectselection','#example1','#example1','#popupcreatenewremark','#customerFormremark');
							$("#modalprogress2").modal("hide");		 
						}		
					});
					
					
			}
	
					
     });
		

	}
	
	
	
	
	
	else if(Myurl=='grades'){

		$.post("grades").done(function (fragment) {
		  
		  
		  	 if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
			
					
				  $.ajax({
						url : '/api/exam/loadgrades',
						contentType : "application/json",
						success: function(data){             
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postgradesdataforreg','#example1','#example1','#popupcreatenewgrade','#customerFormnewgrade');
						$("#modalprogress2").modal("hide");
					}		
				});	   
				   
				   $("#output").replaceWith(fragment);
				   
				   
			}
				  
     }); 
	

	}
	
	
	
	else if(Myurl=='subjectgroups'){

		$('#regsearchbut').hide();
		$('#regmenuname').hide(); 
	
				
		$.post("subjectgroups").done(function (fragment) {
			
			
			
			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
			
					
				   $.ajax({
						url : '/api/exam/loadsubjectgroups',
						contentType : "application/json",
						success: function(data){             
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postsubjectgroupsdataforreg','#example1','#example1','#popupcreatenewsubjectgroup','#customerFormsubjectgroup');
						$("#modalprogress2").modal("hide");
					}		
				});	   
				   
				   $("#output").replaceWith(fragment);
				   
			}
				  
		}); 

	}
	
	
	else if(Myurl=='subjects'){


	$('#regsearchbut').show();
	$('#regmenuname').hide(); 
	
				
      $.post("subjects").done(function (fragment) {
		  
			
			
			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
				
		  
			
					
				   $.ajax({
						url : '/api/exam/loadsubjects',
						contentType : "application/json",
						success: function(data){             
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postsubjectsdataforreg','#example1','#example1','#popupcreatenewsubject','#customerFormsubject');
						$("#modalprogress2").modal("hide");
					}		
				});	 
				   
				   $("#output").replaceWith(fragment);
				   
				   
			}
				   
				  
  
     }); 
		
	

	}
	
	
	
	else if(Myurl=='examcategories'){ 
	
	$('#regsearchbut').hide();
	$('#regmenuname').hide(); 
	
      $.post("examcategories").done(function (fragment) {
		  
		  
		  if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
		  
			
					
				   $.ajax({
						url : '/api/exam/loadexamcategories',
						contentType : "application/json",
						success: function(data){             
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postexamcategoriesdataforreg','#example1','#example1','#popupcreatenewexamcategory','#customerFormexamcategory');
						$("#modalprogress2").modal("hide");
					}		
				});	  
				   
				   $("#output").replaceWith(fragment);
				   
			}
				   
  
     }); 

	}
	
	
	
	
	else if(Myurl=='remarks'){ 
	
	$('#regsearchbut').hide();
	$('#regmenuname').hide(); 
	
	$('#selectteacherremarkdivteacher').hide();
	$('#selectteacherremarkdivcategory').show();	
	
		
		
      $.post("remarks").done(function (fragment) {
			
			
			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
			
				
		$("#output").replaceWith(fragment);
				
				var uniqueid='Category';
				var listofstudents;
				var indexed_array = {};
				indexed_array['category'] = $('#selectremarkcategorycombo').val();		
				indexed_array['teacher'] = $('#selectremarkteachercombofrommainpage').val();		


				   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : '/api/exam/loadremarks',
						data : JSON.stringify(indexed_array),
						success: function(data){             
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postremarksdataforreg','#example1','#example1','#popupcreatenewremark','#customerFormremark');
						$("#modalprogress2").modal("hide");				 
					}		
				});
				   
			}  
				  
     }); 
	
	}
	
	
	
	else if(Myurl=='gradingsystem'){ 
	
		$('#regsearchbut').show();
		$('#regmenuname').hide(); 
	
					
      $.post("gradingsystem").done(function (fragment) {
			
			
			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
			
			
					
					$("#output").replaceWith(fragment);
				   
				   var indexed_array = {};
					indexed_array['category'] = $('#selectgradingsystemcategorycombo').val();		
					indexed_array['classs'] = $('#selectgradingclasscombo').val();		

				
				   $.ajax({
						type : "POST",
						contentType : "application/json",
						url : '/api/exam/loadgradingsystem',
						data : JSON.stringify(indexed_array),
						success: function(data){             
						listofstudents = JSON.parse(data);
						dynamictable(listofstudents,'#div1','postgradingsystemforreg','#example1','#example1','#popupcreatenewgradingsystem','#customerFormgradingsystem');
						$("#modalprogress2").modal("hide");
					}		
				});	    
				   
				  
			}				  
				  
     });
	
	
	}
	
	
	
	else if(Myurl=='selectexammarksbystream'){ 
	
		var indexed_array = {};
		indexed_array['examname'] = $("#examdetails").text().split(" - ")[0];
		indexed_array['stream'] = $("#selectexamstreams").val();
		
		
						$.ajax({
					
							type : "POST",
							contentType : "application/json",
							url : "/api/exam/getallenteredmarksasadminusingstream",
							data : JSON.stringify(indexed_array),
							success : function(result) {
								
								$("#example2").DataTable().destroy(true);	
								dynamictable(JSON.parse(result),'#div1marksasadmin','posttogetmarksasadmin','#example2','#example2','#popupcreatenewgradingsystem','#customerFormgradingsystem');
								$("#modalprogress2").modal("hide");
										
							},
							error:function (xhr, ajaxOptions, thrownError) {
								console.log(thrownError+"  mceee  "+xhr);
							}		
						});
					
     
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//   Biometrics module   
	
	
	else if(Myurl=='biometricsmain'){ 
	 

		
		  $.post("biometricsmain").done(function (fragment) {
			  
			  
			  if(fragment.includes("change-me")){
					
					$(function () {
									$("#change-me").html("You dont have permission to access this module");
									$('#myModalError').modal('show');
									setTimeout(function () {
										$('#myModalError').modal('hide');
									}, 2000);
					});
					
					
				}else{
			  
			
				      $.ajax({
						url : '/api/exam/biometricsmain',
						contentType : "application/json",
								success: function(data){             
								loadbiostudents = JSON.parse(data);
								dynamictable(loadbiostudents,'#div1','postexamdataforreg','#example1','#example1','#popupcreatenewexam','#customerFormexam');
								$("#modalprogress2").modal("hide");
							}		
						});
						
					   
					   $("#output").replaceWith(fragment);
		
				}
				
					   
			});
		
		
	}
	
	
	else if(Myurl=='loadbiostudents'){ 
	 

		  $.post("loadbiostudents").done(function (fragment) {
			  
			  
			  if(fragment.includes("change-me")){
					
					$(function () {
									$("#change-me").html("You dont have permission to access this module");
									$('#myModalError').modal('show');
									setTimeout(function () {
										$('#myModalError').modal('hide');
									}, 2000);
					});
					
					
				}else{
			  
			
				      $.ajax({
						url : '/api/exam/loadbiostudents',
						contentType : "application/json",
								success: function(data){             
								loadbiostudents = JSON.parse(data);
								dynamictable(loadbiostudents,'#div1','postexamdataforreg','#example1','#example1','#popupcreatenewexam','#customerFormexam');
								$("#modalprogress2").modal("hide");
							}		
						});
						
					   
					   $("#output").replaceWith(fragment);
		
				}
				
					   
			});
		
		
	}
	
	   
});





function opendefaultmodal() {
	
	
	 

	
	actionsaveorupdate='Save';
					
	
	if(Myurl=='poststudentdataforreg'){
		
		myform="#customerFormstudents";
	    
		  $(myform)[0].reset();			
		  $(modaltoopen).modal({
				backdrop: 'static',
				keyboard: false
			});
			
		openCity(event,'Londonstudents');
		
					
			
   }else if(Myurl=='postteacherdataforreg'){
	   
	   myform="#customerFormteachers";
	   
	   var table = $('#example1').DataTable();
	   table.$("input[type=checkbox]").prop("checked", false);
	   $(myform)[0].reset();
	   
		
		var lastid=0;		
		
		if (dataarray[(dataarray.length-1)].trnu == null){
			lastid=1;		 
		}else{
			lastid=+dataarray[(dataarray.length-1)].trnu+1;		 	
		}
		 $('#trnu').val(lastid);
		 
		 openCity(event,'Londonteachers');
		 
		 
		 				
			$.ajax({
						url : '/api/reg/getsubtaught',
						contentType : "application/json",
								success: function(data){									
								var table = $('#example1').DataTable();
								table.$("input[type=checkbox]").prop("checked", false);
								listofteachers = JSON.parse(data);
								$("#example2").DataTable().destroy(true);												
													
								$(modaltoopen).modal({
									backdrop: 'static',
									keyboard: false
								});	
								
								dynamictable(listofteachers,'#classesdiv','postteacherdataforreg','#example2','#example1','#popup1teachers','#customerFormteachers');
								
								
							}		
			});
								
	  	
   }else if(Myurl=='poststaffdataforreg'){
	
	  myform="#customerFormstaff";
	   
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
		
		
		openCity(event,'Londonstaff');
	
		
   }else if(Myurl=='postboarddataforreg'){
	
		 myform="#customerFormboard";
		   
		  $(myform)[0].reset();			
		  $(modaltoopen).modal({
				backdrop: 'static',
				keyboard: false
			});

		  openCity(event,'Londonboard');
			
		
   }
   
   
   
   
   
   
   
   
   
   
   
   
   
   
 //EXAMS MODULE  
   
   else if( Myurl=='postexamdataforreg'){	
	
	  myform="#customerFormexam";
		 
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
   }
   
   
   else if( Myurl=='postgradesdataforreg'){	
	
	  myform="#customerFormnewgrade";
		 
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
		
   }
   
   else if( Myurl=='postsubjectgroupsdataforreg'){	
	
	  myform="#customerFormsubjectgroup";
		 
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
		
   }

  else if( Myurl=='postexamcategoriesdataforreg'){	
	
	  myform="#customerFormexamcategory";
		 
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
		
   }
   
   
   
   else if( Myurl=='postremarksdataforreg'){	
	
	  myform="#customerFormremark";
		 
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
		
   }
   
   
   else if( Myurl=='postgradingsystemforreg'){	
	
			$('#myInputcategory').val($('#selectgradingsystemcategorycombo').val());
			$('#myInputclass').val($('#selectgradingclasscombo').val());
	
	  myform="#customerFormgradingsystem";
		 
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
		
   }


else if( Myurl=='postsubjectsdataforreg'){	
	
	  myform="#customerFormsubject";
		 
	  $(myform)[0].reset();			
	  $(modaltoopen).modal({
			backdrop: 'static',
			keyboard: false
		});	
		
   }





	$("#formsubmitbuttstudents").html("Save");	
	$("#formsubmitbuttteachers").html("Save");	
	$("#formsubmitbuttstaff").html("Save");	
	$("#formsubmitbuttboard").html("Save");	
 
 
 
 
	$("#saveexambutt").html("Save");	
	$("#formsubmitbuttgrade").html("Save");	
	$("#formsubmitbuttsubjectgroup").html("Save");	
	$("#formsubmitbuttexamcategory").html("Save");	
	$("#formsubmitbuttremark").html("Save");	
	$("#formsubmitbuttgradingsystem").html("Save");	
	
	$("#formsubmitbuttsubject").html("Save");	
	

	
  
}



function populate(frm,data,Myurl) { 

	
 
	$(frm)[0].reset();
	  
		$.each(data, function(key, value) {  
        var ctrl = $('[name="'+key+'"]', frm);
		switch(ctrl.prop("type")) { 
            case "radio": case "checkbox":   
                ctrl.each(function() {
                    if($(this).attr('value') == value) $(this).attr("checked",value);
                });   
                break;  
            case "select-one":  
				ctrl.val(value);
				ctrl.click();				
                break;  
            default:				
				ctrl.val(value); 
				
				if(Myurl=='postexamdataforreg' && key=='customexamname' && value!=''){								
					$('#customexamnamediv' ).show();
					$('#customexamnamecheck').prop('checked', true);
					$('#customexamnamecheck').attr("disabled", true);					
					
					$('#includeallclassescheck').prop('checked', false);					
					$('#includeallclassescheck').attr("disabled", true);
					
				}if(Myurl=='postexamdataforreg' && value==''){								
					
					$('#customexamnamecheck').prop('checked', false);
					$('#customexamnamecheck').attr("disabled", true);							
					$('#includeallclassescheck').attr("disabled", true);					
				}
				
        }  
		
		if(key=='simage'){
			$("#simage"+uploadimagesoption).attr("src",imgname=value);			
		}
		
    });

	
		   
		if(Myurl=='postteacherdataforreg'){
		   	
		  
		   var table = $('#example1').DataTable();
		   table.$("input[type=checkbox]").prop("checked", false);
		   
		   $.ajax({
						url : '/api/reg/getsubtaught',
						contentType : "application/json",
								success: function(data){									
								var table = $('#example1').DataTable();
								table.$("input[type=checkbox]").prop("checked", false);
								listofteachers = JSON.parse(data);
								$("#example2").DataTable().destroy(true);												
								
								dynamictable(listofteachers,'#classesdiv','postteacherdataforreg','#example2','#example1','#popup1teachers','#customerFormteachers');
																
								$(modaltoopen).modal({
									backdrop: 'static',
									keyboard: false
								});	
								
								
								
							}		
			});
   
		
		}
		
		
  		
}




function dynamicdeleteitem(id,tablebeingedited,Myurl) {	        

		var unindexed_array = $( this ).serializeArray();
		var indexed_array = {};
			indexed_array['id'] = id;	


		if(Myurl=='/api/exam/deleteremark'){
		indexed_array['category'] = $('#selectremarkcategorycombo').val();				
		}
										
		
		$.ajax({
			
			type : "POST",
			contentType : "application/json",
			url : Myurl,
			data : JSON.stringify(indexed_array),
			dataType : 'json',
			success : function(result) {						
				
				var object = result[0];
					
				if(object['querystatus']=="Operation successfull"){

						$(function () {
							$(modaltoopen).modal('hide');
							$("#messageid").html(object['querystatus']);
							$('#myModal').modal('show');
							setTimeout(function () {
								$('#myModal').modal('hide');
							}, 1000);
						});

						
						 const table = $(tablebeingedited).DataTable();							
						 var row = table.row(rowindex);
						 row.remove().draw();
						
						if(Myurl=='postclasstogetfeestructure'){
											var totalAmount = 0;
											for (var i = 0; i < $(tablebeingedited).DataTable().column(5).data().length; i++) {
												totalAmount += parseFloat($(tablebeingedited).DataTable().column(5).data()[i]);
											}
											$("#totalfeestructureamt").html('Total  '+numberWithCommas(totalAmount));
						}
					
							
				}else{
					
					$(function () {						    
						$("#change-me").html(object['querystatus']);
						$('#myModalError').modal('show');						
					});
					
				}
				
				
			},
			error : function(e) {
				console.log("ERROR: ", e);
			}
		});
	

}





function dynamicformsubmit(myform,actionurl,listofstudents,action,Modal) {	

	actionsaveorupdate='Save Changes';
					

	var indexed_array = {};
		
		if(Myurl=='postremarksdataforreg'){
			indexed_array['category'] = $('#selectremarkcategorycombo').val();				
		}
		else if(Myurl=='postgradingsystemforreg'){
			indexed_array['category'] = $('#selectgradingsystemcategorycombo').val();		
			indexed_array['classs'] = $('#selectgradingclasscombo').val();		
		}
		
		indexed_array['id'] = listofstudents;
		
		
					
		$.ajax({
			
			type : "POST",
			contentType : "application/json",
			url : actionurl,
			data : JSON.stringify(indexed_array),
			dataType : 'json',
			success : function(result) {
	
	
				if(Myurl=='postexammarksforreg'){
					
					$("#example2").DataTable().destroy(true);	
					
					dynamictable(result,'#div1entermarks','postexameditmarksforreg','#example2','#example2');
									
					$(Modal).modal({
						backdrop : 'static',
						keyboard : false
					});
					
					
					
				}
	
				else{
					
					populate(myform,result,Myurl);	
					
					 
					 $("#formsubmitbuttstudents").html("Save Changes");	
					 $("#formsubmitbuttteachers").html("Save Changes");	
					 $("#formsubmitbuttstaff").html("Save Changes");	
					 $("#formsubmitbuttboard").html("Save Changes");	


					$("#saveexambutt").html("Save Changes");
					$("#formsubmitbuttgrade").html("Save Changes");
					$("#formsubmitbuttsubjectgroup").html("Save Changes");	
					$("#formsubmitbuttexamcategory").html("Save Changes");	
					$("#formsubmitbuttremark").html("Save Changes");	
					$("#formsubmitbuttgradingsystem").html("Save Changes");	
					$("#formsubmitbuttsubject").html("Save Changes");	
	

					


					$(Modal).modal({
						backdrop : 'static',
						keyboard : false
					});	
					

				}	


					openCity(event,action);
	
													
			},
			error : function(e) {
				console.log("ERROR: ", e);
			}
		});
	
	

}




function dynamictablereport(data,Mydiv,tablee) {	
	  
	$(tablee).DataTable().destroy(true);												
	tablereport=tablee;

	  
	if (Object.keys(data[0]).length === 0){
	   
	}else{
	
	
			var collumns = Object.keys(data[0]);
				
					var html = '<table  id='+tablee.substring(1)+' class="table table-striped projects"  width="100%">';
							
							html +='<thead  class=" text-primary">';
								
								html +='<tr>';
												
										for (i=0; i<collumns.length; i++) {														
										  html +='<td>' + collumns[i]+ '</td>';								
										}
								
								html +='</tr>';
								
							html +='</thead>';					
						
							html +='<tbody>';
										
										for (var i = 0; i < data.length; i++) {									
											
											html +='<tr>';										
															
												var object = data[i];
													  for (var property in object) {
														
														if(property=="Name"){									
															html +='<td style="min-width:170px">' +object[property]+ '</td>';
														}																			
														else{ 	
															html +='<td>' +object[property]+ '</td>';										
														}
													  
													  }
											
											html +='</tr>';
											
										}
							
									
							
							html += '</tbody>';
						
					html += '</table>';
						
						$(html).appendTo(Mydiv);
						  
								$(tablee).DataTable({
							  "responsive": true, "lengthChange": false,"bDestroy": true,"bFilter": false, "autoWidth": false,pageLength: 6,
							  buttons: [
											   {extend: 'copy', footer: false},
											   {extend: 'print',footer: false},
											   {text: 'Pdf',
											   action: function ( e, dt, node, config ) {
													generate(data);	
													}
												},
											   {extend: 'csv',  footer: false},
											   {extend: 'excel',footer: false}         
										]
							}).buttons().container().appendTo(tablee+'_wrapper .col-md-6:eq(0)');
							
						
						
	}			
				
			
}



	  


function generate(data) {
	
	
		 var head = [Object.keys(data[0])];		 
		 var body=data.map(el=>Object.values(el));
		
  var doc = new jspdf.jsPDF();
  var totalPagesExp = '{total_pages_count_string}'

 
	
  doc.autoTable({
    head:head,
    body: body
	,theme: 'grid',
    didDrawPage: function (data) {
      // Header
	  
	  
	  
	  var Schoolname="";
		var Motto="";
		var Pobox="";
		var City="";
		var Phone="";
		var Email="";
		var logo="";
		
		$.ajax({					
					url : "/api/log/getschoolinfo",
					contentType : "application/json",
					async: false,
					success: function(data){											
						var object = JSON.parse(data)[0];
						Schoolname=object['schoolname'];
						Pobox=object['address'];						
						City=object['city'];						
						Phone=object['phone'];						
						Email=object['email'];
						logo=object['logo'];				
					},
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError+"  mceee  "+xhr);
					}					
			});
			
			
      
		if (img) {
            doc.addImage(img, 'jpeg', 40, 5, 20, 20);
		}
		
	  
						
		doc.setFontSize(14)
		doc.setTextColor(30);
		doc.text(100, 7, Schoolname, 'center');

		doc.setFontSize(11)
		doc.text(100, 12, Pobox, 'center');
		doc.text(100, 17, City, 'center');

		doc.setFontSize(9)
		doc.text(100, 21, Email, 'center');
		doc.text(100, 25, Phone, 'center');

		doc.setFontSize(15)
		doc.setTextColor(40);
		
		
		if(reportTitle=="Transfered students"){
			doc.text(100, 31,document.getElementById("reporttitle").innerHTML+' from '+$('#reservation').val(), 'center');
		}else if(reportTitle=="List of allumni"){
			doc.text(100, 31,document.getElementById("reporttitle").innerHTML+' for the year '+$('#selectyear').val().split("-")[0], 'center');		
		}else if(reportTitle=="List of teachers" || reportTitle=="Support staff"){
			doc.text(100, 31,document.getElementById("reporttitle").innerHTML, 'center');
		}else if(reportTitle=="Hostel"){
			doc.text(100, 31,$("#hostel option:selected").html()+' '+document.getElementById("reporttitle").innerHTML, 'center');
		}else if(reporttitle=="group list"){ 
			doc.text(100, 31,$("#religionreportselect option:selected").html()+' '+document.getElementById("reporttitle").innerHTML, 'center');		
		}else{
			doc.text(100, 31, $("#selectval option:selected").html()+' '+document.getElementById("reporttitle").innerHTML, 'center');		
		}
		
      // Footer
      var str = 'Page ' + doc.internal.getNumberOfPages()
      // Total page number plugin only available in jspdf v1.0+
      if (typeof doc.putTotalPages === 'function') {
        str = str + ' of ' + totalPagesExp
      }
      doc.setFontSize(10)

      // jsPDF 1.4+ uses getWidth, <1.4 uses .width
      var pageSize = doc.internal.pageSize
      var pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight()
	  doc.text(str,248-5,297-4,null,null,"right");

    },
    margin: { top: 34, left: 5,right: 5,bottom: 6},
	columnStyles: { 0:{cellWidth:10},1:{cellWidth:15},2:{cellWidth:48}},
    styles: { fontSize: 8 },
		
  });

  // Total page number plugin only available in jspdf v1.0+
  if (typeof doc.putTotalPages === 'function') {
    doc.putTotalPages(totalPagesExp)
  }
   
   doc.save('table.pdf'); 
}





	  
	 
function toDataURL(url, callback) {
  var xhr = new XMLHttpRequest();
  xhr.onload = function() {
    var reader = new FileReader();
    reader.onloadend = function() {
      callback(reader.result);
    }
    reader.readAsDataURL(xhr.response);
  };
  xhr.open('GET', url);
  xhr.responseType = 'blob';
  xhr.send();
}





    
	  



 $(function () {
    
    //Date range picker
    $('#reservation').daterangepicker();
    
	//Date range picker with time picker
    $('#reservationtime').daterangepicker({
      timePicker: true,
      timePickerIncrement: 30,
      locale: {
        format: 'MM/DD/YYYY hh:mm A'
      }
    })
    

  });



Date.prototype.toDateInputValue = (function() {
    var local = new Date(this);
    local.setMinutes(this.getMinutes() - this.getTimezoneOffset());
    return local.toJSON().slice(0,10);
});

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

$('#messagetextarea').keyup(function() {
	if(this.value.length<=160){
			$('#smsperperson').html(1);

	}else if(this.value.length>160 && this.value.length<=320){
			$('#smsperperson').html(2);

	}else if(this.value.length>160 && this.value.length<=320){
		
			$('#smsperperson').html(3);

	}else if(this.value.length>320 && this.value.length<=480){
		
			$('#smsperperson').html(4);

	}else if(this.value.length>480 && this.value.length<=640){
		
			$('#smsperperson').html(5);

	}else if(this.value.length>640 && this.value.length<=800){
		
			$('#smsperperson').html(6);

	}else if(this.value.length>800 && this.value.length<=960){
		
			$('#smsperperson').html(7);

	}else if(this.value.length>960 && this.value.length<=1120){
		
			$('#smsperperson').html(8);

		
	}else if(this.value.length>1120 && this.value.length<=1280){
		
			$('#smsperperson').html(9);

	}else if(this.value.length>1280 && this.value.length<=1440){
		
		$('#smsperperson').html(10);
	}
});



$(function(){
	
    $('#sendthattextbutt').click(function() {


			if (!$.trim($("#phonenumberstextarea").val())) {				

				$(function () {
								$("#change-me").html('Please select at least one category of recipients');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			

			
			else if (!$.trim($("#messagetextarea").val())) {				

				$(function () {
								$("#change-me").html('Please type a message before trying to send');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
				
		     else{
				 
				Myurl='sendthattext';
				openTestModal();
 
			 }
    });
});

$(function(){
	$("#accountbal").click(function(){
	   Myurl='checksmsaccbalance';
	   openTestModal();
	});
});

$(function(){
	
      $('#bothorsingleparentselect').on('change', function () {
          var url = $(this).val();		  		  		  
          Myurl='postsmslisttogetphones';
		  openTestModal();
		  
          return false;
      });
	  
	  
	  
});

$(function(){
	$('#defaultModalbtnsearchstudent').click(function(){		
		
		$.ajax({
				url : '/api/sms/getaccoutbal',
				contentType : "application/json",
				success: function(data){		
					 $('#accountbal').html('BAL | '+JSON.parse(data)[0].smsaccbal);
				}		
				});	
		
	    $.post("newmessage").done(function (fragment) {	  				   
			$("#output").replaceWith(fragment);			  		
        });
		
		Myurl='getsearchstudents';
		openTestModal();		
	});
});


$(function(){
	$('#closesearchstudentsmsbut').click(function(){		
		Myurl='postsmslisttogetphones';
	});
});





$(function(){
	$("#getsearchedstudentsbutt").click(function(){
	   //Myurl='checksmsaccbalance';
	   //openTestModal();
	   
	   searchedphones.length=0;
		
		if($('#bothorsingleparentselect').val()=="One parent"){
			
			$(tablebeingedited).DataTable().column(6).data().each(function(value, index) {
			 if (value && $.inArray(value, searchedphones)=="-1") {searchedphones.push(value);}
			});
		}
		
		else if($('#bothorsingleparentselect').val()=="Both parents"){
			
			$(tablebeingedited).DataTable().column(6).data().each(function(value, index) {
				if (value && $.inArray(value, searchedphones)=="-1") {searchedphones.push(value);}
			});
			$(tablebeingedited).DataTable().column(7).data().each(function(value, index) {
				if (value && $.inArray(value, searchedphones)=="-1") {searchedphones.push(value);}
			});
			
		}
		
		else{
			$(tablebeingedited).DataTable().column(6).data().each(function(value, index) {
				if (value && $.inArray(value, searchedphones)=="-1") {searchedphones.push(value);}
			});
			$(tablebeingedited).DataTable().column(7).data().each(function(value, index) {
				if (value && $.inArray(value, searchedphones)=="-1") {searchedphones.push(value);}
			});
			$(tablebeingedited).DataTable().column(8).data().each(function(value, index) {
				if (value && $.inArray(value, searchedphones)=="-1") {searchedphones.push(value);}
			});
		}
		
			$('#phonenumberstextarea').val(searchedphones); 
			$('#smstotalrecipients').html(numberWithCommas(searchedphones.length));
			Myurl='postsmslisttogetphones';
 
								
			var indexed_array = {};
			indexed_array['numarray'] = searchedphones;	

				
			
		
			$.ajax({
					
					type : "POST",
					contentType : "application/json",
					url : "/api/sms/receivenumbersfromsearchstudent",
					data : JSON.stringify(indexed_array),
					success: function(data){

					},
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError);
					}		
			});
		
		
		
		
				
	   
	});
});






// Finance   Module
$(function(){
	
      $('#feestructureselectclasscombo').on('change', function () {          
		  if($("#feestructureterm").val() != "" && $("#feestructureyear").val() != ""){
			  var url = $(this).val();		  		  		  
			  Myurl='postclasstogetfeestructure';
			  openTestModal(); 
			  
		  }  		  
          return false;
      });
	  
	  $('#feestructureyear').on('change', function () {          
		  if($("#feestructureterm").val() != "" && $("#feestructureselectclasscombo").val() != ""){
			  var url = $(this).val();		  		  		  
			  Myurl='postclasstogetfeestructure';
			  openTestModal(); 
		  }  		  
          return false;
      });
	  
	  $('#feestructureterm').on('change', function () {          
		  if($("#feestructureselectclasscombo").val() != "" && $("#feestructureyear").val() != ""){
			  var url = $(this).val();		  		  		  
			  Myurl='postclasstogetfeestructure';
			  openTestModal(); 
		  }  		  
          return false;
      });
});



function addinvoiceitem() {	
	$("#feestructureaccount").val('');
	$("#feestructureamount").val('')
	$('#myModaladdinvoiceitem').modal({
		backdrop: 'static',
		keyboard: false
	});
}


$(function(){
	$('#submitaddinvoiceitembutt').click(function(){

		 if ($("#feestructureaccount").val()==='') {

			$("#change-me").html('Please select votehead');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				
		}else if (!$.trim($("#feestructureamount").val())) {				

				$("#change-me").html('Please input votehead amount');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
		}else{
		
		Myurl='postaddinvoiceitem';
		openTestModal(); 
		
		}
	});
});


	$('#submitorshowstudentsforfeestructure').click(function(){
		
			  if ($('#feestructuremaindiv').css('display') == 'block') {	

				if ($("#feestructureselectclasscombo").val()!='' && $("#feestructureyear").val()!='' && $("#feestructureterm").val()!='') {			  
					Myurl='postgetstudentstoinvoice';
					openTestModal();
				}else{
								
								$("#change-me").html('Please select class and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				}					
			  }
			
			else {
				
				Myurl='poststudentstoinvoice';					
				openTestModal(); 

			}
			
	});	
	


	$('#feestructuregobackbut').click(function(){
				$("#divselectstudentsforbilling").hide();
				$("#feestructuremaindiv").show();
				Myurl='postclasstogetfeestructure';			
		
	});


function copyvotes() {	
	if ($("#feestructureyear").val()!='' && $("#feestructureterm").val()!='') {		  				
		$("#classtocpyfromcombo").val('');
		$("#classtocopytocombo").val('');
		
		$('#copyvotesmodal').modal({
			backdrop: 'static',
			keyboard: false
		});		
	}
	else{
								
								$("#change-me").html('Please select class and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
		}	
}

$('#submitcopyclassbutt').click(function(){
					

		if ($("#classtocpyfromcombo").val()!='' && $("#classtocopytocombo").val()!='') {		  				
					Myurl='postcopyvoteheads';	
					openTestModal(); 	
		}
		else{
								
								$("#change-me").html('Please select class and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
		}
					
});



function replicatevotes() {	

		if ($("#feestructureselectclasscombo").val()!='' && $("#feestructureyear").val()!='' && $("#feestructureterm").val()!='') {			  				
			$("#replicatevotescombo").val('');
			$("#replicatevotesaddamt").val('0')
			$('#replicatevotes').modal({
				backdrop: 'static',
				keyboard: false
			});
	
		}
		else{
								
								$("#change-me").html('Please select class and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
		}	
	
}


$('#submitreplicatevotes').click(function(){
					

		if ($("#replicatevotescombo").val()!='' && $("#replicatevotesaddamt").val()!='') {		  				
					Myurl='postvotestoreplicate';	
					openTestModal(); 	
		}
		else{
								
								$("#change-me").html('Please select votehead and amount and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
		}
					
});



$('#feepaymentproceedbutt').click(function(){
 
     var todaysDate = new Date();
	 var inputDate = new Date($("#feepaymentdate").val());
	
 
	if($("#feepaymentrctamt").val()==''){
		
		$("#change-me").html('Please provide rct amount and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);
		
	}
	
	else if($("#studentrctnu").val()==''){
		
		$("#change-me").html('Please provide rct number and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);
		
	}
	
	else if($("#feepaymentdate").val()==''){
		
		$("#change-me").html('Please provide transaction date and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);
		
	}
	
	else if($("#feepaymentpaymentmethod").val()==''){
		
		$("#change-me").html('Please select payment method and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);
		
	}
	
	else if($("#feepaymentdepodate").val()=='' && requestdateandtransnu==1){
		
		$("#change-me").html('Please provide slip/cheque deposit date and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);
		
	}
	
	else if($("#feepaymenttransnu").val()=='' && requestdateandtransnu==1){
		
		$("#change-me").html('Please provide slip/cheque transaction number and try again');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);
		
	}
	
	else if((inputDate.setHours(0,0,0,0) != todaysDate.setHours(0,0,0,0))  && allowbackdating==0) {

			$("#change-me").html('Back dating or front dating  is disabled');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);


	}
	
	else{
		
				Myurl='processandloadstudentreceipt';					
				openTestModal(); 
	
		$("#paayfeesdisplayreceipt").show();
		$("#payfeesmainpage").hide();	
	
	}
});

$('#feespaymentkbut').click(function(){	 
	$("#payfeesmainpage").show();
	$("#paayfeesdisplayreceipt").hide();	
});

$('#rcyautocheck').click(function(){	 
	
});





$(function(){
    $('#saveexambutt').click(function() {


			if (!$.trim($("#examdate").val())) {				

				$("#modalprogress").modal("hide");
				
				$(function () {
								$("#change-me").html('Please select date exam created');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			else if (!$.trim($("#examcategory").val())) {				

				$("#modalprogress").modal("hide");
				
				$(function () {
								$("#change-me").html('Please select exam category');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			else if (!$.trim($("#examclass").val())) {				

				$("#modalprogress").modal("hide");
				
				$(function () {
								$("#change-me").html('Please select exam class');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			else if (!$.trim($("#examterm").val())) {				

				$("#modalprogress").modal("hide");
				
				$(function () {
								$("#change-me").html('Please select exam term/semester');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			
			else if (!$.trim($("#customexamname").val()) && $("#customexamnamecheck").prop('checked') == true) {				

				$("#modalprogress").modal("hide");
				
				$(function () {
								$("#change-me").html('Please provide custom exam name');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
								
						
			}
				
		    else if($("#saveexambutt").text()=="Save"){
				Myurl='addexam';
				openTestModal();
			}
			 
			else if($("#saveexambutt").text()=="Save Changes"){
				Myurl='updateexam';
				openTestModal();
			}
    });
});










// Exam analysis


$(function() {
				
	$('#defaultModalbtncreatenewexam').click(function() {
			
			$('#customexamnamecheck').attr("disabled", false);							
			$('#includeallclassescheck').attr("disabled", false);
			
			$('#customexamname').val('');
			
			$('#examdate').val('');
			$('#examterm').val('');
			$('#examclass').val('');
			$('#examcategory').val('');
			
			$("#saveexambutt").html("Save");  		
				
			
			$('#popupcreatenewexam').modal({
					backdrop : 'static',
					keyboard : false
			});
		
	});
			
});











$(function(){
    $('#defaultModalbtncreatenewexamcategory').click(function() {
			
			$('#popupcreatenewexamcategory').modal({
					backdrop : 'static',
					keyboard : false
			});

    });
});






function showselecttechercombo(){  						
	
	$('#myInput').val($('#selectremarkcategorycombo').val());
	
	
	if($('#selectremarkcategorycombo').val()=='Teachers'){		
	
		$('#remarksteacherdiv').show();
		$('#selectteacherremarkdivcombo').show();
		$('#selectteacherremarkdivteacher').show();		
		$('#selectteacherremarkdivcategory').hide();		
		
	}else{
		
		$('#remarksteacherdiv').hide();
		$('#selectteacherremarkdivcombo').hide();
		$('#selectteacherremarkdivteacher').hide();
		$('#selectteacherremarkdivcategory').show();		
	}
	
	
	Myurl='loadremarks';
	openTestModal();	

	
};





function  selectgradingcategory(){  						
	$('#myInputcategory').val($('#selectgradingsystemcategorycombo').val());
	$('#myInputclass').val($('#selectgradingclasscombo').val());
	
	Myurl='loadgradingsystem';
	openTestModal();		
};


function  selectgradingclass(){  						
	$('#myInputclass').val($('#selectgradingclasscombo').val());
	$('#myInputcategory').val($('#selectgradingsystemcategorycombo').val());		
	Myurl='loadgradingsystem';
	openTestModal();		
};



function copyremarks(){
			
			if ($('#selectteacherremarkdivcombo').is(':visible')) {
			
				if (!$.trim($("#selectremarkteachercombo").val())) {				

					$(function () {
									$("#change-me").html('Please select at least one teacher and try again');
									$('#myModalError').modal('show');
									setTimeout(function () {
										$('#myModalError').modal('hide');
									}, 2000);
					});
					
				} 
				
				
				else{
				
					$('#popupcopyremarks').modal({
						backdrop : 'static',
						keyboard : false
					});	
				
				}
				
				
			}
			
			else {
			
				if (!$.trim($("#selectremarkcategorycombo").val())) {				

					$(function () {
									$("#change-me").html('Please select at least one category to copy to');
									$('#myModalError').modal('show');
									setTimeout(function () {
										$('#myModalError').modal('hide');
									}, 2000);
					});
					
				} 
				
				else{
				
					$('#popupcopyremarks').modal({
						backdrop : 'static',
						keyboard : false
					});	
				
				}
					
			}
			
			
			
		
			
   
};


function copygradingsystem(){
			
			if (!$.trim($("#selectgradingsystemcategorycombo").val())) {				

				$(function () {
								$("#change-me").html('Please select at least one category to copy to');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			} 
			
			else{
				
				$('#popupcopygradingsystem').modal({
					backdrop : 'static',
					keyboard : false
				});	
				
			}
			
		
			
   
};




function submitcopygradingsystem() {


			if (!$.trim($("#categorytocopygradingto").val())) {				

				$(function () {
								$("#change-me").html('Please select at least one category to copy to');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			else if ($("#categorytocopygradingto").val()==$("#selectgradingsystemcategorycombo").val()) {				

				$(function () {
								$("#change-me").html('Can not copy to same category');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
		     else{
				 
				$("#popupcopygradingsystem").modal("hide");
				Myurl='api/exam/copygrading';
				openTestModal();
 
			 }
    };







function submitremark() {


			if (!$.trim($("#categorytocopyremarkto").val())) {				

				$(function () {
								$("#change-me").html('Please select at least one category to copy to');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			else if ($('#selectteacherremarkdivteacher').is(':visible') &&  !$.trim($("#selectremarkteachercombo").val())) {				

				$(function () {
								$("#change-me").html('Please select at least one teacher to copy to');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			else if($('#selectteacherremarkdivcategory').is(':visible') && $("#selectremarkcategorycombo").val()!='' && ($("#selectremarkcategorycombo").val()  == $("#categorytocopyremarkto").val())) {				

				$(function () {
								$("#change-me").html('Can not copy to the same category');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			else if ($('#selectteacherremarkdivteacher').is(':visible') &&  $("#selectremarkteachercombofrommainpage").val()!='' && ($("#selectremarkteachercombofrommainpage").val()  == $("#selectremarkteachercombo").val())) {				

				$(function () {
								$("#change-me").html('Can not copy to same teacher');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
		     else{
				 
				$("#popupcopyremarks").modal("hide");
				Myurl='api/exam/copyremarks';
				openTestModal();
 
			 }
    };







$('#customexamnamecheck').on('change', function() {
	
    if($("#customexamnamecheck").prop('checked') == false){
		$( '#customexamnamediv' ).hide();
	}
	else{
		$( '#customexamnamediv' ).show();
	}	
	
});


function filterexamby(){	

	if($('#filterbyyear').val()!="" && $('#filterbyclass').val()!="" && $('#filterbyterm').val()!=""){
			Myurl='loadexamsby';
			openTestModal();	
	}		
	
}
















function selectclassreportdates(){  						
	$('#myInputcategory').val($('#reportformdatesclass').val());		
	Myurl='loadmoresettings';
	openTestModal();		
};


function savereportformdates() {


			if (!$.trim($("#reportformdatesclass").val())) {				

				$(function () {
								$("#change-me").html('Please select at least one class');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
			}
			
			
		     else{
				 
				Myurl='savereportformdates';
				openTestModal();
 
			 }
    
};




$('.nav li a').click(function(e) {	
        $('.nav li.active').removeClass('active');
        var $parent = $(this).parent();
        $parent.addClass('active');
		$('#toglebutt').click();
        e.preventDefault();
});








function loadregdashboard() {	

		
       $.post("registrationmain").done(function (fragment) {	  				   
			
			$("#output").replaceWith(fragment);
			md.initDashboardPageCharts();
			
			
			$('#regmenuname').show();
			$('#regsearchbut').hide(); 
	
			
  		
       });
	 
}





function loadlistofstudents() {
		Myurl='students';
		openTestModal();	
}



function loadlistofteachers() {
		Myurl='teachers';
		openTestModal();		
}


function loadlistofstaff() {
		Myurl='staff';
		openTestModal();
}

function loadlistofboard() {
		Myurl='board';
		openTestModal();
	
}





$(document).on("keydown", +myform, function(event) { 
    return event.key != "Enter";
});


function submitForm(event){
      
	  event.preventDefault();	   
	   
	    var unindexed_array = $(myform).serializeArray();
		var indexed_array = {};
		indexed_array['id'] = id;
		
		 
			
		if(Myurl=='postteacherdataforreg'){
			indexed_array['substought'] = rows_selected;
		}
		
		
		
		
		$.map(unindexed_array, function(n, i){
			indexed_array[n['name']] = n['value'];
		});
		
		
		if(actionsaveorupdate=="Save Changes"){
			
				
						if(Myurl=='poststudentdataforreg'){
							actionurl="/api/reg/updatestudent";
						}else if(Myurl=='postteacherdataforreg'){
							actionurl="/api/reg/updateteacher";
						}else if(Myurl=='poststaffdataforreg'){
							actionurl="/api/reg/updatestaff";
						}else if(Myurl=='postboarddataforreg'){
							actionurl="/api/reg/updateboard";
						}
						
						
						else if(Myurl=='postgradesdataforreg'){
							actionurl="/api/exam/updategrade";
						}else if(Myurl=='postsubjectgroupsdataforreg'){
							actionurl="/api/exam/updatesubjectgroup";
						}else if(Myurl=='postsubjectsdataforreg'){
							actionurl="/api/exam/updatesubject";
						}else if(Myurl=='postexamcategoriesdataforreg'){
							actionurl="/api/exam/updateexamcategory";
						}else if(Myurl=='postremarksdataforreg'){
							actionurl="/api/exam/updateremark";
							indexed_array['category'] = $('#selectremarkcategorycombo').val();					
						}else if(Myurl=='postgradingsystemforreg'){
							actionurl="/api/exam/updategradingsystem";
							indexed_array['category'] = $('#selectgradingsystemcategorycombo').val();		
						}
						
						
				
		}else{
			
						if(Myurl=='poststudentdataforreg'){
							actionurl="/api/reg/addstudent";
						}else if(Myurl=='postteacherdataforreg'){
							actionurl="/api/reg/addteacher";
						}else if(Myurl=='poststaffdataforreg'){
							actionurl="/api/reg/addstaff";
						}else if(Myurl=='postboarddataforreg'){
							actionurl="/api/reg/addboard";
						}else if(Myurl=='loadlistofexams'){
							actionurl="/api/reg/addexam";
						}
						
						else if(Myurl=='postgradesdataforreg'){
							actionurl="/api/exam/addgrade";
						}else if(Myurl=='postsubjectgroupsdataforreg'){
							actionurl="/api/exam/addsubjectgroup";
						}else if(Myurl=='postsubjectsdataforreg'){
							actionurl="/api/exam/addsubject";
						}else if(Myurl=='postexamcategoriesdataforreg'){
							actionurl="/api/exam/addexamcategory";
						}else if(Myurl=='postremarksdataforreg'){
							actionurl="/api/exam/addremark";
							indexed_array['category'] = $('#selectremarkcategorycombo').val();								
						}else if(Myurl=='postgradingsystemforreg'){						
							actionurl="/api/exam/addgradingsystem";
							indexed_array['category'] = $('#selectgradingsystemcategorycombo').val();		
						}

				
		}
		
		
		
		
			
		
		$.ajax({
			type : "POST",
			contentType : "application/json",
			url : actionurl,
			data : JSON.stringify(indexed_array),
			dataType : 'json',
			success : function(result) {						
				
				var object = result[0];
				
					
				if(object['querystatus']=="Operation successfull"){
	
					$(function () {
						$(modaltoopen).modal('hide');
						$("#messageid").html(object['querystatus']);
						$('#myModal').modal('show');
						setTimeout(function () {
							$('#myModal').modal('hide');
						}, 1000);
					});

					$(myform)[0].reset();					
					
					
					var html ='<tr>';
										
												html +='<td></td>'
											
											  for (var property in object) {

												
												if(property=="id"){									
													html +='<td style="display:none" class="id_event">' + object[property]+ '</td>';						
												}
												else if(property=="action"  || property=="Action"){									
													html +="<td class='project-actions text-center'>    <a id='btnView' class='btn btn-primary btn-sm' href='#' title='"+viewtitle+"'><i class='fas fa-eye'></i></i></a>     <a  id='btnEdit'  style='margin:5px' class='btn btn-info btn-sm' href='#'><i class='fas fa-pencil-alt'></i> </a>     <a id='btnDelete'  class='btn btn-danger btn-sm' href='#'><i class='fas fa-trash'></i></a>    </td>";																									
												}
												else if(property=="status"){									
													html +='<td class="project-state text-left"><span class="badge badge-success">'+object[property]+'</span></td>';																																											
												}else if(property=="profile"){									
													html +='<td class="project_progress"><div class="progress progress-sm"><div class="progress-bar bg-green" role="progressbar" aria-valuenow="57" aria-valuemin="0" aria-valuemax="100" style="width: 57%"></div></div><small>'+object[property]+'</small> </td>';																												
												}
												else if(property=="simage"){									
												html +='<td> <img  style= "width:50px;height:50px; border-radius: 50%; " src='+object[property]+'> </td>';																													
													}
												else{ 	
													html +='<td>' +object[property]+ '</td>';										
												}	
												
											  
											  }
									
						html +='</tr>';
						
						
				if(actionsaveorupdate=="Save"){					

					 var table = $(tablebeingedited).DataTable();
					 const tr = $(html);
					 table.row.add(tr[0]).draw();

					
				}
				
				else{

				
					var table = $(tablebeingedited).DataTable();
					table.row(rowindex).remove();		 					
					
					var currentPage = table.page();
					const tr = $(html);					
					table.row.add(tr[0]).draw();					
					var rowCount = table.data().length-1,
						insertedRow = table.row(rowCount).data(),
						tempRow;
					for (var i=rowCount;i>rowindex;i--) {
						tempRow = table.row(i-1).data();
						table.row(i).data(tempRow);
						table.row(i-1).data(insertedRow);
					}     
					
					table.page(currentPage).draw(false);

     			}
				
					 	
						
						
						
						
						
						$(tablebeingedited+' tbody').on('click', '#btnEdit', function(){
								var table = $(tablebeingedited).DataTable();
								var tr = $(this.closest('tr'));
								if(tr.hasClass('child')){
									tr = tr.prev();
								}
								rowindex=table.row(tr).index();						
								var data = table.row(tr).data();						
								data.forEach(function(d, index, arr) {
									d = $('<div>').html(d);
									arr[index] = d.val() || d.text()
								  })						
								id=data[3];					 
					 
							 var indexed_array = {};
								indexed_array['id'] = id;		
								//indexed_array['idcol'] = idcol;		
								
								$.ajax({
									type : "POST",
									contentType : "application/json",
									url : Myurl,
									data : JSON.stringify(indexed_array),
									dataType : 'json',
									success : function(result) {
										$(modaltoopen).modal('show');
										actionsaveorupdate='Save Changes';
															
										populate(myform, result,Myurl);
										
			
									},
									error : function(e) {
										console.log("ERROR: ", e);
									}
								});
							 
							 event.preventDefault();
		
						});
						
						
							
				}else{
					
					$(function () {
									$("#errormessage").html(object['querystatus']);
										$('#myModalErrorstop').modal('show');
											setTimeout(function () {
											$('#myModalErrorstop').modal('hide');
										   }, 2000);
										});	
					
				}
				
												
				
				
			
				
			},
			error : function(e) {
				console.log("ERROR: ", e);
			}
		});
		
       
        };




function proceedtosendsms() {

    
		$('#popupsendingmsg').modal({
				backdrop: 'static',
				keyboard: false
		});	

};




function loadnewsms() {
		
		Myurl='loadnewsms';
		openTestModal();
			
      
}


function loadsmsdashboard() {
		
				
			
			
		
       $.post("smsmain").done(function (fragment) {	

			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
				
				
	   
				$("#output").replaceWith(fragment);
				md.initDashboardPageCharts();
				
				

			}
			
			
       });
	 
  		
}
	
	
	
	
	
	
	
	
	
	
	
	
	



$('.nav-links li a').click(function(e) {	
        $('.nav-links li.active').removeClass('active');
        var $parent = $(this).parent();
        $parent.addClass('active');
		$('#toglebutt').click();
        e.preventDefault();
});




function loadexamdashboard() {	

		
			
		
       $.post("exammain").done(function (fragment) {

			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{

		   
			$("#output").replaceWith(fragment);
			
			 dataCompletedTasksChart  = {
				labels: ['12p', '3p', '6p', '9p', '12p', '3a', '6a', '9a'],
				series: [
				  [230, 750, 450, 300, 280, 240, 200, 190]
				]
			  };
			 
			 
			  var chartexamalldashboard = new Chartist.Line('#chartexamalldashboard', dataCompletedTasksChart, null);
			  md.startAnimationForLineChart(chartexamalldashboard);
			
			
			$('#regmenuname').show();
			$('#regsearchbut').hide(); 
			
			}
	
		
       });
	
		
  		
}





function loadlistofexams() {
		
		Myurl='listofexams';
		openTestModal();
		
}


var tablemain="#example1";
	  
function loadlistofallowedexams() {  
  Myurl='allowedexams';
  openTestModal();
	
}


function loadsubjectselection() {
	
	Myurl='subjectselection';
	openTestModal();
	 
}


function grades() {		
	Myurl='grades';
	openTestModal();	
}
	
	
function subjectgroups() {
	Myurl='subjectgroups';
	openTestModal();	
}



function subjects() {	
	Myurl='subjects';
	openTestModal();	
}





function examcategories() {	
	Myurl='examcategories';
	openTestModal();
}
	
	
	
	

function remarks() {
	Myurl='remarks';
	openTestModal();	
}
	

function gradingsystem() {	
	Myurl='gradingsystem';
	openTestModal();		
}
		
		
		
function moresettings() {
	
	  $('#regsearchbut').hide();
	  $('#regmenuname').hide(); 
	
	
				  
      $.post("moresettings").done(function (fragment) {
			
			
			if(fragment.includes("change-me")){
			
				$(function () {
								$("#change-me").html("You dont have permission to access this module");
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 2000);
				});
				
				
			}else{
					 
				   $("#output").replaceWith(fragment);
				   
			}
				  
     }); 
	 
	  
  
	
}
		
		
		
function filtersubjectsbyclass() {		
    if($('#filtersubjectsbyclass').val()!=""){
		Myurl='loadsubjectsby';
		openTestModal();	
	}
}
		
		
		
		
function loadsubjectselectionperclass(){  						
	Myurl='loadsubjectselection';
	openTestModal();		
};

		
		
function submitsubjectselection(){
	
	var table = $(maintable).DataTable();
	
	var employees = {marks: [],classs: []};		
	table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {       
	var Row = this.data();
	for (i=3; i<collumns.length; i++) {																			
		employees.marks.push({"studentid" : Row[0],'sub' : collumns[i],'yesno' : Row[i]});									
	}
	 								
	});	
	employees.classs.push({"class" : $('#subjectselectionclass').val().replace(/^\s+|\s+$/g, "")});	

	
	
	$.ajax({					
					type : "POST",
					contentType : "application/json",
					url : "/api/exam/submitsubjectselection",
					data : JSON.stringify(employees),
					dataType : 'json',
					success: function(data){									
						
						
						$("#modalgeneratereports").modal("hide");
						
						var object =data[0];
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 1500);
									});
						

					},
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError+"  mceee  "+xhr);
					}		
			});
	
	
}



function submitmarks(){
	
var tablemain="#example1";
	
	if($("#outofbox").val()==''){
		
		$("#change-me").html('Please provide out of value');
								$('#myModalError').modal('show');
								setTimeout(function () {
									$('#myModalError').modal('hide');
								}, 3000);
		
	}
	
	else{
	
	
	var table = $(maintable).DataTable();
	var employees = {marks: []};		
	table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {       
	var Row = this.data();								  
	   employees.marks.push({ 
		 "studentid" : Row[0],
		 "sub" : Row[1],
		 "exam" : Row[2],
		 "outof" : $('#outofbox').val().replace(/^\s+|\s+$/g, ""),
		 "value"  : this.cell(rowIdx,5).nodes().to$().find('input').val()
		});									
	});	
	
	console.log(employees);
	
	$.ajax({					
					type : "POST",
					contentType : "application/json",
					url : "/api/exam/postmarks",
					data : JSON.stringify(employees),
					success: function(data){									
						
						$("#modalgeneratereports").modal("hide");
						
						var object = JSON.parse(data)[0];
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 1500);
									});
						

					},
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError+"  mceee  "+xhr);
					}		
			});
	
	}
}



		
function submitmarksadmin(){
	
		
						
	var table = $("#example2").DataTable();
	
	
		var allmarks = {employees: [],examname: []};
	
	table.rows().every( function ( rowIdx, tableLoop, rowLoop ) {       
			var Row = this.data();	
			
			var indexed_array = {};
			indexed_array['studentid'] = Row[0];			 			
			for (i=3; i<collumns.length; i++) {	
			 indexed_array[[collumns[i]]] = this.cell(rowIdx,i).nodes().to$().find('input').val();				
			} 		  
			
			allmarks.employees.push(indexed_array);
			
			});

				
				allmarks.examname.push($("#examdetails").text().split(" - ")[0]);
				
				$.ajax({					
					type : "POST",
					contentType : "application/json",
					url : "/api/exam/postmarksasadmin",
					data : JSON.stringify(allmarks),
					success: function(data){									
						
						
						var object = JSON.parse(data)[0];
									$(function () {
										$("#messageid").html(object['querystatus']);
										$('#myModal').modal('show');
										setTimeout(function () {
											$('#myModal').modal('hide');
										}, 1500);
									});
						

					},
					error:function (xhr, ajaxOptions, thrownError) {
						console.log(thrownError+"  mceee  "+xhr);
					}		
				});
				
 
  				
	
}






function selectgetexamreposrts(){
	
	
	
	var indexed_array = {};
		indexed_array['examname'] = $("#examdetails").text().split(" - ")[0];
		indexed_array['stream'] = $("#selectexamstreams").val();
		indexed_array['streamlong'] = $( "#selectexamstreams option:selected" ).text();
		indexed_array['report'] = $("#selectgetexamreports").val();
		
		if ($.trim($("#selectgetexamreports").val())) {	

			
					

						$.ajax({
						
								type : "POST",
								contentType : "application/json",
								url : "/api/exam/getexamreports",
								data : JSON.stringify(indexed_array),
								success : function(result) {
									
									$("#modalprogress2").modal("hide");
									var sampleArr = base64ToArrayBuffer(result);
									var file = new Blob([sampleArr], {type: 'application/pdf'});
									var fileURL = URL.createObjectURL(file);
									window.open(fileURL);						
							
									
											
								},
								error:function (xhr, ajaxOptions, thrownError) {
									console.log(thrownError+"  mceee  "+xhr);
								}		
						});			
				
	
  			
	}
						
	
}


















function loadbiostudents() {		
		Myurl='loadbiostudents';
		openTestModal();		
}

function loadbiodashboard() {		
		Myurl='biometricsmain';
		openTestModal();		
}



