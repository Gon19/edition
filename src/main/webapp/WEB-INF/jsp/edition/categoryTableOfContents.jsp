<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <h3 class="text-center">
            <spring:message code="virtualedition" />
            : <a
                href="${contextPath}/edition/internalid/${category.getTaxonomy().getEdition().getExternalId()}">
                ${category.getTaxonomy().getEdition().title}</a>
            <spring:message code="general.taxonomy" />
            : <a
                href="${contextPath}/edition/taxonomy/${category.getTaxonomy().getExternalId()}">${category.getTaxonomy().getName()}</a>
            <spring:message code="general.category" />
            : ${category.getName()} (${category.getTagSet().size()})
        </h3>

        <table class="table table-hover table-condensed">
            <thead>
                <tr>
                    <th><spring:message
                            code="tableofcontents.title" /></th>
                    <c:if test="${taxonomy.getAdHoc() }">
                        <th><spring:message code="general.weight" /></th>
                    </c:if>
                    <th><spring:message
                            code="tableofcontents.usesEditions" /></th>
                    <th><spring:message
                            code="tableofcontents.number" /></th>
                </tr>
            <tbody>
                <c:forEach var="categoryInFragInter"
                    items='${category.getSortedActiveTags()}'>
                    <tr>
                        <td><a
                            href="${contextPath}/fragments/fragment/inter/${categoryInFragInter.getFragInter().getExternalId()}">${categoryInFragInter.getFragInter().getTitle()}</a></td>
                        <c:if test="${taxonomy.getAdHoc() }">
                            <td>${categoryInFragInter.getWeight()}</td>
                        </c:if>
                        <td><c:forEach var="used"
                                items="${categoryInFragInter.getFragInter().getListUsed()}">-><a
                                    href="${contextPath}/fragments/fragment/inter/${used.externalId}">${used.shortName}</a>
                            </c:forEach></td>
                        <td>${categoryInFragInter.getFragInter().getNumber()}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>

