<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.js"></script>
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
  	<div class="container">
    	<a class="brand" href="${contextPath}/">Arquivo do LdoD</a>
    	<div class="offset3">
    	<ul class="nav">
  			<li class="dropdown">
    			<a class="dropdown-toggle" data-toggle="dropdown" href="#">Acerca<b class="caret"></b></a>
    			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
      				<li><a href="#">Objectivos</a></li>
					<li><a href="#">Notas Editoriais</a></li>    			
					<li><a href="#">Financiado</a></li>    			
					<li><a href="#">Equipa Editorial</a></li>   
				</ul> 			
  			</li>
		</ul>
    	<ul class="nav">
  			<li class="dropdown">
    			<a class="dropdown-toggle" data-toggle="dropdown" href="#">Edições<b class="caret"></b></a>
    			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
      				<li><a href="${contextPath}/edition/JPC">Jacinto Prado Coelho</a></li>
					<li><a href="${contextPath}/edition/TSC">Teresa Sobral Cunha</a></li>    			
					<li><a href="${contextPath}/edition/RZ">Richard Zenith</a></li>    			
					<li><a href="${contextPath}/edition/JP">Jerónimo Pizarro</a></li>    			
					<li><a href="#">Edições Virtuals</a></li>   
				</ul> 			
  			</li>
		</ul>
    	<ul class="nav">
  			<li class="dropdown">
    			<a class="dropdown-toggle" data-toggle="dropdown" href="#">Pesquisa<b class="caret"></b></a>
    			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
      				<li><a href="${contextPath}/search/fragments">Fragmentos</a></li>
					<li><a href="#">Testemunhos Autorais</a></li>   
				</ul> 			
  			</li>
		</ul>
    	<ul class="nav">
  			<li class="dropdown">
    			<a class="dropdown-toggle" data-toggle="dropdown" href="#">Virtual<b class="caret"></b></a>
    			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
      				<li><a href="#">Login</a></li>
					<li><a href="#">Comunidades</a></li>
				</ul> 			
  			</li>
		</ul>
    	<ul class="nav">
  			<li class="dropdown">
    			<a class="dropdown-toggle" data-toggle="dropdown" href="#">Carregar<b class="caret"></b></a>
    			<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
      				<li><a href="${contextPath}/load/corpusForm">Corpus</a></li>
					<li><a href="${contextPath}/load/fragmentForm">Fragmento</a></li>   
				</ul> 			
  			</li>
		</ul>
    	</div>
  	</div>
  </div>
</div>
