<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<script src="/resources/js/spin-v2.3.2.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<c:set var="taxonomy" value="${virtualEdition.getTaxonomy()}" />
	<c:set var="userLdoD"
		value='${pageContext.request.userPrincipal.principal.getUser()}' />

	<div class="container">
		<h1 class="text-center">
			<spring:message code="virtualedition" />
			${virtualEdition.title}
		</h1>
		<div class="row">
			<h4 class="pull-right">
				<spring:message code="general.public.pages" />
				- <a
					href="${contextPath}/edition/internalid/${virtualEdition.getExternalId()}">
					<spring:message code="general.edition" />
				</a>
			</h4>
		</div>
		<h2 class="text-center">
			<spring:message code="general.taxonomy" />
			<c:if test="${virtualEdition.getAdminSet().contains(userLdoD)}">
				<a class="" role="button" data-toggle="collapse"
					href="#collapsemenu" aria-expanded="false"
					aria-controls="collapseExample" style="font-size: 18px"> <span
					class="glyphicon glyphicon-pencil"></span>
				</a>
			</c:if>
		</h2>

		<div class="row col-md-12 has-error">
			<c:forEach var="error" items='${errors}'>
				<div class="row">
					<spring:message code="${error}" />
				</div>
			</c:forEach>
		</div>
		<div class="row col-md-12">
			<div class="collapse" id="collapsemenu">
				<div class="well" style="height: 70px">
					<form class="form-inline" role="form" method="POST"
						action="/virtualeditions/restricted/${virtualEdition.getExternalId()}/taxonomy/edit/">
						<div class="form-group col-md-4" style="padding-left: 0px">
							<label class="control-label for="acronym"><spring:message
									code="taxonomy.manage" /></label> <select class="form-control"
								name="management" id="management">
								<c:choose>
									<c:when test="${taxonomy.getOpenManagement() == true}">
										<option value="true" selected>
											<spring:message code="taxonomy.manage.members" />
										</option>
										<option value="false"><spring:message
												code="taxonomy.manage.owners" /></option>
									</c:when>
									<c:otherwise>
										<option value="true"><spring:message
												code="taxonomy.manage.members" /></option>
										<option value="false" selected><spring:message
												code="taxonomy.manage.owners" /></option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
						<div class="form-group  col-md-4" style="padding-left: 0px">
							<label class="control-label" for="pub"><spring:message
									code="taxonomy.annotation" /></label> <select class="form-control"
								name="annotation" id="annotation">
								<c:choose>
									<c:when test="${taxonomy.getOpenAnnotation() == true}">
										<option value="true" selected>
											<spring:message code="taxonomy.annotation.all" />
										</option>
										<option value="false"><spring:message
												code="taxonomy.annotation.members" /></option>
									</c:when>
									<c:otherwise>
										<option value="true"><spring:message
												code="taxonomy.annotation.all" /></option>
										<option value="false" selected><spring:message
												code="taxonomy.annotation.members" /></option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
						<div class="form-group  col-md-3" style="padding-left: 0px">
							<label class="control-label" for="title"><spring:message
									code="taxonomy.vocabulary" /></label> <select class="form-control"
								name="vocabulary" id="vocabulary">
								<c:choose>
									<c:when test="${taxonomy.getOpenVocabulary() == true}">
										<option value="true" selected>
											<spring:message code="taxonomy.vocabulary.open" />
										</option>
										<option value="false"><spring:message
												code="taxonomy.vocabulary.closed" /></option>
									</c:when>
									<c:otherwise>
										<option value="true"><spring:message
												code="taxonomy.vocabulary.open" /></option>
										<option value="false" selected><spring:message
												code="taxonomy.vocabulary.closed" /></option>
									</c:otherwise>
								</c:choose>
							</select>
						</div>
						<div class="form-group  col-md-1"
							style="padding-right: 0px; padding-left: 0px">
							<label class="sr-only" for=submit><spring:message
									code="general.update" /></label>

							<button type="submit" class="btn btn-primary" id="submit">
								<span class="glyphicon glyphicon-ok"></span>
							</button>
							<button type="button" class="btn btn-default"
								onclick="$('#collapsemenu').collapse('hide')">
								<span class="glyphicon glyphicon-remove"></span>

							</button>
						</div>

						<div class="form-group  col-xs-12" id="test">
							<br> <br>
						</div>
						<div class="form-group  col-xs-12"></div>
					</form>
				</div>
			</div>
		</div>

		<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
			<div class="row">
				<div class="col-md-5">
					<c:forEach var="categoryError" items='${categoryErrors}'>
						<div class="row has-error">${categoryError}</div>
					</c:forEach>
					<form name="createCategory" class="form-inline" method="POST"
						action="/virtualeditions/restricted/category/create"
						onsubmit="return validateCreateCategoryForm()">
						<div class="form-group">
							<input type="hidden" class="form-control" name="externalId"
								value="${virtualEdition.externalId}" />
						</div>
						<div class="form-group">
							<input type="text" class="form-control" name="name"
								placeholder="<spring:message code="general.name" />">
						</div>
						<button type="submit" class="btn btn-primary">
							<span class="glyphicon glyphicon-plus"></span>
							<spring:message code="category.add" />
						</button>
					</form>
				</div>
				<div class="col-md-7">
					<button class="btn btn-primary pull-right" data-toggle="modal"
						data-target="#topicModal">
						<span class="glyphicon glyphicon-plus"></span>
						<spring:message code="topics.generate.short" />
					</button>
				</div>
			</div>
		</c:if>
		<br />
		<div class="row col-md-12">
			<div class="row">
				<table class="table table-hover">
					<form class="form-horizontal" role="form" method="POST"
						action="/virtualeditions/restricted/category/mulop" id="mulopForm">
						<div class="form-group">
							<div class="hidden">
								<label> <input type="hidden" name="taxonomyId"
									value="${taxonomy.getExternalId()}">
								</label> <label> <input type="hidden" name="type" id="type">
								</label>
							</div>
						</div>
						<thead>
							<tr>
								<th><spring:message code="general.category" /></th>
								<th><spring:message code="fragments" /></th>
								<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
									<th>
										<div class="dropdown">
											<button class="btn btn-primary dropdown-toggle" type="button"
												id="dropdownMenu" data-toggle="dropdown"
												aria-haspopup="true" aria-expanded="true">
												<spring:message code="general.action" />
												<span class="caret"></span>
											</button>
											<ul class="dropdown-menu" aria-labelledby="dropdownMenu">
												<li>
													<button class="btn btn-link" role="link"
														onclick="$('#type').val('merge'); submit()">
														<spring:message code="general.merge" />
													</button>
												</li>
												<li>
													<button class="btn btn-link" role="link"
														onclick="$('#type').val('delete'); submit()">
														<spring:message code="general.delete" />
													</button>
												</li>
											</ul>
										</div>
									</th>
								</c:if>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="category" items='${taxonomy.getCategoriesSet()}'>
								<tr>
									<td><a
										href="${contextPath}/virtualeditions/restricted/category/${category.getExternalId()}">${category.getName()}</a></td>
									<td><c:forEach var="tag"
											items='${category.getSortedTags()}'>
											<a
												href="${contextPath}/virtualeditions/restricted/fraginter/${tag.getInter().getExternalId()}">${tag.getInter().getTitle()}</a> (${tag.getWeight()})</c:forEach></td>
									<c:if test="${taxonomy.canManipulateTaxonomy(userLdoD)}">
										<td class="col-centered">
											<div class="form-group">
												<div class="checkbox text-center">
													<label> <input type="checkbox" name="categories[]"
														value="${category.getExternalId()}">
													</label>
												</div>
											</div>
										</td>
									</c:if>
								</tr>
							</c:forEach>
						</tbody>
					</form>
				</table>
			</div>
		</div>
	</div>
	<!-- Validate Modal HTML -->
	<div id="validateModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title">Informa��o</h4>
				</div>
				<div class="modal-body">
					<h3 id="message" />
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
				</div>
			</div>
		</div>
	</div>
	<!-- Topic Model Generation Modal HTML -->
	<div class="modal fade" id="topicModal" tabindex="-1" role="dialog">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title text-center">
						<spring:message code="topics.generate.long" />
					</h4>
				</div>
				<div class="modal-body">
					<div class="form-group" id="generationForm">
						<div class="col-md-2">
							<input type="text" class="form-control" id="numTopics"
								placeholder="<spring:message code="general.taxonomies.number.topics" />">
						</div>
						<div class="col-md-2">
							<input type="text" class="form-control" id="numWords"
								placeholder="<spring:message code="general.taxonomies.number.words" />">
						</div>
						<div class="col-md-2">
							<input type="text" class="form-control" id="thresholdCategories"
								placeholder="<spring:message code="general.taxonomies.threshold.categories" />">
						</div>
						<div class="col-md-2">
							<input type="text" class="form-control" id="numIterations"
								placeholder="<spring:message code="general.taxonomies.number.iterations" />">
						</div>

						<div class="col-md-2">
							<button type="submit" class="btn btn-primary"
								onclick="generate()">
								<span class="glyphicon glyphicon-cog"></span>
								<spring:message code="general.generate" />
							</button>
						</div>
					</div>
					<div class="row">
						<div class="col-md-6">
							<p class="text-danger" id="error"></p>
						</div>
					</div>
					<div id="topics"></div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</body>

