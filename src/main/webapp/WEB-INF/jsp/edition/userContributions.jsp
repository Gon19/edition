<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
<link rel="stylesheet" type="text/css" href="/resources/css/bootstrap-table.min.css">
<script src="/resources/js/bootstrap-table.min.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

	<div class="container">
		<h3 class="text-center">
			<spring:message code="general.contributionsOf" />
			${user.firstName} ${user.lastName} (${user.username})
			(${user.getFragInterSet().size()})
		</h3>
		<br>
		<table id="tableUser" data-pagination="false">
		<!-- <table class="table table-hover table-condensed"> -->
			<thead>
				<tr>
					<th><spring:message code="tableofcontents.title" /></th>
					<th><spring:message code="general.edition" /></th>
					<th><spring:message code="general.category" /></th>
					<th><spring:message code="tableofcontents.usesEditions" /></th>
				</tr>
			<tbody>
				<c:forEach var="inter" items='${user.getFragInterSet()}'>
					<tr>
						<td><a
							href="${contextPath}/fragments/fragment/inter/${inter.externalId}">${inter.title}</a></td>
						<td><a
							href="${contextPath}/edition/acronym/${inter.edition.acronym}">${inter.getEdition().getReference()}</a></td>
						<td><c:forEach var="category"
								items='${inter.getAssignedCategories(user)}'>
								<a
									href="${contextPath}/edition/category/${category.getExternalId()}">
									${category.getNameInEditionContext(inter.getEdition())} </a>
                            </c:forEach></td>
						<td><c:forEach var="used" items="${inter.getListUsed()}">-><a
									href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
							</c:forEach></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>
<script>
$('#tableUser').attr("data-search","true");
$('#tableUser').bootstrapTable();
$(".tip").tooltip({placement: 'bottom'});
</script>

