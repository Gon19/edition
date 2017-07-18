<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>

<body class="ldod-default">
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<!--<div class="container">
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
				<div class="col-md-5">
					<img src="/resources/img/2015_FCT_V_color.jpg"
						class="img-responsive" alt="Responsive image">
				</div>
				<div class="col-md-7">
					<img src="/resources/img/Fundo_Br_Logos_Cor.jpg"
						class="img-responsive" alt="Responsive image">
				</div>
			</div>
			<div class="row">
				<small>Arquivo digital desenvolvido no �mbito do projeto de
					investiga��o 'Nenhum Problema Tem Solu��o: Um Arquivo Digital do
					Livro do Desassossego' (PTDC/CLE-LLI/118713/2010). Projeto pela
					financiado pela FCT - Funda��o para a Ci�ncia e a Tecnologia e
					cofinanciado pelo Fundo Europeu de Desenvolvimento Regional
					(FEDER), atrav�s do Eixo I do Programa Operacional Fatores de
					Competitividade (POFC) do QREN - Uni�o Europeia, COMPETE:
					FCOMP-01-0124-FEDER-019715. Financiado ainda por Fundos Nacionais
					atrav�s da FCT - Funda��o para a Ci�ncia e a Tecnologia no �mbito
					dos projetos "Financiamento Plurianual - Unidade 759":
					"PEst-OE/ELT/UI0759/2011", "UID/ELT/00759/2013" e
					"PEst-OE/ELT/UI0759/2014".</smal>
			</div>
		</div>
	</div>-->

	<% 
	  	int img1 = (int) (Math.random() * 2)+1;
	  	int img2 = (int) (Math.random() * 2)+1; 
	  	int img3 = (int) (Math.random() * 2)+1; 
	  	int img4 = (int) (Math.random() * 2)+1; 
	  	int img5 = (int) (Math.random() * 2)+1; 
	  	int img6 = (int) (Math.random() * 2)+1; 


	  	//{{"BNP/E3","E eu offere�o-te este livro porque sei que elle � bello e inutil.","9-39, 9-41, 9-31","Fr449/inter/Fr449_WIT_MS_Fr449a_000"},


