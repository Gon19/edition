<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>

<div id="fragmentTranscription" class="row">

    <div class="row">
        <h4 class="text-center">${inters.get(0).title}</h4>
        <c:choose>
            <c:when
                test="${inters.get(0).lastUsed.sourceType=='EDITORIAL'}">
                <div class="well" id="content"
                    style="font-family: georgia;">
                    <p>${writer.getTranscription()}</p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="well" id="content"
                    style="font-family: courier;">
                    <p>${writer.getTranscription()}</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
