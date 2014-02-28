<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>

    <div class="container">
        <h1 class="text-center">
            <spring:message code="header.manageeditions" />
        </h1>
        <br>
        <div class="row">
            <form class="form-inline" method="POST"
                action="/virtualeditions/restricted/create">
                <fieldset>
                    <c:forEach var="error" items='${errors}'>
                        <div class="row text-error">
                            <spring:message code="${error}" />
                        </div>
                    </c:forEach>
                    <input type="text" class="input-small"
                        name="acronym" id="acronym"
                        placeholder="<spring:message code="virtualeditionlist.acronym" />"
                        value="${acronym}" /> <input type="text"
                        class="input" name="title" id="title"
                        placeholder="<spring:message code="virtualeditionlist.name" />"
                        value="${title}" /> <select
                        class="selectpicker" name="pub" id="pub">
                        <c:choose>
                            <c:when test="${pub == false}">
                                <option value="true">
                                    <spring:message
                                        code="general.public" />
                                </option>
                                <option value="false" selected>
                                    <spring:message
                                        code="general.private" />
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="true" selected>
                                    <spring:message
                                        code="general.public" />
                                </option>
                                <option value="false">
                                    <spring:message
                                        code="general.private" />
                                </option>
                            </c:otherwise>
                        </c:choose>
                    </select>
                    <button type="submit" class="btn btn-sm">
                        <span class="glyphicon glyphicon-edit"></span>
                        <spring:message code="general.create" />
                    </button>
                </fieldset>
            </form>
        </div>
        <div class="row">
            <div>
                <table
                    class="table table-striped table-bordered table-condensed table-hover">
                    <thead>
                        <tr>
                            <th><spring:message
                                    code="virtualeditionlist.acronym" /></th>
                            <th><spring:message
                                    code="virtualeditionlist.name" /></th>
                            <th><spring:message code="general.date" /></th>
                            <th><spring:message
                                    code="general.access" /></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="virtualEdition"
                            items='${virtualEditions}'>
                            <tr>
                                <td>${virtualEdition.acronym}</td>
                                <td>${virtualEdition.title}</td>
                                <td>${virtualEdition.getDate().toString("dd-MM-yyyy")}</td>
                                <td><c:choose>
                                        <c:when
                                            test="${virtualEdition.pub}">
                                            <spring:message
                                                code="general.public" />
                                        </c:when>
                                        <c:otherwise>
                                            <spring:message
                                                code="general.private" />
                                        </c:otherwise>
                                    </c:choose></td>
                                <td><form class="form-inline"
                                        method="POST"
                                        action="${contextPath}/virtualeditions/toggleselection">
                                        <input type="hidden"
                                            name="externalId"
                                            value="${virtualEdition.externalId}" />
                                        <button type="submit"
                                            class="btn btn-sm">
                                            <span class="glyphicon glyphicon-check"></span>
                                            <c:choose>
                                                <c:when
                                                    test="${ldoDSession.selectedVEs.contains(virtualEdition)}">
                                                    <spring:message
                                                        code="general.deselect" />
                                                </c:when>
                                                <c:otherwise>
                                                    <spring:message
                                                        code="general.select" />
                                                </c:otherwise>
                                            </c:choose>
                                        </button>
                                    </form></td>
                                <c:choose>

                                    <c:when
                                        test="${virtualEdition.participantSet.contains(user)}">
                                        <td><a class="btn btn-sm"
                                            href="${contextPath}/virtualeditions/restricted/editForm/${virtualEdition.externalId}"><span class="glyphicon glyphicon-edit"></span> <spring:message
                                                    code="general.edit" /></a></td>
                                        <td><a class="btn btn-sm"
                                            href="${contextPath}/virtualeditions/restricted/participantsForm/${virtualEdition.externalId}"><span class="glyphicon glyphicon-edit"></span> <spring:message
                                                    code="participant.manage" /></a></td>
                                        <td>
                                            <form class="form-inline"
                                                method="POST"
                                                action="${contextPath}/virtualeditions/restricted/delete">
                                                <input type="hidden"
                                                    name="externalId"
                                                    value="${virtualEdition.externalId}" />
                                                <button type="submit"
                                                    class="btn btn-sm">
                                                    <span class="glyphicon glyphicon-remove"></span>
                                                    <spring:message
                                                        code="general.delete" />
                                                </button>
                                            </form>
                                        </td>
                                    </c:when>
                                    <c:otherwise>
                                        <td></td>
                                        <td></td>
                                        <td></td>
                                    </c:otherwise>
                                </c:choose>
                            </tr>

                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</body>
</html>