String [][] excerpts =  {{"Jer�nimo Pizarro","E eu offere�o-te este livro porque sei que elle � bello e inutil.","17","Fr449/inter/Fr449_WIT_ED_CRIT_P"},
{"Jacinto do Prado Coelho","Senti-me agora respirar como se houvesse practicado uma cousa nova, ou atrazada.","188","Fr157/inter/Fr157_WIT_ED_CRIT_C"},
{"Teresa Sobral Cunha","Em mim foi sempre menor a intensidade das sensa��es que a intensidade da sensa��o delas.","283","Fr309/inter/Fr309_WIT_ED_CRIT_SC"},
{"Richard Zenith","O sil�ncio que sai do som da chuva espalha-se, num crescendo de monotonia cinzenta, pela rua estreita que fito.","41","Fr175/inter/Fr175_WIT_ED_CRIT_Z"},
{"Jer�nimo Pizarro","A grande terra, que serve os mortos, serviria, menos maternalmente, esses papeis.","387","Fr159.b/inter/Fr159_b_WIT_ED_CRIT_P_1"},
{"Teresa Sobral Cunha","Em cada pingo de chuva a minha vida falhada chora na natureza.","266","Fr390/inter/Fr390_WIT_ED_CRIT_SC"}, 
{"Jacinto do Prado Coelho","Como nos dias em que a trovoada se prepara e os ruidos da rua fallam alto com uma voz solit�ria.","45","Fr042/inter/Fr042_WIT_ED_CRIT_C"},
{"Teresa Sobral Cunha","Ningu�m estava quem era, e o patr�o Vasques apareceu � porta do gabinete para pensar em dizer qualquer coisa.","441","Fr043/inter/Fr043_WIT_ED_CRIT_SC"},
{"Richard Zenith","'Vem a� uma grande trovoada', disse o Moreira, e voltou a p�gina do Raz�o.","183","Fr044/inter/Fr044_WIT_ED_CRIT_Z"},
{"Jer�nimo Pizarro","E ent�o, em plena vida, � que o sonho tem grandes cinemas.","262","Fr149/inter/Fr149_WIT_ED_CRIT_P"},
{"Jer�nimo Pizarro","L�r � sonhar pela m�o de outrem.","586","Fr554/inter/Fr554_WIT_ED_CRIT_P"},
{"Jacinto do Prado Coelho","Devo ao ser guarda-livros grande parte do que posso sentir e pensar como a nega��o e a fuga do cargo.","133","Fr198/inter/Fr198_WIT_ED_CRIT_C"},
{"Teresa Sobral Cunha","Durmo sobre os cotovelos onde o corrim�o me doe, e sei de nada como um grande prometimento.","380","Fr030/inter/Fr030_WIT_ED_CRIT_SC"},
{"Richard Zenith","Sentado � janela, contemplo com os sentidos todos esta coisa nenhuma da vida universal que est� l� fora.","50","Fr118/inter/Fr118_WIT_ED_CRIT_Z"},
{"Jer�nimo Pizarro","J� me cansa a rua, mas n�o, n�o me cansa � tudo � rua na vida.","284","Fr523/inter/Fr523_WIT_ED_CRIT_P"},
{"Jacinto do Prado Coelho","Mergulhou na sombra como quem entra na porta onde chega.","485","Fr306a/inter/Fr306a_WIT_ED_CRIT_C"},
{"Jacinto do Prado Coelho","Para mim os pormenores s�o coisas, vozes, lettras.","163","Fr255/inter/Fr255_WIT_ED_CRIT_C"},
{"Teresa Sobral Cunha","Entre mim e a vida h� um vidro t�nue.","171","Fr447/inter/Fr447_WIT_ED_CRIT_SC"},
{"Richard Zenith","N�o toquemos na vida nem com as pontas dos dedos.","284","Fr452/inter/Fr452_WIT_ED_CRIT_Z"},
{"Jer�nimo Pizarro","N�o era isto, por�m, que eu queria dizer.","394","Fr264/inter/Fr264_WIT_ED_CRIT_P"},
{"Jacinto do Prado Coelho","Minha alma est� hoje triste at� ao corpo.","167","Fr269/inter/Fr269_WIT_ED_CRIT_C"},
{"Jacinto do Prado Coelho","Eu n�o sei quem tu �s, mas sei ao certo o que sou?","254","Fr285/inter/Fr285_WIT_ED_CRIT_C"},
{"Teresa Sobral Cunha","Pasmo sempre quando acabo qualquer coisa.","711","Fr009/inter/Fr009_WIT_ED_CRIT_SC"},
{"Richard Zenith","� uma oleografia sem rem�dio.","25","Fr010/inter/Fr010_WIT_ED_CRIT_Z"},
{"Jer�nimo Pizarro","Toda a vida � um somno.","197","Fr027/inter/Fr027_WIT_ED_CRIT_P"},
{"Richard Zenith","N�o consegui nunca ver-me de fora.","338","Fr028/inter/Fr028_WIT_ED_CRIT_Z"},
{"Jacinto do Prado Coelho","Jogar �s escondidas com a nossa consciencia de viver.","370","Fr437/inter/Fr437_WIT_ED_CRIT_C"},
{"Teresa Sobral Cunha","A arte livra-nos ilusoriamente da sordidez de sermos.","456","Fr163/inter/Fr163_WIT_ED_CRIT_SC"},
{"Richard Zenith","As coisas sonhadas s� t�m o lado de c�.","346","Fr510/inter/Fr510_WIT_ED_CRIT_Z"},
{"Jer�nimo Pizarro","Sou uma placa photographica prolixamente impressionavel.","59","Fr456/inter/Fr456_WIT_ED_CRIT_P"}};


		int excerptID = (int) (Math.random() * 30);

	  %>

	<div class="container ldod-default">

		<a href="/reading/fragment/<%=excerpts[excerptID][3]%>" class="frag-link">
		<div class="raw col-xs-12 frag-excerpt">
			<span class="frag-number font-egyptian"><%=excerpts[excerptID][2]%></span>
			<span class="frag-editor font-condensed"><%=excerpts[excerptID][0]%></span>
		</div>
		</a>

		<div class="frag-excerpt-text font-grotesque">
		<p><%=excerpts[excerptID][1]%></p>
		</div>

		<hr class="line-points">

		<div class="about font-monospace">

		<c:choose>
		<c:when test="${pageContext.response.locale.language.equalsIgnoreCase('pt')}">
			<p>
		      O Arquivo LdoD � um arquivo digital colaborativo do <span class="s-ws">Livro do Desassossego</span> de <span class="s-ws">Fernando Pessoa</span>.
		      Cont�m <span class="s-underl">imagens</span> dos documentos aut�grafos, <span class="s-underl">novas transcri��es</span>
		      desses documentos e ainda transcri��es de <span class="s-underl">quatro edi��es da obra</span>.
		      Al�m da <span class="s-underl">leitura</span> e <span class="s-underl">compara��o</span> das transcri��es, o Arquivo LdoD permite que os
		      utilizadores colaborem na cria��o de <span class="s-underl">edi��es virtuais</span> do Livro do Desassossego.
		    </p>
		</c:when>
			<c:when test="${pageContext.response.locale.language.equalsIgnoreCase('en')}">
			<p>
			The LdoD Archive is a collaborative digital archive of the Book of Disquiet by Fernando Pessoa. It contains images of the autograph documents, new transcriptions of those documents and also transcriptions of four editions of the work. In addition to reading and comparing transcriptions, the LdoD Archive enables users to collaborate in creating virtual editions of the Book of Disquiet.
			</p>
		</c:when>  
		<c:when test="${pageContext.response.locale.language.equalsIgnoreCase('es')}">
			<p>
			El Archivo LdoD es un archivo digital colaborativo del Libro del desasosiego de Fernando Pessoa. Contiene im�genes de los documentos originales, nuevas transcripciones de estos documentos y transcripciones de cuatro ediciones de la obra. Adem�s de la lectura y la comparaci�n de las transcripciones, el Archivo LdoD permite a los usuarios colaborar en la creaci�n de ediciones virtuales del Libro del desasosiego.
			</p>
		</c:when>   
	
		</c:choose>

	    
	  </div>

	  <hr class="line-x">


	   <!-- boxes -->

	   
	   <div class="menu-boxes hidden-xs col-xs-12">
	    <a href="/reading">
		  <div class="div-link">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-01-<%=img1%>.svg">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-01-<%=img1%>-h.svg">
		  </div>
	    </a>

	    <hr class="line-points">

	    <a href="/source/list">
		  <div class="div-link">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-02-<%=img2%>.svg">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-02-<%=img2%>-h.svg">
		  </div>
	    </a>

	    <hr class="line-points">

	     <a href="/edition">
		  <div class="div-link">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-03-<%=img3%>.svg">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-03-<%=img3%>-h.svg">
		  </div>
	    </a>

	    <hr class="line-points">
	     <a href="/search/simple">
		  <div class="div-link">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-04-<%=img4%>.svg">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-04-<%=img4%>-h.svg">
		  </div>
	    </a>

	    <hr class="line-points">

	     <a href="/virtualeditions">
		  <div class="div-link">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-05-<%=img5%>.svg">
		    <img src="/resources/img/boxes/D-${pageContext.response.locale.language}-05-<%=img5%>-h.svg">
		  </div>
	    </a>

	    <!--
	    <hr class="line-points">

	     <a href="">
		  <div class="div-link">
		    <img src="/resources/img/boxes/D-PT-06-<%=img6%>.svg">
		    <img src="/resources/img/boxes/D-PT-06-<%=img6%>-h.svg">
		  </div>
	    </a>
	    -->

	  </div>


	  <div class="menu-boxes visible-xs-inline col-xs-12">
	    <a href="/reading">
		  <div class="div-link">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-01-<%=img1%>.svg">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-01-<%=img1%>-h.svg">
		  </div>
	    </a>

	    <hr class="line-points">

	    <a href="/source/list">
		  <div class="div-link">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-02-<%=img2%>.svg">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-02-<%=img2%>-h.svg">
		  </div>
	    </a>

	    <hr class="line-points">

	     <a href="/edition">
		  <div class="div-link">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-03-<%=img3%>.svg">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-03-<%=img3%>-h.svg">
		  </div>
	    </a>

	    <hr class="line-points">
	     <a href="/search/simple">
		  <div class="div-link">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-04-<%=img4%>.svg">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-04-<%=img4%>-h.svg">
		  </div>
	    </a>

	    
	    <hr class="line-points">

	     <a href="/virtualeditions">
		  <div class="div-link">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-05-<%=img5%>.svg">
		    <img src="/resources/img/boxes/M-${pageContext.response.locale.language}-05-<%=img5%>-h.svg">
		  </div>
	    </a>

	    <!--
	    <hr class="line-points">

	     <a href="">
		  <div class="div-link">
		    <img src="/resources/img/boxes/D-PT-06-<%=img6%>.svg">
		    <img src="/resources/img/boxes/D-PT-06-<%=img6%>-h.svg">
		  </div>
	    </a>
	    -->

	  </div>

	  <!-- apoios info -->
    <div class="bottom-info font-monospace">
     <img class="hidden-xs" src="/resources/img/logotipos.png" width="100%">
      <img class="visible-xs-inline " src="/resources/img/logotiposm.png" width="100%">
     <br><br>
     <br>
      <p>

      	 <c:choose>
         <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('pt')}">
         O Arquivo LdoD foi desenvolvido no �mbito do projeto de investiga��o "Nenhum Problema Tem Solu��o: Um Arquivo Digital do Livro do Desassossego" (PTDC/CLE-LLI/118713/2010), do Centro de Literatura Portuguesa da Universidade de Coimbra. Projeto financiado pela Funda��o para a Ci�ncia e a Tecnologia (FCT), e cofinanciado pelo Fundo Europeu de Desenvolvimento Regional (FEDER), atrav�s do Eixo I do Programa Operacional Fatores de Competitividade (POFC) do QREN�Uni�o Europeia, COMPETE: FCOMP-01-0124-FEDER-019715. Financiado ainda por fundos nacionais, atrav�s da Funda��o para Ci�ncia e a Tecnologia (FCT), no �mbito dos projetos �Financiamento Plurianual�Unidade 759": "PEst-OE/ELT/00759/2013" e "PEst-OE/ELT/UI0759/2014".
         </c:when>
         <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('en')}">
         The LdoD Archive is an outcome of the research project "No Problem Has a Solution: A Digital Archive of the Book of Disquiet" (PTDC/CLE-LLI/118713/2010), developed by the Centre for Portuguese Literature at the University of Coimbra. The research project was funded by the Foundation for Science and Technology (FCT), and co-funded by the European Regional Development Fund (FEDER), through Axis 1 of the Operational Competitiveness Program (POFC) of the National Strategic Framework (QREN)�European Union, COMPETE: FCOMP-01-0124-FEDER-019715. Additional national funds by the Foundation for Science and Technology (FCT), under the "Plurianual Funding�Unit 759" projects: �PEst-OE/ELT/00759/2013" and "PEst-OE/ELT/UI0759/2014".
         </c:when>  
         <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('es')}">
         El Archivo LdoD es un resultado del proyecto de investigaci�n "Ning�n problema tiene soluci�n: un archivo digital del Libro del desasosiego" (PTDC/CLE-LLI/118713/2010), del Centro de Literatura Portuguesa de la Universidad de Coimbra. Proyecto financiado por la Fundaci�n para la Ciencia y la Tecnolog�a (FCT), y cofinanciado por el Fondo Europeo de Desarrollo Regional (FEDER), a trav�s del Eje I del Programa Operativo Factores de Competitividad (POFC) del QREN-Uni�n Europea, COMPET: FCOMP-01- 0124-FEDER-019715. Financiado tambi�n por fondos nacionales, a trav�s de la Fundaci�n para Ciencia y la Tecnolog�a (FCT), en el marco de los proyectos de "Financiaci�n Plurianual�Unidad 759": "PEst-OE/ELT/00759/2013" y "PEst-OE/ELT/UI0759/2014".
         </c:when>   
        </c:choose>


      </p>
    </div>
    <!-- END OF apoios -->

    

    <!--
		<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla blandit ac massa sit amet viverra. In dapibus nibh nec tempus suscipit. Mauris efficitur turpis quis neque blandit, consectetur placerat lorem fringilla. Suspendisse volutpat nunc luctus accumsan scelerisque. Aliquam sit amet posuere nunc. Nullam dapibus tristique sem, quis porta lacus. Nulla mauris nunc, lacinia sit amet condimentum a, dapibus eu sem. Sed malesuada dapibus hendrerit. In bibendum interdum enim sed suscipit. Vestibulum tristique lorem id mi iaculis, eget pellentesque nisi dignissim. Sed sit amet tortor vel risus efficitur pharetra. Proin mollis turpis eros, nec venenatis mauris pulvinar eget. Maecenas convallis at nunc ut feugiat.</p>

		<h1>Invejo - mas nao sei se invejo</h1>

		<p>Invejo . mas nao sei se invejo . aquelles de quem se pode escrever uma biografia, ou que podem escrever a propria. Nestas impressaes sem nexo, nem desejo de nexo, narro indifferentemente a minha autobiogra a sem factos, a minha historia sem vida. Sao as minhas Confissoes, e, se nellas nada digo, a que nada tenho que dizer.</p>

		<h1>Heading 1</h1>
		<h2>Heading 2</h2>
		<h3>Heading 3</h3>

		<p>Phasellus vel nibh quis dolor ullamcorper eleifend. Praesent mi odio, tincidunt nec efficitur eu, tincidunt id nunc. Nullam maximus nisi eros, nec tempor leo pulvinar at. Praesent euismod mauris eget elementum dapibus. Aenean id turpis nec tortor vulputate pellentesque. Nam fringilla, quam eu blandit gravida, lectus quam sodales lacus, nec hendrerit eros mauris sollicitudin justo. Nunc eget dui eu augue facilisis ultrices vitae ac quam. Donec sodales eros ut mattis tincidunt. Cras sed vulputate nunc. Pellentesque non ullamcorper magna. Nunc condimentum sem rhoncus ornare viverra. Phasellus tempus posuere nulla at pretium.</p>

		<h1>Heading 1</h1>
	
		<p>Ut semper consequat velit, vitae sollicitudin dolor bibendum sit amet. Suspendisse aliquam justo nunc. Morbi posuere fermentum est in congue. Nullam in maximus libero. Donec sit amet ligula fringilla, ullamcorper lacus in, placerat purus. Vivamus aliquet odio diam, in cursus lacus congue nec. In hac habitasse platea dictumst. Integer eget arcu eu eros consectetur lobortis. Nulla vel nulla ac erat molestie placerat placerat non enim.</p>

		<h2>Heading 2</h2>
	
		<p>Quisque enim ligula, porttitor eget est eu, elementum venenatis diam. Nam consequat in libero euismod scelerisque. Aenean placerat libero eleifend purus ultricies ullamcorper. Nunc rhoncus id tellus ut mattis. Nunc placerat tortor et lectus dictum scelerisque. Pellentesque id ultricies lectus. Maecenas aliquet ac tortor et porttitor. Phasellus aliquam magna laoreet rutrum laoreet. In aliquam nulla leo, et egestas mi dapibus nec. In tincidunt sollicitudin eros sed aliquet. In non justo in tellus eleifend tincidunt vitae vel ligula. Cras condimentum efficitur rhoncus. Nulla dapibus a nisi sit amet ullamcorper. Fusce laoreet erat eros, a commodo est sagittis a. Praesent auctor nulla ipsum, et porta sapien vestibulum quis.</p>

		<h3>Heading 3</h3>

		<p>Vestibulum non felis commodo, molestie lacus at, sagittis tortor. Nam feugiat, turpis eget cursus aliquam, nisl felis scelerisque odio, vitae dapibus metus sem ut justo. Fusce eu justo arcu. Etiam est erat, pulvinar at odio at, accumsan lacinia nisi. Quisque cursus quis massa sed eleifend. Sed luctus nulla in magna gravida placerat. Praesent sit amet dui id nunc fermentum suscipit a ac est. Sed felis libero, feugiat eget metus ac, dictum volutpat risus. Duis sagittis, nisi sed ullamcorper pretium, nisl diam iaculis mi, quis tempus mauris lorem a ex. Aliquam euismod vel tellus non bibendum. Duis et ultricies nisi.</p>
	-->
	</div>

	<div class="bottom-bar">
    </div>

</body>
</html>