<script>
function validateCreateCategoryForm() {
    var x = document.forms["createCategory"]["name"].value;
    if (x == null || x == "") {
        $("#message").html("Deve ser indicado o nome da categoria");
        $("#validateModal").modal('show');
        return false;
    }
}
</script>

<script>
function validateGenerateTopics(topics, words, threshold, iterations) {
	$("#error").empty();
	$("#topics").html("");
	
	var errors = [];
    if (topics == null || topics == "") {
    	errors.push("Deve ser indicado o n�mero de t�picos a gerar");
    } else if (isNaN(topics) || topics < 1) {
    	errors.push("Deve ser indicado um n�mero de t�picos positivo");
    }
    if (words == null || words == "") {
    	errors.push("Deve ser indicado o n�mero de palavras a apresentar");
    } else if (isNaN(words) || words < 1) {
    	errors.push("Deve ser indicado um n�mero de palavras positivo");
    }
    if (threshold == null || threshold == "") {
    	errors.push("Deve ser indicado o valor de corte de categorias");
    } else if (isNaN(threshold) || threshold < 0 || threshold > 100) {
    	errors.push("Deve ser indicado um valor de corte de categorias entre 0 e 100");
    }
    if (iterations == null || iterations == "") {
    	errors.push("Deve ser indicado o n�mero de itera��es");
    } else if (isNaN(iterations) || iterations < 1) {
    	errors.push("Deve ser indicado um n�mero de itera��es positivo");
    }
    
    if (errors.length > 0) {
   	 	$("#error").html(errors.join("<br>"));
    	return false;
    } else {
    	return true;
    }
}

