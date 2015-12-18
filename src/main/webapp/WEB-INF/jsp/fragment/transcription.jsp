<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id="fragmentTranscription" >

	
		<h4 class="text-center">${inters.get(0).title}</h4>
		<c:choose>
			<c:when test="${inters.get(0).lastUsed.sourceType=='EDITORIAL'}">
				<div class="well" style="font-family: georgia; font-size: medium;">
					<p>${writer.getTranscription()}</p>
				</div>
			</c:when>
			<c:otherwise>
				<div class="well" style="font-family: courier;">
					<p>${writer.getTranscription()}</p>
				</div>
			</c:otherwise>
		</c:choose>

</div>


