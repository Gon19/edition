<<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Erro Genérico do LdoD</title>
		<link rel="stylesheet" type="text/css" href="/static/css/bootstrap.css" />
		<link rel="stylesheet" type="text/css" href="/static/css/style.css" />
<style type="text/css">  
body {  
padding: 50px;  
}  
</style>  
</head>  
<body>  
		<div class="container">
			<div class="hero-unit">
				<h1>Ocorreu um erro:</h1>
				<br>
				<div class="alert alert-error">  
  					<a class="close" data-dismiss="alert">×</a>  
  					<strong>${exception.message} </strong> 
				</div> 
			</div>
		</div>

</body>  
</html>