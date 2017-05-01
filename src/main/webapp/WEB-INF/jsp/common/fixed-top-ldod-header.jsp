<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav class="navbar navbar-default navbar-fixed-top" role="navigation"
	style="display: none">
    <%@ include file="/WEB-INF/jsp/common/ldod-header.jsp"%>
</nav>
<nav class="ldod-navbar navbar navbar-default navbar-fixed-top"
	role="navigation">
	
	<div class="container-fluid">
	
		<div class="container">
	
		<div class="navbar-header">
		
			<button type="button"
				class="navbar-toggle"
				data-toggle="collapse"
				data-target=".navbar-collapse">
			
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="${contextPath}/"><spring:message code="header.title" /></a>
			
			
			<ul class="nav navbar-nav navbar-right hidden-xs">
			
				<!-- Login -->
				<c:choose>
					<c:when test="${pageContext.request.userPrincipal.authenticated}">
						<li class="dropdown">
							<a href="#"
								class="dropdown-toggle"
								data-toggle="dropdown">
								
								${pageContext.request.userPrincipal.principal.getUser().getFirstName()}
								${pageContext.request.userPrincipal.principal.getUser().getLastName()}
								<span class="caret"></span>
							</a>
							
							<ul class="dropdown-menu">
								<li><a href="<c:url value="${contextPath}/user/changePassword"/>"><spring:message code="user.password" /></a></li>
								<li><a href="<c:url value="${contextPath}/signout"/>"><spring:message code="header.logout" /></a></li>
							</ul>
					</c:when>
					<c:otherwise>
						<li><a href="${contextPath}/signin"><spring:message code="header.login" /></a></li>
					</c:otherwise>
				</c:choose>
			
			</ul>
			
		</div>
		
		</div>
		
	</div>
	
	<div class="container">
		
		<div class="collapse navbar-collapse">
			<ul class="nav navbar-nav navbar-nav-flex">
			
				<!-- About -->
				<li class="dropdown">
					<a href="#"
						class="dropdown-toggle"
						data-toggle="dropdown">
						
						<spring:message code="header.about" />
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<div class="dropdown-menu-bg"></div>
						<li><a href="${contextPath}/about/archive"><spring:message code="header.archive" /></a></li>
						<li><a href="${contextPath}/about/faq"><spring:message code="header.faq" /></a></li>
						<li><a href="${contextPath}/about/articles"><spring:message code="header.articles" /></a></li>
						<li><a href="${contextPath}/about/team"><spring:message code="header.team" /></a></li>
						<li><a href="${contextPath}/about/sponsors"><spring:message code="header.sponsors" /></a></li>
						<li><a href="${contextPath}/about/copyright"><spring:message code="header.copyright" /></a></li>
					</ul>
				</li>

				<!-- Reading -->
				<li><a href="${contextPath}/reading"><spring:message code="general.reading" /></a></li>
				
				<!-- Documents -->
				<li class="dropdown">
					<a href="#"
						class="dropdown-toggle"
						data-toggle="dropdown">
						
						<spring:message code="header.documents" />
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<div class="dropdown-menu-bg"></div>
						<li><a href="${contextPath}/source/list"><spring:message code="authorial.source" /></a></li>
						<li><a href="${contentPath}/fragments"><spring:message code="fragment.codified" /></a></li>
					</ul>
				</li>
				
				<!-- Editions -->
				<li class="dropdown">
					<a href="#"
						class="dropdown-toggle"
						data-toggle="dropdown">
						
						<spring:message code="header.editions" />
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu">
						<div class="dropdown-menu-bg"></div>
						<li><a href="${contextPath}/edition/acronym/JPC">Jacinto do Prado Coelho</a></li>
						<li><a href="${contextPath}/edition/acronym/TSC">Teresa Sobral Cunha</a></li>
						<li><a href="${contextPath}/edition/acronym/RZ">Richard Zenith</a></li>
						<li><a href="${contextPath}/edition/acronym/JP">Jer�nimo Pizarro</a></li>
						<li class="divider"></li>
						<li><a href="${contextPath}/edition/acronym/LdoD-Arquivo">Arquivo LdoD</a></li>
						<li class="divider"></li>
						<c:forEach var="acronym" items='${ldoDSession.selectedVEAcr}'>
							<li><a href="${contextPath}/edition/acronym/${acronym}">${acronym}</a></li>
						</c:forEach>
					</ul>
				</li>
				
				<!-- Search -->
				<li class='dropdown'>
					<a href="#"
						class="dropdown-toggle"
						data-toggle="dropdown">
						
						<spring:message code="header.search" />
						<span class='caret'></span>
					</a>
				
					<ul class="dropdown-menu">
						<div class="dropdown-menu-bg"></div>
						<li><a href="${contextPath}/search/simple"> <spring:message code="header.search.simple" /></a></li>
						<li><a href="${contentPath}/search/advanced"> <spring:message code="header.search.advanced" /></a></li>
					</ul>
				</li>
			
				<!-- Manage Virtual Editions -->
				<li><a href="${contextPath}/virtualeditions"><spring:message code="virtual" /> </a></li>
			
				<!-- Administration -->

				<c:if test='${pageContext.request.userPrincipal.principal.hasRole("ROLE_ADMIN")}'>
					<li class="dropdown">
						<a href="#"
							class="dropdown-toggle"
							data-toggle="dropdown">
							
							<spring:message code="header.admin" />
						
							<span class="caret"></span>
						</a>
					
						<ul class="dropdown-menu">
							<div class="dropdown-menu-bg"></div>
							<li><a href="${contextPath}/admin/loadForm"><spring:message code="load" /></a></li>
							<li class="divider"></li>
							<li><a href="${contextPath}/admin/exportForm"><spring:message code="general.export" /></a></li>
							<li class="divider"></li>
							<li><a href="${contextPath}/admin/fragment/list"><spring:message code="fragment.delete" /></a></li>
							<li class="divider"></li>
							<li><a href="${contextPath}/admin/user/list"><spring:message code="user.manage" /></a></li>
						</ul>
					</li>
				</c:if>
				
				<!--  Language -->
				<li class="nav-lang">
					<a href="/?lang=pt_PT" class="active">PT</a>
					<a href="/?lang=en">EN</a>
					<a href="/?lang=es">ES</a>
				</li>
			</ul>
			
		</div>
	</div>
</nav>
