<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>

</head>

<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<div class="col-md-8 col-md-offset-2 text-justify">
			<h1 class="text-center">
				<spring:message code="header.faq" />
			</h1>
			<h4>El Archivo</h4>
			<p>
				El <em>Archivo LdoD</em> est&aacute; constituido, por un lado, por
				un grupo de fragmentos que Fernando Pessoa expl&iacute;citamente
				indic&oacute; como pertenecientes al <em>Libro del desasosiego</em>,
				y, por otro lado, por otro grupo de fragmentos (sin indicaciones del
				escritor) que editores de la obra seleccionaron a partir de
				evidencias materiales o estil&iacute;sticas de los testimonios
				aut&oacute;grafos.
			</p>
			<p>
				Para cada fragmento el archivo proporciona el conjunto de
				interpretaciones asociadas a ese fragmento. Una
				interpretaci&oacute;n est&aacute; constituida por una
				transcripci&oacute;n del texto y un conjunto de
				metainformaci&oacute;n sobre el mismo, como por ejemplo la
				atribuci&oacute;n de una fecha, o de un heter&oacute;nimo, o la
				indicaci&oacute;n de la referencia de la Biblioteca Nacional de
				Portugal que identifica la fuente autoral en causa dentro del
				legado. Los diferentes editores del <em>Libro</em> produjeron
				interpretaciones que pueden diferir bien en la transcripci&oacute;n
				bien en la metainformaci&oacute;n. As&iacute;, en el <em>Archivo
					LdoD</em>, un fragmento posee interpretaciones de los editores
				(interpretaciones editoriales) y puede ocurrir que alguno de los
				editores no considere ese fragmento como perteneciente al <em>Libro</em>,
				con lo que no habr&aacute; interpretaci&oacute;n asociada a ese
				especialista. Adem&aacute;s, un fragmento posee una o m&aacute;s
				interpretaciones de las fuentes autorales (interpretaciones
				autorales) y puede haber varias interpretaciones autorales del mismo
				fragmento, debido, por ejemplo, a la existencia de varios
				manuscritos y datiloscritos (escritos a m&aacute;quina) de un mismo
				texto. La transcripci&oacute;n y metainformaci&oacute;n asociada a
				una interpretaci&oacute;n autoral son de responsabilidad del equipo
				del <em>Archivo LdoD</em>.
			</p>
			
			<p>
				<em>&iquest;Qu&eacute; es un fragmento en el </em>Archivo LdoD<em>?</em>
			</p>
			<p>
				Un fragmento es una secuencia textual modular que constituye la
				unidad b&aacute;sica de composici&oacute;n del <em>Libro</em>. La
				mayor parte de las secuencias textuales constituyen tambi&eacute;n
				unidades materiales delimitadas, en la medida en que estas
				est&aacute;n escritas en hojas sueltas (o peque&ntilde;os grupos de
				hojas) sin una ordenaci&oacute;n definida entre s&iacute;. Los
				fragmentos codificados en el <em>Archivo LdoD</em> corresponden a la
				suma de todos los textos que fueron considerados como pertenecientes
				o como asociados al <em>Libro del desasosiego</em> por las cuatro
				ediciones cr&iacute;ticas, incluyendo aquellos textos que son
				publicados en ap&eacute;ndice o anexo.
			</p>
			
			<p>
				<em>&iquest;Qu&eacute; es una interpretaci&oacute;n en el </em>Archivo
				LdoD<em>?</em>
			</p>
			<p>
				Una interpretaci&oacute;n es el conjunto formado por una
				transcripci&oacute;n y por la metainformaci&oacute;n asociada. Las
				interpretaciones de las cinco versiones (esto es, de las cuatro
				ediciones de los especialistas y tambi&eacute;n de la
				representaci&oacute;n gen&eacute;tica de las fuentes autorales) no
				son totalmente coincidentes entre s&iacute;, bien sea por
				variaciones y diferencias microtextuales (transcripci&oacute;n de
				frases, ordenaci&oacute;n interna de los par&aacute;grafos,
				divisi&oacute;n de los fragmentos entre s&iacute;, por ejemplo),
				bien por variaciones macrotextuales (ordenaci&oacute;n relativa del
				fragmento en la arquitectura del <em>Libro</em>, inclusi&oacute;n o
				exclusi&oacute;n de un determinado fragmento del corpus del <em>Libro</em>).
			</p>
			
			<p>
				<em>&iquest;Por qu&eacute; algunos fragmentos poseen m&aacute;s
					de una interpretaci&oacute;n editorial para el mismo editor?</em>
			</p>
			<p>Esta situaci&oacute;n se da porque los editores dividen, a
				veces, un fragmento o asocian varios fragmentos. Por ejemplo, un
				editor puede considerar como un &uacute;nico fragmento lo que otro
				editor considera dos fragmentos distintos, o sea, para el primer
				editor el archivo asocia una &uacute;nica interpretaci&oacute;n
				mientras que para el segundo asocia dos.</p>
			
			<p>
				<em>&iquest;Qu&eacute; es la interpretaci&oacute;n autoral
					representativa de un fragmento?</em>
			</p>
			<p>Es la interpretaci&oacute;n autoral de la fuente impresa, en
				caso que exista; de una fuente datiloscrita o manuscrita, en el caso
				de faltar la primera. Es necesario definir una interpretaci&oacute;n
				autoral representativa para tener una transcripci&oacute;n de
				referencia, por ejemplo, para calcular la similitud textual entre
				dos fragmentos cuando se usa el algoritmo de recomendaci&oacute;n.</p>
			
			<p>Fuentes Autorales</p>
			
			<p>
				<em>&iquest;Qu&eacute; son las fuentes autorales?</em>
			</p>
			<p>
				Las fuentes autorales son las interpretaciones de los fragmentos
				constituidas por transcripciones gen&eacute;ticas de manuscritos y
				dactiloscritos aut&oacute;grafos, y tambi&eacute;n textos publicados
				por Pessoa, codificados por el equipo del <em>Archivo LdoD</em>.
				Adem&aacute;s de la transcripci&oacute;n, hay un conjunto de
				metainformaci&oacute;n sobre los testimonios documentales (soporte y
				medio de la inscripci&oacute;n, dimensiones de la hoja, eventuales
				particularidades del documento, referencia de la BNP, etc.).
			</p>
			
			<p>
				<em>&iquest;Qu&eacute; es una transcripci&oacute;n
					gen&eacute;tica?</em>
			</p>
			<p>Es una transcripci&oacute;n que permite observar el proceso de
				escritura y revisi&oacute;n autoral, registrando todas las capas de
				composici&oacute;n del texto y no apenas la versi&oacute;n final.</p>
			
			<p>
				<em>&iquest;Qu&eacute; permite la interfaz de
					visualizaci&oacute;n de las fuentes autorales?</em>
			</p>
			<p>Permite ver la transcripci&oacute;n de un testimonio autoral
				que respeta la topograf&iacute;a de los saltos de l&iacute;nea y de
				distribuci&oacute;n de espacios del documento original. Permite ver
				tambi&eacute;n los elementos textuales eliminados, substituidos y
				adicionados por Fernando Pessoa, as&iacute; como el facs&iacute;mil
				digital de los documentos originales. Son representadas
				tambi&eacute;n todas las variantes, esto es, aquellas partes del
				texto para las que existen dos o m&aacute;s alternativas.</p>
			
			<p>
				<em>&iquest;Qu&eacute; es un facs&iacute;mil? </em>
			</p>
			<p>
				El facs&iacute;mil es una imagen digital del documento original, que
				es representado a trav&eacute;s de la unidad de la p&aacute;gina
				(con excepci&oacute;n de las fuentes publicadas, en las que es usada
				una doble p&aacute;gina). Son incluidas im&aacute;genes de todas las
				caras escritas (frente y verso) de documentos cuyo texto fue
				atribuido al <em>Libro</em>. Hay, por eso, hojas representadas
				apenas en su frente y otras en frente y verso.
			</p>
			
			<p>Ediciones de los especialistas</p>
			
			<p>
				<em>&iquest;Qu&eacute; son las ediciones de los especialistas?</em>
			</p>
			<p>
				Las ediciones de los especialistas que constan en el <em>Archivo
					LdoD</em> son las principales ediciones cr&iacute;ticas publicadas entre
				1982 y 2012. Estas cuatro ediciones cr&iacute;ticas (preparadas a
				partir de los testimonios aut&oacute;grafos) fueron organizadas,
				respectivamente por Jacinto do Prado Coelho (1982, reimp. 1997),
				Teresa Sobral Cunha (2008; 1&ordf; ed. 1990-1991), Richard Zenith
				(2012; 1&ordf; ed. 1998) y Jer&oacute;nimo Pizarro (2010). La
				secuencia de presentaci&oacute;n de las ediciones de los
				especialistas en el men&uacute; corresponde al orden
				cronol&oacute;gico de la fecha de publicaci&oacute;n original. Las
				ediciones de los especialistas son, por lo tanto, muy significativas
				bien desde un punto de vista filol&oacute;gico, bien desde el punto
				de vista de la socializaci&oacute;n de la obra. Aunque la escala del
				respectivo aparato cr&iacute;tico sea muy variable, los cuatro
				editores trabajaron a partir de los originales, con el objetivo de
				producir una versi&oacute;n legible e integral del <em>Libro</em>.
				Adem&aacute;s, estas ediciones determinaron la diseminaci&oacute;n
				del <em>Libro del desasosiego</em> desde 1982, bien en
				portugu&eacute;s, bien en otras lenguas, en la medida en que fueron
				tomadas como base para otras ediciones portuguesas y para
				in&uacute;meras traducciones. Con excepci&oacute;n de la
				edici&oacute;n de Jacinto do Prado Coelho (1982-1997), que fue
				reimpresa una &uacute;nica vez sin alteraciones despu&eacute;s de la
				muerte del editor, todas las restantes son ediciones que fueron
				alteradas y corregidas en cada nueva publicaci&oacute;n. La
				edici&oacute;n de Jacinto do Prado Coelho es hist&oacute;ricamente
				significativa por haber sido la primera, y las restantes son
				hist&oacute;ricamente significativas por haber continuado
				demostrando materialmente la posibilidad de reedici&oacute;n y
				reorganizaci&oacute;n del <em>Libro</em>. La edici&oacute;n de
				Jer&oacute;nimo Pizarro es, tambi&eacute;n, significativa por haber
				ordenado los textos por la fecha de composici&oacute;n y por haber
				desarrollado un aparato cr&iacute;tico gen&eacute;tico
				sistem&aacute;tico y exhaustivo.
			</p>
			
			<p>
				<em>&iquest;Por qu&eacute; se escogieron estas ediciones
					espec&iacute;ficas de los especialistas?</em>
			</p>
			<p>
				Porque estas cuatro ediciones (Coelho 1982; Sobral Cunha 2008;
				Zenith 2012 y Pizarro 2010) fueron las &uacute;ltimas ediciones en
				el momento en que la codificaci&oacute;n de los ficheros del <em>Archivo
					LdoD</em> se inici&oacute;, en el 2012. La edici&oacute;n de 1982
				(reimpresa una &uacute;nica vez, sin alteraciones, en 1997) es la
				&uacute;nica que no sufri&oacute; modificaciones. Las otras tres
				ediciones contin&uacute;an sufriendo cambios (en el corpus, en la
				transcripci&oacute;n, en la ordenaci&oacute;n), aunque mantienen lo
				esencial de las respectivas estructuras y principios de
				ordenaci&oacute;n. Entre tanto, desde 2012, fueron publicadas nuevas
				ediciones (Cunha 2013 y 2016; Pizarro 2013 y 2014; Zenith 2015).
			</p>
			
			<p>
				<em>&iquest;Las ediciones de los especialistas ser&aacute;n
					actualizadas en el futuro?</em>
			</p>
			<p>
				Aunque los especialistas hayan manifestado el deseo de ver la
				&uacute;ltima versi&oacute;n de las respectivas ediciones
				representada en el <em>Archivo LdoD</em>, una futura
				actualizaci&oacute;n depender&aacute; de los recursos disponibles
				para recodificar los archivos para que puedan reflejar las
				alteraciones introducidas en una nueva edici&oacute;n determinada.
				Para garantizar el rigor filol&oacute;gico del trabajo realizado, es
				necesario que las interpretaciones contenidas en el <em>Archivo
					LdoD</em> para representar las ediciones de los especialistas
				correspondan a un testimonio espec&iacute;fico (en este caso, a las
				ediciones Coelho-1982, Sobral Cunha-2008, Zentih-2012 y
				Pizarro-2010) y no a una combinaci&oacute;n ecl&eacute;ctica de
				lecturas escogidas en diferentes ediciones de texto.
			</p>
			
			<p>Codificaci&oacute;n XML-TEI</p>
			
			<p>
				<em>&iquest;C&oacute;mo se realiz&oacute; la
					codificaci&oacute;n de los textos del Archivo?</em>
			</p>
			<p>
				La codificaci&oacute;n fue realizada de acuerdo con las
				recomendaciones de la Text Encoding Initiative (TEI) en la
				versi&oacute;n P5 de la norma. Un ejemplo de matriz XML de
				codificaci&oacute;n puede ser consultado <strong>AQU&Iacute;</strong>.
				El trabajo fue realizado a lo largo de cuatro a&ntilde;os, por un
				equipo de cinco personas, en un total de doce mil horas de
				codificaci&oacute;n y revisi&oacute;n.
			</p>
			
			<p>
				<em>&iquest;Qu&eacute; elementos fueron codificados?</em>
			</p>
			<p>
				La codificaci&oacute;n XML de las fuentes autorales y de las cuatro
				ediciones de los especialistas contenidas en el <em>Archivo
					LdoD</em> fue hecha de forma extremamente granular, de modo que todas
				las variaciones pudiesen ser comparables, bien (a) en la escala
				microtextual interna a cada fragmento, bien (b) en la escala
				macrotextual de organizaci&oacute;n externa de los fragmentos en el
				<em>Libro</em>, tambi&eacute;n todav&iacute;a (c) a nivel de la
				metainformaci&oacute;n sobre cada &iacute;tem del archivo. Los
				elementos formalmente representados abarcan (a) puntuaci&oacute;n,
				ortograf&iacute;a, palabras, divisi&oacute;n/agrupaci&oacute;n de
				par&aacute;grafos y espacios entre par&aacute;grafos, pero
				tambi&eacute;n (b) divisi&oacute;n/agrupaci&oacute;n y
				ordenaci&oacute;n de fragmentos en la estructura del <em>Libro</em>,
				as&iacute; como (c) metainformaci&oacute;n relativa a la
				atribuci&oacute;n editorial de fecha y heter&oacute;nimo o relativa
				a la naturaleza material de las fuentes autorales. En el caso de las
				fuentes autorales, fueron tambi&eacute;n codificados todos los actos
				de revisi&oacute;n autoral (eliminaciones, adiciones,
				substituciones, variantes), as&iacute; como la topograf&iacute;a de
				los saltos de l&iacute;nea y del uso de espacios o trazos de
				divisi&oacute;n entre bloques de texto en todos los documentos. En
				el caso de las ediciones de los especialistas fueron codificados
				tambi&eacute;n todos los espacios relativos y trazos de
				divisi&oacute;n entre par&aacute;grafos.
			</p>
			
			<p>Comparaci&oacute;n de ediciones</p>
			
			<p>
				<em>&iquest;Para qu&eacute; sirve la interfaz de
					comparaci&oacute;n de las ediciones?</em>
			</p>
			<p>
				La interfaz de comparaci&oacute;n de las ediciones sirve para
				relacionar entre s&iacute; las varias interpretaciones de un mismo
				fragmento, esto es, las interpretaciones de las fuentes autorales y
				las interpretaciones de las ediciones de los especialistas. La
				visualizaci&oacute;n paralela de los fragmentos permite comparar
				entre s&iacute; las transcripciones de cada fragmento, y
				tambi&eacute;n su posici&oacute;n relativa dentro de la
				ordenaci&oacute;n del <em>Libro</em>.
			</p>
			
			<p>
				<em>&iquest;Qu&eacute; muestra la visualizaci&oacute;n de
					realce de diferencias?</em>
			</p>
			<p>Esta funci&oacute;n muestra los puntos de variaci&oacute;n
				entre las interpretaciones que se comparan. Estos puntos de
				variaci&oacute;n son resaltados en azul en el cuerpo del texto, y
				son presentados bajo la forma de tabla, a continuaci&oacute;n del
				texto, en la parte inferior de la p&aacute;gina. Las variaciones en
				la tonalidad azul resaltada se&ntilde;alan la cantidad relativa de
				variaci&oacute;n (de tal modo que la existencia de variaci&oacute;n
				en una &uacute;nica interpretaci&oacute;n resulta en un tono azul
				m&aacute;s claro y la existencia de variaciones en todas las
				interpretaciones resulta en un tono azul m&aacute;s oscuro,
				existiendo todav&iacute;a dos tonalidades intermedias).</p>
			
			<p>
				<em>&iquest;Cu&aacute;les son las unidades de realce de
					diferencias?</em>
			</p>
			<p>Las unidades de realce de diferencias pueden ser de cuatro
				tipos: (a) palabra o grupo de palabras, (b) signo de
				puntuaci&oacute;n, (c) frase o grupo de frases y (d) parte o todo el
				fragmento. El realce de palabra o grupo de palabras indica
				variaciones ortogr&aacute;ficas o variaciones substantivas en las
				transcripciones en comparaci&oacute;n. Por ejemplo, las ediciones
				Prado Coelho-1982 y Pizarro-2010 siguen la ortograf&iacute;a del
				documento original, mientras que las ediciones Cunha-2008 y
				Zenith-2012 modernizan la ortograf&iacute;a a la norma en vigor en
				la fecha de publicaci&oacute;n. Estos casos son se&ntilde;alados en
				la tabla de variaciones a trav&eacute;s de la tipolog&iacute;a
				&ldquo;orthographic&rdquo; (esto es, variaci&oacute;n
				ortogr&aacute;fica). Los casos de variaci&oacute;n substantiva en la
				transcripci&oacute;n (por ejemplo, cuando los peritos optan por
				variantes distintas o leen de forma diferente determinado pasaje)
				son se&ntilde;alados en la tabla de variaciones por la
				tipolog&iacute;a &ldquo;substantive&rdquo; (esto es,
				variaci&oacute;n substantiva). El realce del signo de
				puntuaci&oacute;n indica que las transcripciones en
				comparaci&oacute;n no coinciden en la puntuaci&oacute;n de
				determinado pasaje. Estos casos son se&ntilde;alados en la tabla de
				variaciones por la tipolog&iacute;a &ldquo;punctuation&rdquo; (esto
				es, variaci&oacute;n de puntuaci&oacute;n). El realce de frase o
				grupo de frases indica que una o m&aacute;s transcripciones dividen
				los par&aacute;grafos de forma diferente. Estos casos son
				se&ntilde;alados en la tabla de variaciones a trav&eacute;s de la
				tipolog&iacute;a &ldquo;paragraph&rdquo; (esto es, variaci&oacute;n
				en la divisi&oacute;n de par&aacute;grafos). El realce de parte o de
				todo el fragmento indica que las ediciones dividen los fragmentos
				entre s&iacute; de forma diferente, o sea, la correspondencia no es
				de 1 a 1, si no de 1 a 2 o a m&aacute;s de 2. En estos casos la
				tabla de variaciones indica s&oacute;lo las diferencias
				macrotextuales, por lo que es necesario ver los realces en el propio
				texto para identificar los otros puntos de variaci&oacute;n a la
				escala inferior de la palabra o grupo de palabras, signo de
				puntuaci&oacute;n y frase o grupo de frases.</p>
			
			<p>
				<em>&iquest;Qu&eacute; revela la comparaci&oacute;n sobre las
					ediciones?</em>
			</p>
			<p>
				La comparaci&oacute;n permite conocer mejor cada una de las
				ediciones y hace expl&iacute;citos los criterios editoriales que no
				siempre son declarados en los principios editoriales generales, bien
				en lo que respecta a las transcripciones individuales, bien en lo
				que respecta a la ordenaci&oacute;n global de los fragmentos. La
				comparaci&oacute;n autom&aacute;tica permite, por ejemplo, dar
				cuenta de las diversas opciones e intervenciones editoriales en la
				lectura de pasajes dif&iacute;ciles de descifrar, de la
				elecci&oacute;n de variantes, de la puntuaci&oacute;n de los textos,
				de la divisi&oacute;n/agrupaci&oacute;n de par&aacute;grafos de los
				fragmentos. La comparaci&oacute;n (no s&oacute;lo de estas ediciones
				entre s&iacute;, sino de la historia de cada una de ellas en
				comparaci&oacute;n con las restantes) revela, a&uacute;n, que a
				medida que el tiempo pasa, las lecturas de pasajes dif&iacute;ciles
				de leer (y para los que se ofrecen inicialmente lecturas
				conjeturales) van presentando, de edici&oacute;n en edici&oacute;n,
				menos variaci&oacute;n entre s&iacute;, en la medida en que nuevos
				textos descifrados, con un grado mayor de probabilidad de certeza,
				son incorporados en las ediciones subsecuentes, tras mostrar que los
				especialistas cooperan en el esfuerzo para leer la
				caligraf&iacute;a, a veces, idiosincr&aacute;tica y variable de
				Fernando Pessoa. Aunque en la escala granular del descifrado de los
				caracteres y de las palabras, a medida que el tiempo pasa, haya
				menos variaci&oacute;n entre ediciones, la comparaci&oacute;n revela
				a&uacute;n que los editores que contin&uacute;an reeditando el texto
				(Sobral Cunha, Zenith y Pizarro) son coerentes con la
				macroorganizaci&oacute;n original de los fragmentos de sus
				respectivas ediciones y que se mantienen fieles a la estructura y al
				modelo que concibieron inicialmente. Con la excepci&oacute;n de
				peque&ntilde;as alteraciones, esa ordenaci&oacute;n de los
				fragmentos en el <em>Libro</em> permanece estable de edici&oacute;n
				en edici&oacute;n. A la escala del libro, la comparaci&oacute;n
				entre las ediciones de los especialistas permite comprobar la
				existencia de cuatro modelos distintos de producci&oacute;n en el <em>Libro
					del desasosiego</em>.
			</p>
			
			<p>
				<em>&iquest;Es posible comparar una interpretaci&oacute;n
					editorial o autoral con una interpretaci&oacute;n virtual?</em>
			</p>
			<p>No, dado que una interpretaci&oacute;n virtual nunca altera la
				transcripci&oacute;n de la interpretaci&oacute;n que utiliza.</p>
			
			<p>
				<em>&iquest;Qu&eacute; muestra la comparaci&oacute;n de las
					anotaciones de fragmentos de las ediciones virtuales?</em>
			</p>
			<p>En esa interfaz es posible comparar las anotaciones que los
				editores virtuales aplicaron sobre las interpretaciones virtuales.
				Por ejemplo, lo que fue citado, y qu&eacute; notas y
				categor&iacute;as fueron asociadas a la cita por los editores
				virtuales. N&oacute;tese que la comparaci&oacute;n es entre
				interpretaciones del mismo fragmento, incluso cuando la
				interpretaci&oacute;n de base utilizada no es la misma.</p>
			
			<p>B&uacute;squeda</p>
			
			<p>La b&uacute;squeda permite seleccionar fragmentos de acuerdo a
				un conjunto de criterios.</p>
			
			<p>
				<em>&iquest;Qu&eacute; es una b&uacute;squeda simple?</em>
			</p>
			<p>En la b&uacute;squeda simple es posible seleccionar el
				conjunto de fragmentos de acuerdo con dos criterios: texto y tipo de
				fuente. El criterio de texto se puede aplicar a toda
				transcripci&oacute;n o apenas al t&iacute;tulo. El resultado es el
				conjunto de interpretaciones que posee el texto inserido en el campo
				de b&uacute;squeda. N&oacute;tese que el resultado de la
				b&uacute;squeda puede incluir varias interpretaciones asociadas al
				mismo fragmento (esto es, interpretaciones autorales e
				interpretaciones editoriales).</p>
			
			<p>
				<em>&iquest;Cu&aacute;les son los caracteres permitidos en el
					criterio de b&uacute;squeda por texto?</em>
			</p>
			<p>S&oacute;lo son permitidos los caracteres
				alfanum&eacute;ricos, el gui&oacute;n, el &ldquo;&amp;&rdquo;
				comercial y el espacio. Los restantes caracteres son ignorados, por
				ejemplo al inserir &ldquo;al*ma&rdquo; en el campo de
				b&uacute;squeda el resultado incluir&aacute; todas las
				interpretaciones en donde la palabra &ldquo;alma&rdquo; surja.
				N&oacute;tese que no hay distinci&oacute;n entre min&uacute;scula y
				may&uacute;scula, por lo que en el ejemplo anterior tambi&eacute;n
				se obtendr&aacute;, en el resultado, las interpretaciones en donde
				surja la palabra &ldquo;Alma&rdquo;.</p>
			
			<p>
				<em>&iquest;Qu&eacute; es una b&uacute;squeda avanzada?</em>
			</p>
			<p>En la b&uacute;squeda avanzada es posible usar un mayor
				conjunto de criterios. Adem&aacute;s, es posible formular una
				b&uacute;squeda que componga los criterios de dos formas: de acuerdo
				con todos los criterios o de acuerdo con apenas uno de los
				criterios. En el caso de la b&uacute;squeda ser de acuerdo con todos
				los criterios, el resultado contendr&aacute; los fragmentos que para
				cada uno de los criterios definidos posean por lo menos una
				interpretaci&oacute;n que satisfaga ese criterio. Por ejemplo, en
				una b&uacute;squeda en la que en el campo de criterio texto se
				coloca &ldquo;alma liberdade&rdquo; y en el campo de criterio
				heter&oacute;nimo se escoge &ldquo;Vicente Guedes&rdquo;, van a ser
				seleccionados todos los fragmentos que posean por lo menos una
				interpretaci&oacute;n que en su texto incluya las dos palabras y que
				tenga por lo menos una interpretaci&oacute;n que atribuya el
				fragmento a Vicente Guedes. N&oacute;tese que no es necesario que
				sea la misma interpretaci&oacute;n la que satisfaga ambos criterios,
				basta que sean las dos del mismo fragmento. En el caso en que la
				b&uacute;squeda est&eacute; de acuerdo con, como m&iacute;nimo, uno
				de los criterios, el resultado contendr&aacute; todos los fragmentos
				que posean una interpretaci&oacute;n que satisfaga por lo menos uno
				de los criterios. Usando el ejemplo anterior, el resultado
				incluir&aacute; los fragmentos que posean por lo menos una
				interpretaci&oacute;n atribuida a Vicente Guedes o que posean las
				palabras &ldquo;alma&rdquo; y &ldquo;liberdade&rdquo;. En ambos
				casos, el resultado de la b&uacute;squeda incluye las
				interpretaciones de los fragmentos seleccionados, al indicar
				cu&aacute;les son los criterios que cada una de las interpretaciones
				satisface y as&iacute; justificando el motivo por el que el
				fragmento fue seleccionado.</p>
			
			<p>Lectura</p>
			
			<p>La interfaz de lectura ofrece la posibilidad de leer los
				fragmentos del Libro del desasosiego, de acuerdo con las secuencias
				propuestas por cada uno de los editores. Adem&aacute;s, el archivo
				recomienda una secuencia de lectura en base a pesos (entre 0.0 y
				1.0, con intervalos de 0.2), que el usuario puede atribuir a cuatro
				criterios: heter&oacute;nimo, fecha, texto y taxonom&iacute;a. Para
				el fragmento que el usuario est&aacute; leyendo el sistema
				recomienda lo que considera m&aacute;s pr&oacute;ximo de acuerdo con
				una medida de distancia entre fragmentos.</p>
			
			<p>
				<em>&iquest;C&oacute;mo es calculada la distancia entre dos
					fragmentos?</em>
			</p>
			<p>
				Es calculada aplicando la <a
					href="https://es.wikipedia.org/wiki/Similitud_coseno">similitud
					coseno</a> a los vectores construidos para cada uno de los cuatro
				criterios. Cuando se comparan dos fragmentos, se calcula su
				similitud usando los vectores de los criterios para los cuales el
				usuario atribuy&oacute; un peso diferente a cero. Los vectores de
				criterio son construidos utilizando la informaci&oacute;n asociada a
				todas las interpretaciones del fragmento y multiplicados por el peso
				atribuido por el usuario a ese criterio, un valor entre 0.2 y 1.0.
			</p>
			
			<p>
				<em>&iquest;C&oacute;mo se calcula el vector asociado al
					heter&oacute;nimo?</em>
			</p>
			<p>El vector de heter&oacute;nimo posee dos c&eacute;lulas, una
				para cada uno de los heter&oacute;nimos, Vicente Guedes y Bernardo
				Soares. Una c&eacute;lula posee el valor 1.0 si por lo menos una de
				las interpretaciones atribuye la heteronimia del fragmento al
				heter&oacute;nimo asociado a esa c&eacute;lula, y 0.0 en caso
				contrario. Por ejemplo, si un fragmento es atribuido a Vicente
				Guedes por uno de los editores y a Bernardo Soares por dos de los
				otros editores, el vector de heter&oacute;nimo tendr&aacute; 1.0 en
				ambas c&eacute;lulas.</p>
			
			<p>
				<em>&iquest;C&oacute;mo es calculado el vector asociado a la
					fecha?</em>
			</p>
			<p>
				El vector de fecha tiene 22 c&eacute;lulas, una por cada uno de los
				a&ntilde;os que median entre el a&ntilde;o del primer fragmento
				atribuido al <em>Libro</em>, 1913, y el a&ntilde;o del &uacute;ltimo
				fragmento atribuido, 1934. Si, por ejemplo, dos interpretaciones
				atribuyen fechas diferentes a un fragmento, las respectivas
				c&eacute;lulas tendr&aacute;n el valor 1.0 y las restantes
				ser&aacute;n completadas con valores menores a 1.0 con un
				decaimiento de MAX (0.0, 1.0 &ndash; N x 0.1) en que N es la
				distancia entre el a&ntilde;o asociado a la c&eacute;lula y la
				c&eacute;lula m&aacute;s pr&oacute;xima con valor 1.0.
			</p>
			
			<p>
				<em>&iquest;C&oacute;mo es calculado el vector asociado al
					texto?</em>
			</p>
			<p>
				Inicialmente es calculado el conjunto de los 100 primeros <a
					href="https://es.wikipedia.org/wiki/Tf-idf">tf-idf</a> (term
				frequency-inverse document frequency) de los t&eacute;rminos de cada
				uno de los dos fragmentos que se comparan. N&oacute;tese que este
				conjunto tiene N t&eacute;rminos que pueden ser menores a 200, 100
				t&eacute;rminos para cada uno de los fragmentos, en el caso de haber
				t&eacute;rminos iguales en ambos fragmentos.&nbsp; Despu&eacute;s es
				creado un vector, con un n&uacute;mero de c&eacute;lulas igual a N,
				la cardinal del conjunto, en que la c&eacute;lula asociada a un
				t&eacute;rmino posee el valor 1.0 si surge en el conjunto de los
				primeros N t&eacute;rminos td-idf de ese fragmento, y 0.0 en caso
				contrario. Dado que las palabras existentes en un fragmento
				var&iacute;an conforme a la transcripci&oacute;n, se utiliza la
				transcripci&oacute;n asociada a la interpretaci&oacute;n autoral
				representativa.
			</p>
			
			<p>
				<em>&iquest;C&oacute;mo es calculado el vector asociado a la
					taxonom&iacute;a?</em>
			</p>
			<p>
				El vector tiene un n&uacute;mero de c&eacute;lulas igual al
				n&uacute;mero de categor&iacute;as existentes en la taxonom&iacute;a
				de la <em>Edici&oacute;n del Archivo LdoD</em>. La c&eacute;lula
				asociada a una categor&iacute;a posee el valor 1.0 si la
				interpretaci&oacute;n del fragmento en la <em>Edici&oacute;n
					del Archivo LdoD</em> atribuye esa categor&iacute;a a alguna etiqueta, y
				tiene el valor 0.0 en caso contrario.
			</p>
			
			<p>
				<em>&iquest;Qu&eacute; significa la recomendaci&oacute;n de
					lectura cuando todos los pesos de los criterios est&aacute;n a
					cero?</em>
			</p>
			<p>Quiere decir que no existe similitud entre los fragmentos, el
				valor de la similitud entre cualquiera de los fragmentos es 0.0.</p>
			
			<p>
				<em>La similitud es calculada entre fragmentos, pero lo que
					recomienda el archivo para lectura es una interpretaci&oacute;n y
					no un fragmento, &iquest;por qu&eacute;?</em>
			</p>
			<p>La similitud es calculada entre los fragmentos, pero la
				lectura debe ser efectuada sobre la transcripci&oacute;n de una de
				las interpretaciones. As&iacute;, es sugerida para la lectura la
				transcripci&oacute;n asociada a la edici&oacute;n de la
				transcripci&oacute;n que el usuario lee.</p>
			
			<p>
				<em>&iquest;Los fragmentos que fueron recomendados pueden
					volver a ser recomendados?</em>
			</p>
			<p>S&oacute;lo son recomendados para la lectura los fragmentos
				que a&uacute;n no fueron le&iacute;dos desde que el usuario
				inici&oacute; una secuencia de lectura, pero si apenas restan 50
				fragmentos por leer, el archivo vuelve a considerar en su
				recomendaci&oacute;n los primeros 50 fragmentos le&iacute;dos.</p>
			
			<p>
				<em>&iquest;Puede surgir m&aacute;s de un fragmento
					recomendado?</em>
			</p>
			<p>S&iacute;, se sugiere el fragmento m&aacute;s semejante al que
				se lee m&aacute;s todos los fragmentos cuya diferencia entre el
				valor de su similitud y el valor de similitud del sugerido es menor
				que 0.001, hasta un m&aacute;ximo de 5 fragmentos. N&oacute;tese que
				el fragmento sugerido es el m&aacute;s similar que tiene una
				interpretaci&oacute;n en la edici&oacute;n de la
				transcripci&oacute;n que es le&iacute;da.</p>
			
			<p>
				<em>&iquest;Por qu&eacute; despu&eacute;s de ser recomendada
					una interpretaci&oacute;n de un fragmento ninguna otra
					interpretaci&oacute;n del mismo fragmento vuelve a ser recomendada,
					incluso cuando la interpretaci&oacute;n le&iacute;da es de una
					edici&oacute;n que posee dos interpretaciones para ese fragmento?</em>
			</p>
			<p>Esa situaci&oacute;n tiene lugar porque el mecanismo de
				recomendaci&oacute;n de interpretaciones para la lectura se basa en
				el c&aacute;lculo de similitud entre fragmentos.</p>
			
			<p>Edici&oacute;n virtual</p>
			
			<p>Una edici&oacute;n virtual es una selecci&oacute;n de
				fragmentos que uno o m&aacute;s usuarios definen al escoger entre
				las interpretaciones existentes. Una edici&oacute;n virtual es una
				entidad singular que permite que los editores virtuales escojan los
				fragmentos que desean incluir, su ordenaci&oacute;n, y su
				anotaci&oacute;n a trav&eacute;s de notas y categor&iacute;as. El
				conjunto de categor&iacute;as asociadas a una edici&oacute;n virtual
				define su taxonom&iacute;a.</p>
			
			<p>
				<em>&iquest;Qu&eacute; es la Edici&oacute;n del Archivo LdoD?</em>
			</p>
			<p>
				Es una edici&oacute;n experimental del <em>Archivo LdoD</em> que
				utiliza la interpretaci&oacute;n autoral representativa de cada uno
				de los fragmentos. Posee todav&iacute;a una taxonom&iacute;a para el
				libro definida por los editores del <em>Archivo</em>, que
				adicionalmente anotaron las interpretaciones con las
				categor&iacute;as de la taxonom&iacute;a.
			</p>
			
			<p>
				<em>&iquest;C&oacute;mo puedo crear una edici&oacute;n virtual?</em>
			</p>
			<p>En la interfaz virtual se puede crear una nueva edici&oacute;n
				virtual. La edici&oacute;n virtual puede usar como base una
				edici&oacute;n ya existente, de especialista o virtual, y quedar,
				as&iacute;, con toda la informaci&oacute;n asociada a la
				edici&oacute;n de base. Si no se escoge una edici&oacute;n como
				base, la nueva edici&oacute;n virtual se inicia sin poseer ninguna
				interpretaci&oacute;n. En el caso de utilizar una edici&oacute;n
				virtual como base, la nueva edici&oacute;n virtual contiene todas
				las anotaciones sobre los fragmentos efectuadas en dicha
				edici&oacute;n de base. De todas formas, no es posible alterar esas
				anotaciones en la nueva edici&oacute;n, pero si estas fuesen
				alteradas en la edici&oacute;n de base, por los editores de la
				edici&oacute;n de base, los efectos de las alteraciones son visibles
				en la nueva edici&oacute;n. Este criterio fue escogido para respetar
				la autor&iacute;a de las anotaciones.</p>
			
			<p>
				<em>&iquest;Una edici&oacute;n virtual puede poseer m&aacute;s
					de una interpretaci&oacute;n del mismo fragmento?</em>
			</p>
			<p>No, s&oacute;lo es posible asociar una interpretaci&oacute;n
				por fragmento a una edici&oacute;n virtual.</p>
			
			<p>
				<em>&iquest;C&oacute;mo adiciono una interpretaci&oacute;n a
					una edici&oacute;n virtual?</em>
			</p>
			<p>Existen diversas formas de asociar una interpretaci&oacute;n
				de un fragmento a una edici&oacute;n virtual: (a) cuando la
				interpretaci&oacute;n es visualizada en la pantalla de
				comparaci&oacute;n de interpretaciones es posible adicionarla
				individualmente si ella a&uacute;n no forma parte de la
				edici&oacute;n; (2) en la interfaz de ordenaci&oacute;n manual de la
				edici&oacute;n virtual es posible buscar fragmentos y adicionarlos a
				la edici&oacute;n virtual.</p>
			
			<p>
				<em>&iquest;C&oacute;mo puedo eliminar una
					interpretaci&oacute;n de una edici&oacute;n virtual?</em>
			</p>
			<p>En la interfaz de ordenaci&oacute;n manual de la
				edici&oacute;n virtual es posible seleccionar una o m&aacute;s
				interpretaciones y removerlas.</p>
			
			<p>
				<em>&iquest;Qui&eacute;n puede adicionar y eliminar
					interpretaciones en una edici&oacute;n virtual?</em>
			</p>
			<p>Todos los editores de la edici&oacute;n virtual.</p>
			
			<p>
				<em>&iquest;Puedo colaborar con otras personas en la
					construcci&oacute;n de una edici&oacute;n virtual?</em>
			</p>
			<p>S&iacute;, una edici&oacute;n virtual puede tener varios
				editores. Existen dos tipos de roles para los editores de una
				edici&oacute;n virtual: los gestores y los editores. Los gestores
				pueden adicionar nuevos miembros a la edici&oacute;n virtual,
				s&oacute;lo es necesario saber cu&aacute;l es el Nombre de Usuario.
				Por otro lado, los gestores pueden aceptar pedidos de acceso de
				nuevos editores. Cualquier usuario puede efectuar un pedido de
				acceso a una edici&oacute;n virtual p&uacute;blica. Un gestor puede
				eliminar de la lista como editor a otro usuario o alterar su rol.
				N&oacute;tese que los gestores son tambi&eacute;n, por inherencia,
				editores, al poseer todos los privilegios de un editor.</p>
			
			<p>
				<em>&iquest;Qu&eacute; distingue una edici&oacute;n virtual
					p&uacute;blica de una privada?</em>
			</p>
			<p>
				Una edici&oacute;n privada s&oacute;lo es visible y puede ser
				manipulada para y por sus editores. Una edici&oacute;n
				p&uacute;blica es visible para toda la comunidad del <em>Archivo
					LdoD</em>, pero s&oacute;lo puede ser manipulada por sus editores, con
				excepci&oacute;n de las anotaciones, en los casos en que su
				creaci&oacute;n sea permitida a usuarios autentificados. S&oacute;lo
				los gestores de una edici&oacute;n virtual pueden alterar el estado
				de la edici&oacute;n entre p&uacute;blica y privada, as&iacute; como
				su nombre y acr&oacute;nimo.
			</p>
			
			<p>
				<em>&iquest;C&oacute;mo puedo anotar las interpretaciones de
					una edici&oacute;n virtual?</em>
			</p>
			<p>Las interpretaciones de una edici&oacute;n virtual pueden ser
				anotadas manual y autom&aacute;ticamente. La anotaci&oacute;n manual
				se puede aplicar a toda la transcripci&oacute;n o s&oacute;lo a un
				segmento del texto de la transcripci&oacute;n. La anotaci&oacute;n
				manual ocurre en la interfaz de comparaci&oacute;n de
				interpretaciones. La anotaci&oacute;n manual de un segmento de texto
				se efect&uacute;a seleccionando el texto, al que se le puede asociar
				un comentario, as&iacute; como, tambi&eacute;n, un conjunto de
				categor&iacute;as de la taxonom&iacute;a de la edici&oacute;n
				virtual. La anotaci&oacute;n manual de toda la transcripci&oacute;n
				puede ser hecha en la misma interfaz que la anotaci&oacute;n de un
				segmento del texto, pero en este caso las categor&iacute;as quedan
				asociadas a toda la transcripci&oacute;n. Naturalmente, en la misma
				interfaz es posible la eliminaci&oacute;n de anotaciones y sus
				categor&iacute;as. La anotaci&oacute;n autom&aacute;tica de las
				interpretaciones de una edici&oacute;n virtual ocurre cuando se
				aplica el algoritmo de generaci&oacute;n de t&oacute;picos, en la
				interfaz de la edici&oacute;n virtual, al quedar la
				transcripci&oacute;n de las interpretaciones anotadas con las
				categor&iacute;as generadas.</p>
			
			<p>
				<em>&iquest;Qui&eacute;n puede anotar interpretaciones de una
					edici&oacute;n virtual?</em>
			</p>
			<p>En la interfaz de taxonom&iacute;a, en edici&oacute;n virtual,
				los gestores de la edici&oacute;n virtual pueden indicar
				qui&eacute;n puede anotar una interpretaci&oacute;n. Son ofrecidas
				dos posibilidades: la creaci&oacute;n de anotaciones es restricta a
				los editores en la edici&oacute;n virtual; o cualquier usuario
				autentificado puede crear anotaciones. Por lo que se refiere a la
				edici&oacute;n y eliminaci&oacute;n de anotaciones, s&oacute;lo el
				usuario que las cre&oacute; puede efectuar estas operaciones.</p>
			
			<p>
				<em>&iquest;C&oacute;mo se define el conjunto de
					categor&iacute;as de una taxonom&iacute;a?</em>
			</p>
			<p>La taxonom&iacute;a de una edici&oacute;n virtual puede ser
				abierta o cerrada, en la medida que sea posible, o no, adicionar
				nuevas categor&iacute;as durante la anotaci&oacute;n de una
				interpretaci&oacute;n. La anotaci&oacute;n de una
				interpretaci&oacute;n ocurre en la interfaz de comparaci&oacute;n de
				interpretaciones. En los casos en que la taxonom&iacute;a es
				abierta, el conjunto de categor&iacute;as aumenta siempre que una
				categor&iacute;a no existente en la taxonom&iacute;a es incluida en
				una anotaci&oacute;n. Por otro lado, para ambas, taxonom&iacute;as
				abiertas y cerradas, es posible manipular el conjunto de
				categor&iacute;as existentes a trav&eacute;s de operaciones de
				creaci&oacute;n de categor&iacute;a, uni&oacute;n de dos o
				m&aacute;s categor&iacute;as, separaci&oacute;n de una
				categor&iacute;a en dos, y eliminaci&oacute;n de categor&iacute;a.
				Esta manipulaci&oacute;n del conjunto de categor&iacute;as de la
				taxonom&iacute;a es hecha a trav&eacute;s de la interfaz
				taxonom&iacute;a de la edici&oacute;n virtual. Es a&uacute;n posible
				aumentar autom&aacute;ticamente el n&uacute;mero de
				categor&iacute;as de una edici&oacute;n virtual por medio de la
				aplicaci&oacute;n del algoritmo de generaci&oacute;n de
				t&oacute;picos.</p>
			
			<p>
				<em>&iquest;Qui&eacute;n pude definir si una taxonom&iacute;a
					es abierta o cerrada?</em>
			</p>
			<p>En la interfaz de taxonom&iacute;a, en edici&oacute;n virtual,
				los gestores de la edici&oacute;n virtual pueden definir si la
				taxonom&iacute;a es abierta o cerrada.</p>
			
			<p>
				<em>&iquest;Qu&eacute; es el algoritmo de creaci&oacute;n de
					t&oacute;picos?</em>
			</p>
			<p>
				El <a href="https://en.wikipedia.org/wiki/Topic_model">algoritmo
					de creaci&oacute;n de t&oacute;picos</a> agrupa las interpretaciones de
				la edici&oacute;n virtual en t&oacute;picos. Cada uno de estos
				t&oacute;picos es incluido en la taxonom&iacute;a como una nueva
				categor&iacute;a. En el proceso de creaci&oacute;n, el usuario puede
				indicar cuantos t&oacute;picos diferentes pretende ver generados, el
				n&uacute;mero de palabras que tendr&aacute; en el nombre de la nueva
				categor&iacute;a (son escogidas las palabras m&aacute;s relevantes
				del t&oacute;pico), el valor de similitud para que una
				interpretaci&oacute;n pueda ser incluida en el t&oacute;pico (para
				que la interpretaci&oacute;n sea autom&aacute;ticamente anotada por
				la respectiva categor&iacute;a), y el n&uacute;mero de interacciones
				que el algoritmo debe ejecutar antes de generar los t&oacute;picos
				(las interacciones deben ser entre 1500 y 2000 para que los
				resultados sean de confianza). A trav&eacute;s de la
				aplicaci&oacute;n del algoritmo una interpretaci&oacute;n puede
				quedar asociada a varias categor&iacute;as. Para la creaci&oacute;n
				autom&aacute;tica de t&oacute;picos se debe acceder a la
				taxonom&iacute;a en la interfaz de la edici&oacute;n virtual.
			</p>
			
			<p>
				<em>&iquest;Qui&eacute;n puede manipular las categor&iacute;as
					de una edici&oacute;n virtual?</em>
			</p>
			<p>En la interfaz de taxonom&iacute;a, en edici&oacute;n virtual,
				los gestores pueden definir si son s&oacute;lo los gestores que
				pueden manipular las categor&iacute;as, o si todos los editores de
				la edici&oacute;n virtual lo pueden hacer.</p>
			
			<p>
				<em>&iquest;Puedo utilizar las categor&iacute;as de las
					taxonom&iacute;as de las ediciones de base en las anotaciones de mi
					edici&oacute;n virtual?</em>
			</p>
			<p>
				S&iacute;, cuando se usa una edici&oacute;n virtual en la
				creaci&oacute;n de una nueva edici&oacute;n virtual se pueden usar
				las categor&iacute;as de la taxonom&iacute;a de base para anotar las
				interpretaciones en la nueva edici&oacute;n virtual. Esas
				categor&iacute;as poseen como prefijo el acr&oacute;nimo, en
				may&uacute;sculas, de la edici&oacute;n virtual utilizada.
				N&oacute;tese que si la categor&iacute;a usada fuese eliminada o
				alterado su nombre en la edici&oacute;n virtual de base, esas
				alteraciones se reflejar&iacute;an en la anotaci&oacute;n que
				utiliz&oacute; la categor&iacute;a, pues el <em>Archivo LdoD</em>
				preserva la autor&iacute;a de las anotaciones.
			</p>
			
			<p>
				<em>&iquest;C&oacute;mo puedo reordenar las interpretaciones en
					una edici&oacute;n virtual?</em>
			</p>
			<p>
				Existen dos formas de reordenar las interpretaciones: una forma
				manual y una forma asistida. La forma manual ocurre en la interfaz
				de edici&oacute;n de la edici&oacute;n virtual y consiste en la
				selecci&oacute;n de las interpretaciones que se pretenden modificar
				de posici&oacute;n, seguido de la colocaci&oacute;n en la nueva
				posici&oacute;n. Es a&uacute;n posible alterar la ordenaci&oacute;n
				de las interpretaciones en una edici&oacute;n virtual usando el
				algoritmo de recomendaci&oacute;n de la edici&oacute;n virtual. El
				algoritmo de recomendaci&oacute;n es id&eacute;ntico al utilizado en
				la recomendaci&oacute;n de lectura, con la diferencia que la
				similitud de taxonom&iacute;a utiliza, naturalmente, la
				taxonom&iacute;a de la edici&oacute;n virtual, en vez de la
				taxonom&iacute;a de la <em>Edici&oacute;n del Archivo LdoD</em>. Una
				vez aplicado el algoritmo de recomendaci&oacute;n, todas las
				interpretaciones son ordenadas a partir de la primera, o sea, la
				segunda interpretaci&oacute;n es la m&aacute;s semejante con la
				primera, y la tercera es la m&aacute;s semejante a la segunda,
				excluyendo la primera interpretaci&oacute;n, y as&iacute;
				sucesivamente. Para generar una nueva ordenaci&oacute;n se pueden
				alterar los criterios o colocar una determinada
				interpretaci&oacute;n como inicial. Una vez alterada la
				ordenaci&oacute;n, esta pude ser guardada, lo que conlleva una
				redefinici&oacute;n de la numeraci&oacute;n asociada a cada
				interpretaci&oacute;n.
			</p>
			
			<p>
				<em>&iquest;Qui&eacute;n puede reordenar las interpretaciones
					en una edici&oacute;n virtual?</em>
			</p>
			<p>Todos los editores de la edici&oacute;n virtual.</p>
			
			<p>
				<em>Cuando una edici&oacute;n virtual es creada,
					&iquest;cu&aacute;les son las configuraciones por defecto?</em>
			</p>
			<p>Por defecto los editores pueden anotar las interpretaciones,
				pero esa posibilidad est&aacute; vedada a los usuarios
				autentificados, s&oacute;lo los gestores pueden manipular las
				categor&iacute;as de la taxonom&iacute;a, al ser el vocabulario de
				la taxonom&iacute;a abierto.</p>
			
			<p>
				<em>&iquest;Cu&aacute;les pueden ser algunos de los escenarios
					t&iacute;picos asociados a la creaci&oacute;n de ediciones
					virtuales?</em>
			</p>
			<ul>
				<li>Un docente decide crear una edici&oacute;n virtual para que
					sus estudiantes puedan anotar los fragmentos de acuerdo con una
					taxonom&iacute;a pre-definida. En este escenario, la edici&oacute;n
					virtual es privada, s&oacute;lo el docente y los estudiantes
					tendr&aacute;n acceso. El docente ser&iacute;a el gestor y los
					estudiantes editores. S&oacute;lo el docente puede manipular las
					categor&iacute;as de la taxonom&iacute;a, y los estudiantes pueden
					anotar las interpretaciones. El vocabulario de la taxonom&iacute;a
					es cerrado.</li>
				<li>Un investigador desea hacer un estudio sobre la
					interpretaci&oacute;n social del <em>Libro</em> que cuente con la
					participaci&oacute;n de un n&uacute;mero no predefinido de
					personas. Para eso crea una edici&oacute;n virtual p&uacute;blica
					de la que es el &uacute;nico editor y gestor, coloca el vocabulario
					de la taxonom&iacute;a como abierto y da acceso de anotaci&oacute;n
					a todos los usuarios autentificados. Una vez configurada la
					edici&oacute;n virtual, promueve su experiencia en las redes
					sociales solicitando que los interesados se registren en el <em>Archivo
						LdoD</em> y comiencen a anotar las interpretaciones creando nuevas
					categor&iacute;as o utilizando las categor&iacute;as ya existentes.
				</li>
				<li>Un grupo de estudiantes de humanidades desea aumentar la <em>Edici&oacute;n
						del Archivo LdoD</em> con un conjunto adicional de categor&iacute;as y
					anotaciones. Para eso crean una nueva edici&oacute;n virtual que
					usa la <em>Edici&oacute;n del Archivo LdoD</em>, restringen la
					creaci&oacute;n de anotaciones a los editores, y permiten que todos
					los editores puedan manipular las categor&iacute;as de la
					taxonom&iacute;a. En este escenario el vocabulario deber&iacute;a
					ser abierto.
				</li>
				<li>Un amante de la obra de Pessoa desea hacer su propia
					edici&oacute;n del <em>Libro</em>. Para eso crea una edici&oacute;n
					virtual privada sin usar ninguna de las existentes. Adiciona las
					interpretaciones que considera que son relevantes y define su
					propio orden. Adem&aacute;s, define su taxonom&iacute;a y anota las
					interpretaciones. Cuando termina su edici&oacute;n la hace
					p&uacute;blica y procede a su divulgaci&oacute;n. En este
					escenario, s&oacute;lo existe un editor, que tambi&eacute;n es el
					gestor. Este editor-gestor puede crear categor&iacute;as para una
					taxonom&iacute;a con vocabulario abierto para anotar las
					interpretaciones, pero otros usuarios no pueden anotar las
					interpretaciones.
				</li>
			</ul>
			
			<p>[En curso, versi&oacute;n de 2 de diciembre de 2016]</p>
			<p>[Revisado, 29 de diciembre de 2016]</p>
		</div>
	</div>
</body>
</html>