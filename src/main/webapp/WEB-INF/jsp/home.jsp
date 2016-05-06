<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>

</head>

<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<div class="jumbotron">
			<h1>Arquivo LdoD</h1>
			<hr>
			<p>Arquivo Digital Colaborativo do Livro do Desassossego</p>
			<br> <br>
			<p class="text-right text-info">
				<strong>Vers�o BETA - Prot�tipo em desenvolvimento</strong><br>
				<span>Social Edition - LdoD</span> by <span>CLP / FLUC / UC
					and ESW / INESC-ID / IST</span><br> <span>Nenhum Problema Tem
					Solu��o Project</span><br> Licensed under a <a
					href="http://www.freebsd.org/copyright/freebsd-license.html">FreeBSD</a>
				License
			</p>
		</div>
		<div class="panel-footer">
			<div class="row">
				<div class="col-md-4">
					<img src="/resources/img/2015_FCT_V_color.jpg"
						class="img-responsive" alt="Responsive image">
				</div>
				<div  class="col-md-8">
					<img src="/resources/img/Fundo_Br_Logos_Cor.jpg"
						class="img-responsive" alt="Responsive image">
				</div>
			</div>
			<div class="row">
				<small>Arquivo digital desenvolvido no �mbito do projeto de
					investiga��o Nenhum Problema Tem Solu��o: Um Arquivo Digital do
					Livro do Desassossego (PTDC/CLE-LLI/118713/2010). Projeto
					financiado pela FCT e cofinanciado pelo FEDER, atrav�s do Eixo I do
					Programa Operacional Fatores de Competitividade (POFC) do QREN,
					COMPETE: FCOMP-01-0124-FEDER-019715. Financiado ainda por Fundos
					Nacionais atrav�s da FCT - Funda��o para a Ci�ncia e a Tecnologia
					no �mbito dos projetos Financiamento Plurianual - Unidade 759:
					"PEst-OE/ELT/UI0759/2011", "PEst-OE/ELT/UI0759/2014" e
					"UID/ELT/00759/2013". </smal>
			</div>
		</div>
	</div>
</body>
<script>
	$(document).ready(function() {
		$("a").tooltip();
	});
</script>
</html>