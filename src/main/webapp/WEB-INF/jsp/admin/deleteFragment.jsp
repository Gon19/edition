<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/ldod-header.jsp"%>

	<div class="container">
		<h1 class="text-center">
			<spring:message code="deletefragment.title" />
		</h1>

		<div id="fragmentList" class="row">
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th><spring:message code="tableofcontents.title" /></th>
					</tr>
				<tbody>
					<c:forEach var="fragment" items='${fragments}'>
						<tr>
							<td><form class="form-inline" method="POST"
									action="${contextPath}/manager/fragment/delete">
									${fragment.title} <input type="hidden" name="externalId"
										value="${fragment.externalId}" />
									<button type="submit" class="btn pull-right">
										<spring:message code="general.remove" />
									</button>
								</form></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>

