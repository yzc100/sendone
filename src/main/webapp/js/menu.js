  	$(document).ready(function(){
  		var m = $.getUrlParam("m");
  		var m_a = 0;
  		var m_b = 0;
  		var m_c = 0;
  		if(m==null){ 
  			m ="0";
  		}

  		if(m.indexOf("-")>0){
  			var m_array = m.split("-");
  			m_a = m_array[0];
  			if(m_array.length==2){
  				m_b = m_array[1];
  			}else{
  				m_b = m_array[1];
  				m_c = m_array[2];
  			}
//  			alert(m_array.length);
  		}
//  		alert(m_a+"-"+m_b+"-"+m_c)
  		$.getJSON("/k-occ/menu/list",function(msg){
  			var menu_text = "";
  			var nav_title = "";
  			var nav_text = " <li><i class=\"fa fa-home\"></i><a href=\"#\">Home</a></li>";
	  		$.each(msg,function(i,model){
	  		var childnode = "";
	  		if(jQuery.isEmptyObject(model.child)){
	  			if(m_a==i){
	  				nav_title="<h1>"+model.name+"</h1>";
	  				if(m_b==0){
	  					nav_text+="<li class=\"active\">"+model.name+"</li>";
	  				}else{
	  					nav_text+="<li><a href="+model.url+"?m="+i+">"+model.name+"</a></li>";
	  				}
	  				menu_text+="<li class=\"active\"><a href="+model.url+"?m="+i+"><i class='"+model.css+"'></i><span class=\"menu-text\"> "+model.name+" </span></a></li>";
	  			}else{
	  				menu_text+="<li><a href="+model.url+"?m="+i+"><i class='"+model.css+"'></i><span class=\"menu-text\"> "+model.name+" </span></a></li>";
	  			}
	  		}else{
	  			if(m_a==i){
	  				nav_title="<h1>"+model.name+"</h1>";
	  				if(m_b==0){
	  					nav_text+="<li class=\"active\">"+model.name+"</li>";
	  				}else{
	  					nav_text+="<li><a href="+model.url+"?m="+i+">"+model.name+"</a></li>";
	  				}
	  				menu_text+="<li class=\"active open\"><a href=\"#\" class=\"menu-dropdown\"><i class='"+model.css+"'></i><span class=\"menu-text\"> "+model.name+" </span> <i class=\"menu-expand\"></i></a><ul class=\"submenu\" style=\"display:block\">";
	  			}else{
	  				menu_text+="<li><a href=\"#\" class=\"menu-dropdown\"><i class='"+model.css+"'></i><span class=\"menu-text\"> "+model.name+" </span> <i class=\"menu-expand\"></i></a><ul class=\"submenu\" style=\"display:none\">";
	  			}
	  			$.each(model.child,function(k,m2){
	  				if(jQuery.isEmptyObject(m2.child)){
	  					if(m_b ==k){
	  						nav_title="<h1>"+m2.name+"</h1>";
	  						if(m_c==0){
	  							menu_text+="<li class=\"active\"><a href="+m2.url+"?m="+i+"-"+k+"><i class='"+m2.css+"'></i><span class=\"menu-text\"> "+m2.name+" </span></a></li>";
	  		  					nav_text+="<li class=\"active\">"+m2.name+"</li>";
	  		  				}else{
	  		  				menu_text+="<li class=\"active open\"><a href="+m2.url+"?m="+i+"-"+k+"><i class='"+m2.css+"'></i><span class=\"menu-text\"> "+m2.name+" </span></a></li>";
	  		  					nav_text+="<li><a href="+m2.url+"?m="+i+"-"+k+">"+m2.name+"</a></li>";
	  		  				}
	  						
		  				}else{
		  					menu_text+="<li ><a href="+m2.url+"?m="+i+"-"+k+"><i class='"+m2.css+"'></i><span class=\"menu-text\"> "+m2.name+" </span></a></li>";
		  				}
	  				}else{
	  					if(m_b==k){
	  						nav_title="<h1>"+m2.name+"</h1>";
	  						if(m_c==0){
	  		  					nav_text+="<li class=\"active\">"+m2.name+"</li>";
	  		  				}else{
	  		  					nav_text+="<li><a href="+m2.url+"?m="+i+"-"+k+">"+m2.name+"</a></li>";
	  		  				}
	  		  				menu_text+="<li class=\"active open\" ><a href=\"#\" class=\"menu-dropdown\"><i class='"+m2.css+"'></i><span class=\"menu-text\"> "+m2.name+" </span> <i class=\"menu-expand\"></i></a><ul class=\"submenu\" style=\"display:block\">";
	  		  			}else{
	  		  				menu_text+="<li><a href=\"#\" class=\"menu-dropdown\"><i class='"+m2.css+"'></i><span class=\"menu-text\"> "+m2.name+" </span> <i class=\"menu-expand\"></i></a><ul class=\"submenu\" style=\"display:none\">";
	  		  			}
	  					$.each(m2.child,function(j,m3){
	  						if(m_c==j){
	  							nav_title="<h1>"+m3.name+"</h1>";
	  							nav_text+="<li class=\"active\">"+m3.name+"</li>";
	  							menu_text+="<li class=\"active\"><a href="+m3.url+"?m="+i+"-"+k+"-"+j+"><i class='"+m3.css+"'></i><span class=\"menu-text\"> "+m3.name+" </span></a></li>";
	  						}else{
	  							menu_text+="<li ><a href="+m3.url+"?m="+i+"-"+k+"-"+j+"><i class='"+m3.css+"'></i><span class=\"menu-text\"> "+m3.name+" </span></a></li>";
	  						}
	  					});
	  					menu_text+="</ul></li>";
	  					
	  				}
	  				
	  			});
	  			menu_text+="</ul></li>";
	  		}
	  		});
	  		$("#m_left").html(menu_text);
  		    $(".breadcrumb").html(nav_text);
  		  $(".header-title").html(nav_title);
  		});
  		
  		
  	});
  		