<%@ include file="/WEB-INF/jsp/common/tags-head.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/WEB-INF/jsp/common/meta-head.jsp"%>
</head>
<body>
	<%@ include file="/WEB-INF/jsp/common/fixed-top-ldod-header.jsp"%>
	<div class="container">
		<div class="jumbotron">
			<h1 class="text-center">Livro do Desassossego</h1>
			<div class="row col-md-offset-2">
				<%@ include file="/WEB-INF/jsp/reading/readingNavigation.jsp"%>
			</div>
			<br>
			<div class="row">
				<%@ include file="/WEB-INF/jsp/reading/readingText.jsp"%>
			</div>
		</div>
	</div>
</body>
<script>
	$(".tip").tooltip({
		placement : 'bottom'
	});
</script>
</html>