function generate() {
	var topics = $("#numTopics").val();
	var words = $("#numWords").val();
	var threshold = $("#thresholdCategories").val();
	var iterations = $("#numIterations").val();
	
	if (validateGenerateTopics(topics, words, threshold, iterations)) {
		
		var opts = {
				  lines: 11 // The number of lines to draw
				, length: 28 // The length of each line
				, width: 17 // The line thickness
				, radius: 46 // The radius of the inner circle
				, scale: 0.35 // Scales overall size of the spinner
				, corners: 1 // Corner roundness (0..1)
				, color: '#000' // #rgb or #rrggbb or array of colors
				, opacity: 0.25 // Opacity of the lines
				, rotate: 40 // The rotation offset
				, direction: 1 // 1: clockwise, -1: counterclockwise
				, speed: 0.8 // Rounds per second
				, trail: 64 // Afterglow percentage
				, fps: 20 // Frames per second when using setTimeout() as a fallback for CSS
				, zIndex: 2e9 // The z-index (defaults to 2000000000)
				, className: 'spinner' // The CSS class to assign to the spinner
				, top: '45%' // Top position relative to parent
				, left: '85%' // Left position relative to parent
				, shadow: false // Whether to render a shadow
				, hwaccel: false // Whether to use hardware acceleration
				, position: 'absolute' // Element positioning
				};
				var target = document.getElementById('generationForm');
				var spinner = new Spinner(opts).spin(target);
	
	$.get("${contextPath}/virtualeditions/restricted/${virtualEdition.getExternalId()}/taxonomy/generateTopics", 
	{
		numTopics : topics,
		numWords : words,
		thresholdCategories : threshold,
		numIterations : iterations
	}, function(html) {
			spinner.stop();
			
			$("#topics").replaceWith(html)
		}
	)
	}
}

$("#topicModal").on("hidden.bs.modal", function(){
    $("#topics").html("");
});
</script>


</html>

