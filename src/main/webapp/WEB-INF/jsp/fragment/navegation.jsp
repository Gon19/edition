<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<div class="row-fluid">
	<c:forEach var="fragInter" items='${fragment.sortedInterps}'>
		<c:if test="${fragInter.sourceType=='EDITORIAL'}"> <spring:message code="navigation.edition"/>
			<a
				href="${contextPath}/edition/internalid/${fragInter.expertEdition.externalId}">
				${fragInter.expertEdition.editor}</a>
			<br>
			<div class="text-center">
				<table>
					<tr>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/prev/number/${fragInter.externalId}"><i
								class="icon-backward"></i></a></td>
						<td><c:choose>
								<c:when test="${fragInter.number !=0}">${fragInter.number}</c:when>
								<c:otherwise>${fragInter.startPage}</c:otherwise>
							</c:choose></td>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/next/number/${fragInter.externalId}"><i
								class="icon-forward"></i></a></td>
					</tr>
					<tr>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/prev/heteronym/${fragInter.externalId}"><i
								class="icon-backward"></i></a></td>
						<td><a
							href="${contextPath}/edition/internalid/heteronym/${fragInter.expertEdition.externalId}/${fragInter.heteronym.externalId}">
								${fragInter.heteronym.name} </a></td>
						<td><a class="btn btn-mini"
							href="${contextPath}/fragments/fragment/interpretation/next/heteronym/${fragInter.externalId}"><i
								class="icon-forward"></i></a></td>
					</tr>
				</table>
				<br>
			</div>
		</c:if>
	</c:forEach>
</div>
