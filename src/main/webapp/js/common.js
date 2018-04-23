var Public = Public || {};
Public.ajaxPost = function(url, params, callback){
	url="/k-occ"+url;
	$.ajax({  
	   type: "POST",
	   url: url,
	   data: params, 
	   dataType: "json",  
	   success: function(data, status){  
		   callback(data);  
	   },  
	   error: function(err){  
			parent.Public.tips({type: 1, content : '操作失败了哦，请检查您的网络链接！'});
	   }  
	});  
};
Public.ajaxGet = function(url, params, callback){
	url="/k-occ"+url;
	$.ajax({  
	   type: "GET",
	   url: url,
	   dataType: "json",  
	   data: params,    
	   success: function(data, status){  
		   callback(data);  
	   },   
	   error: function(err){  
			parent.Public.tips({type: 1, content : '操作失败了哦，请检查您的网络链接！'});
	   }  
	});  
};
$._messengerDefaults = {
		extraClasses: 'messenger-fixed messenger-theme-future messenger-on-bottom messenger-on-right'
}
$.messenger = function(msg,type){
	switch (type) {
	case 0:
		Notify(msg, 'bottom-right', '5000', 'success', 'fa-check', true);
		break;
	case 1:
		Notify(msg, 'bottom-right', '5000', 'danger', 'fa-bolt', true);
		break;
	case 2:
		Notify(msg, 'bottom-right', '5000', 'warning', 'fa-warning', true);
		break;	
	case 3:
		Notify(msg, 'bottom-right', '5000', 'info', 'fa-envelope', true);
		break;	
	default:
		break;
	}
}
$.getUrlParam = function(name)
{
var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
var r = window.location.search.substr(1).match(reg);
if (r!=null) return unescape(r[2]); return null;
}
$.getDateUtil =function(time,format){
	var dataObj = new Date();
	dataObj.setTime(time);
	var month = (dataObj.getMonth()+1)<10?"0"+(dataObj.getMonth()+1):dataObj.getMonth()+1;
	var day = dataObj.getDate()<10?"0"+dataObj.getDate():dataObj.getDate();
	var hh = dataObj.getHours()<10?"0"+dataObj.getHours():dataObj.getHours();
	var mi = dataObj.getMinutes()<10?"0"+dataObj.getMinutes():dataObj.getMinutes();
	var ss = dataObj.getSeconds()<10?"0"+dataObj.getSeconds():dataObj.getSeconds();
//	if(month<10){
//		month="0"+month;
//	}
//	if(day<10){
//		day="0"+day;
//	}
//	if(hh<10){
//		hh="0"+hh;
//	}
//	if(mi<10){
//		mi="0"+mi;
//	}
	var currentDate ="";
	if(format.indexOf('yyyy')!=-1){
		currentDate+=dataObj.getFullYear();
	}
	if(format.indexOf('mm')!=-1){
		if(currentDate.length>0)
		currentDate+="-"+month;
		else
		currentDate+=month;
	}
	if(format.indexOf('dd')!=-1){
		if(currentDate.length>0)
		currentDate+="-"+day;
		else
		currentDate+=day;
	}
	if(format.indexOf('hh')!=-1){
		if(currentDate.length>0)
		currentDate+=" "+hh;
		else
		currentDate+=hh;
	}
	if(format.indexOf('mi')!=-1){
		if(currentDate.length>0)
		currentDate+=":"+mi;
		else
		currentDate+=mi;
	}
	if(format.indexOf('ss')!=-1){
		if(currentDate.length>0)
		currentDate+=":"+ss;
		else
		currentDate+=ss;
	}
	if(currentDate.length==0){
		currentDate+=dataObj.getFullYear()+"-"+month+"-"+dataObj.getDate()+" "+dataObj.getHours()+":"+dataObj.getMinutes()+":"+dataObj.getSeconds()+"";
	}
	return currentDate;
	}
$.getCurrentDate =function(format){
	var dataObj = new Date();
	var month = dataObj.getMonth()+1;
	var day = dataObj.getDate();
	if(month<10){
		month="0"+month;
	}
	if(day<10){
		day="0"+day;
	}
	var currentDate ="";
	if(format.indexOf('yyyy')!=-1){
		currentDate+=dataObj.getFullYear();
	}
	if(format.indexOf('mm')!=-1){
		if(currentDate.length>0)
		currentDate+="-"+month;
		else
		currentDate+=month;
	}
	if(format.indexOf('dd')!=-1){
		if(currentDate.length>0)
		currentDate+="-"+day;
		else
		currentDate+=day;
	}
	if(format.indexOf('hh')!=-1){
		if(currentDate.length>0)
		currentDate+=" "+dataObj.getHours();
		else
		currentDate+=dataObj.getHours();
	}
	if(format.indexOf('mi')!=-1){
		if(currentDate.length>0)
		currentDate+=":"+dataObj.getMinutes();
		else
		currentDate+=dataObj.getMinutes();
	}
	if(format.indexOf('ss')!=-1){
		if(currentDate.length>0)
		currentDate+=":"+dataObj.getSeconds();
		else
		currentDate+=dataObj.getSeconds();
	}
	if(currentDate.length==0){
		currentDate+=dataObj.getFullYear()+"-"+month+"-"+dataObj.getDate()+" "+dataObj.getHours()+":"+dataObj.getMinutes()+":"+dataObj.getSeconds()+"";
	}
	return currentDate;
}
$.toDecimal=function(x) { 
    var f = parseFloat(x);  
    if (isNaN(f)) {  
        return "0";  
    }  
    var f = Math.round(x*100)/100;  
    var s = f.toString();  
    var rs = s.indexOf('.');  
    if (rs < 0) {  
        rs = s.length;  
        s += '.';  
    }  
    while (s.length <= rs + 2) {  
        s += '0';  
    }  
    return s;  
}