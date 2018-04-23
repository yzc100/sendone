<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%><!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Mobile first web app theme | first</title>
  <meta name="description" content="mobile first, app, web app, responsive, admin dashboard, flat, flat ui">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"> 
  <link rel="stylesheet" href="css/bootstrap.css">
  <link rel="stylesheet" href="css/font-awesome.min.css">
  <link rel="stylesheet" href="css/plugin.css">
  <link rel="stylesheet" href="css/style-t.css">  
  <!--[if lt IE 9]>
    <script src="js/ie/respond.min.js"></script>
    <script src="js/ie/html5.js"></script>
  <![endif]-->
</head>
<body>
  <!-- header -->
  <header id="header" class="navbar">
    <ul class="nav navbar-nav navbar-avatar pull-right">
      <li class="dropdown">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown">            
          <span class="hidden-sm-only">Mika Sokeil</span>
          <span class="thumb-small avatar inline"><img src="images/avatar.jpg" alt="Mika Sokeil" class="img-circle"></span>
          <b class="caret hidden-sm-only"></b>
        </a>
        <ul class="dropdown-menu">
          <li><a href="#">Settings</a></li>
          <li><a href="#">Profile</a></li>
          <li><a href="#"><span class="badge bg-danger pull-right">3</span>Notifications</a></li>
          <li class="divider"></li>
          <li><a href="docs.html">Help</a></li>
          <li><a href="signin.html">Logout</a></li>
        </ul>
      </li>
    </ul>
    <a class="navbar-brand" href="#">first</a>
    <button type="button" class="btn btn-link pull-left nav-toggle hidden-lg" data-toggle="class:show" data-target="#nav">
      <i class="icon-reorder icon-xlarge text-default"></i>
    </button>
    <ul class="nav navbar-nav hidden-sm">
      <li>
        <div class="m-t m-b-small" id="panel-notifications">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-comment-alt icon-xlarge text-default"></i><b class="badge badge-notes bg-danger count-n">2</b></a>
          <section class="dropdown-menu m-l-small m-t-mini">
            <section class="panel panel-large arrow arrow-top">
              <header class="panel-heading bg-white"><span class="h5"><strong>You have <span class="count-n">2</span> notifications</strong></span></header>
              <div class="list-group list-group-flush m-t-n">
                <a href="#" class="media list-group-item">
                  <span class="pull-left thumb-small"><img src="images/avatar.jpg" alt="John said" class="img-circle"></span>
                  <span class="media-body block m-b-none">
                    Moved to Bootstrap 3.0<br>
                    <small class="text-muted">23 June 13</small>
                  </span>
                </a>
                <a href="#" class="media list-group-item">
                  <span class="media-body block m-b-none">
                    first v.1 (Bootstrap 2.3 based) released<br>
                    <small class="text-muted">19 June 13</small>
                  </span>
                </a>
              </div>
              <footer class="panel-footer text-small">
                <a href="#" class="pull-right"><i class="icon-cog"></i></a>
                <a href="#">See all the notifications</a>
              </footer>
            </section>
          </section>
        </div>
      </li>
      <li><div class="m-t-small"><a class="btn btn-small btn-info" data-toggle="modal" href="#modal"><i class="icon-plus"></i> POST</a></div></li>
      <li class="dropdown shift" data-toggle="shift:appendTo" data-target=".nav-primary .nav">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="icon-cog icon-xlarge visible-sm visible-sm-inline"></i>Settings <b class="caret hidden-sm-only"></b></a>
        <ul class="dropdown-menu">
          <li>
            <a href="#" data-toggle="class:navbar-fixed" data-target='body'>Navbar 
              <span class="text-active">auto</span>
              <span class="text">fixed</span>
            </a>
          </li>
          <li class="visible-lg">
            <a href="#" data-toggle="class:nav-vertical" data-target="#nav">Nav 
              <span class="text-active">vertical</span>
              <span class="text">horizontal</span>
            </a>
          </li>
          <li class="divider hidden-sm"></li>
          <li class="dropdown-header">Colors</li>
          <li>
            <a href="#" data-toggle="class:bg bg-black" data-target='.navbar'>Navbar 
              <span class="text-active">white</span>
              <span class="text">inverse</span>
            </a>
          </li>
          <li>
            <a href="#" data-toggle="class:bg-light" data-target='#nav'>Nav 
              <span class="text-active">inverse</span>
              <span class="text">light</span>
            </a>
          </li>
        </ul>
      </li>
    </ul>
    <form class="navbar-form pull-left shift" action="" data-toggle="shift:appendTo" data-target=".nav-primary">
      <i class="icon-search text-muted"></i>
      <input type="text" class="input-small form-control" placeholder="Search">
    </form>
  </header>
  <!-- / header -->
  <!-- nav -->
  <nav id="nav" class="nav-primary visible-lg nav-vertical">
    <ul class="nav" data-spy="affix" data-offset-top="50">
      <li><a href="index.html"><i class="icon-dashboard icon-xlarge"></i>Dashboard</a></li>
      <li class="dropdown-submenu">
        <a href="#"><i class="icon-th icon-xlarge"></i>Elements</a>
        <ul class="dropdown-menu">
          <li><a href="buttons.html">Buttons</a></li>
          <li><a href="icons.html"><b class="badge pull-right">302</b>Icons</a></li>            
          <li><a href="grid.html">Grid</a></li>
          <li><a href="widgets.html"><b class="badge bg-primary pull-right">8</b>Widgets</a></li>
          <li><a href="components.html"><b class="badge pull-right">18</b>Components</a></li>
        </ul>
      </li>
      <li class="dropdown-submenu">
        <a href="#"><i class="icon-list icon-xlarge"></i>Lists</a>
        <ul class="dropdown-menu">
          <li><a href="list.html">List groups</a></li>
          <li><a href="table.html">Table</a></li>
        </ul>
      </li>
      <li class="active"><a href="form.html"><i class="icon-edit icon-xlarge"></i>Form</a></li>
      <li><a href="chart.html"><i class="icon-signal icon-xlarge"></i>Chart</a></li>
      <li class="dropdown-submenu">
        <a href="#"><i class="icon-link icon-xlarge"></i>Others</a>
        <ul class="dropdown-menu">
          <li><a href="signin.html">Signin page</a></li>
          <li><a href="signup.html">Signup page</a></li>
          <li><a href="404.html">404 page</a></li>
        </ul>
      </li>
    </ul>
  </nav>
  <!-- / nav -->
  <section id="content">
    <section class="main padder">
      <div class="clearfix">
        <h4><i class="icon-edit"></i>Form</h4>
      </div>
      <div class="row">
        <div class="col-lg-12">
          <section class="panel">
            <form class="form-horizontal" method="get" data-validate="parsley">      
              <div class="form-group">
                <label class="col-lg-3 control-label">金额</label>
                <div class="col-lg-8">
                  <input type="text" name="money" placeholder="金额" data-required="true" class="form-control">
                </div>
              </div>			 <div class="form-group">                <label class="col-lg-3 control-label">码号</label>                <div class="col-lg-8">                  <input type="text" name="auth" placeholder="码号" data-required="true" class="form-control">                </div>              </div>
             
              <div class="form-group">
                <div class="col-lg-9 col-offset-3">                      

                  <button type="button" onclick="pay()" class="btn btn-primary">充值</button>
                </div>
              </div>
            </form>
          </section>
         
        </div>
        
      </div>
    </section>
  </section>
  <!-- footer -->
  <footer id="footer">
    <div class="text-center padder clearfix">
      <p>
        <small>&copy; first 2013, Mobile first web app framework base on Bootstrap</small><br><br>
        <a href="#" class="btn btn-mini btn-circle btn-twitter"><i class="icon-twitter"></i></a>
        <a href="#" class="btn btn-mini btn-circle btn-facebook"><i class="icon-facebook"></i></a>
        <a href="#" class="btn btn-mini btn-circle btn-gplus"><i class="icon-google-plus"></i></a>
      </p>
    </div>
  </footer>
  <!-- / footer -->
	<script src="js/jquery.min.js"></script>
  <!-- Bootstrap -->
  <script src="js/bootstrap.js"></script>
  <!-- app -->
  <script src="js/app.js"></script>
  <script src="js/app.plugin.js"></script>
  <script src="js/app.data.js"></script>
  <!-- fuelux -->
  <script src="js/fuelux/fuelux.js"></script>
  <!-- datepicker -->
  <script src="js/datepicker/bootstrap-datepicker.js"></script>
  <!-- slider -->
  <script src="js/slider/bootstrap-slider.js"></script>
  <!-- file input -->  
  <script src="js/file-input/bootstrap.file-input.js"></script>
  <!-- combodate -->
  <script src="js/combodate/moment.min.js"></script>
  <script src="js/combodate/combodate.js"></script>
  <!-- parsley -->
  <script src="js/parsley/parsley.min.js"></script>  <script type="text/javascript">  function pay(){  	$.ajax({  		url:"/sendone/dlb/pay",  		type:"POST",  		data:"auth="+$("input[name='auth']").val()+"&money="+$("input[name='money']").val(),  		success:function(msg){  			alert(msg);  		}  	});  }  </script>
</body>
</html>