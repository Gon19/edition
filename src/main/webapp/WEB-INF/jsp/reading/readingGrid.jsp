<!-- main container -->
<div class="main-content">
<script type="text/javascript">

  function openRecomModal() {
    $('#recommendationModal').modal()
  }

  function resetPrevRecom() {
    $.get("${contextPath}/reading/inter/prev/recom/reset");
    $('#recommendationModal').modal('hide');
    location.reload();
  }

  function changeWeight(type, value) {
    $.post('${contextPath}/reading/weight', {
      'type' : type,
      'value' : value
    }, function(result) {
     // alert(result);
    });
  }

  function reload() {
    location.reload();
  }

$(window).on("load resize",function(e){




  /* P�gina Leitura */
  // set the height of columns depending on screen size
  $(".reading__text").css("height", "auto");
  
  var height = $(".reading-grid").height();
  if( $(window).width() >= 768 ){
    $(".reading__column").css("height", height+50);
    $(".reading__column--open").css("height", height+50);
    $(".reading__text").css("height", height+50);
  }else{
    /*
    $(".reading__column").css("height", "8.33333%");
    $(".reading__column--open").css("height", "8.33333%");
    $(".reading__text").css("height", height+50);
    */
    $(".reading__text").prependTo('.reading-grid');
    $(".reading__column--open").prependTo('.reading-grid');
  }
  
});
</script>


<c:if test="${inter != null}">
  <c:set var="fragment" value="${inter.getFragment()}" />
</c:if>


<!--
${inter.getEdition().getAcronym()}
 ${expertEdition.getAcronym()}-->

<div class="row reading-grid">


<c:choose>
          <c:when test="${fragment == null}">
          


           <div class="reading__text col-xs-12 col-sm-7 no-pad style-point reading-book-title">
            

            <c:choose>
            <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('pt')}">
              <h1>Livro do Desassossego de Fernando Pessoa</h1>
            </c:when>
            <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('en')}">
             <h1>Book of Disquiet by Fernando Pessoa</h1>
            </c:when>  
            <c:when test="${pageContext.response.locale.language.equalsIgnoreCase('es')}">
              <h1>Libro del Desasosiego de Fernando Pessoa</h1>
            </c:when>   
            </c:choose>
          </div>

          </c:when>
</c:choose>


<c:forEach var="expertEdition" items='${ldoD.sortedExpertEdition}'>



   
<c:choose>
    <c:when test="${inter.getEdition().getAcronym() == expertEdition.getAcronym()}">
      <div class="reading__column--open col-xs-12 col-sm-1 no-pad">
    </c:when>
    <c:otherwise>
      <div class="reading__column col-xs-12 col-sm-1 no-pad">
    </c:otherwise>
</c:choose>

    
         <h4>
          <!--<a href="${contextPath}/edition/internalid/${expertEdition.externalId}">${expertEdition.editor}</a>-->
          ${expertEdition.editor}
          </h4>

          

          <c:choose>
          <c:when test="${fragment == null}">
            <a
              href="${contextPath}/reading/inter/first/edition/${expertEdition.getExternalId()}"><%-- <spring:message
                code="general.reading.start" /> --%> <img src="/resources/img/graphics/arrow_right.png"></a>
          </c:when>

          <c:otherwise>

            <!-- ###################### interpretations ###################### -->
            <c:forEach var="expertEditionInter"
              items="${expertEdition.getSortedInter4Frag(fragment)}">
              
                <!-- ###################### interpretations desktop ###################### -->
                <div class="hidden-xs" style="margin-bottom:25px">
                <a
                  href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}"><!--${expertEditionInter.getEdition().getAcronym()}-->
                  <h2>${expertEditionInter.number}</h2></a>


                <div class="arrows">

                <a href="${contextPath}/reading/inter/prev/number/${expertEditionInter.externalId}">
                 <img src="/resources/img/graphics/arrow_left.png"></a>

                <a
                  href="${contextPath}/reading/inter/next/number/${expertEditionInter.externalId}">
                 <img src="/resources/img/graphics/arrow_right.png"></a>
                 </div>
                </div>

                <!-- ###################### interpretations mobile ###################### -->
                <div class="visible-xs-block">
                <a
                  href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${expertEditionInter.urlId}"><!--${expertEditionInter.getEdition().getAcronym()}-->
                  <h2>${expertEditionInter.number}</h2></a>


               

                 <a href="${contextPath}/reading/inter/next/number/${expertEditionInter.externalId}">
                 <img src="/resources/img/graphics/arrow_right.png"></a>

                <a href="${contextPath}/reading/inter/prev/number/${expertEditionInter.externalId}">
                 <img src="/resources/img/graphics/arrow_left.png"></a>

               
               
                </div>

               
                 
            </c:forEach>
          </c:otherwise>
        </c:choose>
    </div>

    <c:if test="${inter.getEdition().getAcronym() == expertEdition.getAcronym()}">
      <div class="reading__text col-xs-12 col-sm-7 no-pad style-point">
      <h1><a href="/fragments/fragment/inter/${inter.getExternalId()}">${inter.title}</a></h1>
      <br><br>
      <p>
       ${writer.getTranscription()}
      </p>
    </div>
    </c:if>

</c:forEach>


<!-- ###################### RECOMMENDATION ###################### -->

<div class="reading__column col-xs-12 col-sm-1 no-pad recommendation-line">
      <h4 class="f--condensed"><a href="#" onClick="openRecomModal()"><spring:message
            code="general.recommendation" /> </a></h4>


     
  <c:if test="${fragment != null}">
        <div class="h3-group">
      

        <c:if test="${prevRecom != null}">
          <div class="h3-div">

            <a href="${contextPath}/reading/inter/prev/recom">
              <h3>${prevRecom.getEdition().getAcronym()}</h3>
              <h2>${prevRecom.number}</h2>
              <!--<img src="../../resources/img/graphics/arrow_left.png">-->
            </a>
          
          </div>
        </c:if>
        
        <div>
          <a href="${contextPath}/reading/fragment/${fragment.xmlId}/inter/${inter.urlId}">
          <!--<span class="glyphicon glyphicon-play"></span>-->
           <h3>${inter.getEdition().getAcronym()}</h3><h2>${inter.number}</h2></a>
        </div>
        
        <c:forEach var="recomInter" items="${recommendations}">
          <div>
            <a href="${contextPath}/reading/fragment/${recomInter.fragment.xmlId}/inter/${recomInter.urlId}">
            <!--<span class="glyphicon glyphicon-forward"></span>-->
              <h3>${recomInter.getEdition().getAcronym()}</h3><h2>${recomInter.number}</h2></a>
          </div>
        </c:forEach>

        </div>
  </c:if>

</div>
</div>

 
<!-- ###################### MODAL ###################### -->

<!-- Recommendations configuration Modal HTML -->
<div class="modal fade" id="recommendationModal" tabindex="-1"
  role="dialog">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" onclick="reload()"
          data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        <h3 class="modal-title text-center">
          <spring:message code="general.recommendation.config" />
        </h3>
      </div>
      <div class="modal-body">
        <div class="row">
          <div class="form-group" id="clearPrevRecomForm">
            <div class="col-md-7">
              <h4 class="text">
                <spring:message code="general.reset.list" />
                :
              </h4>
            </div>
            <div class="col-md-1">
              <button type="submit" class="btn btn-primary"
                onclick="resetPrevRecom()">
                <span class="glyphicon glyphicon-saved"></span>
                <spring:message code="general.reset" />
              </button>
            </div>
          </div>
        </div>
        <hr>
        <div class="row text-center">
          <div class="col-md-3 text-center">
            <h4>
              <spring:message code="recommendation.criteria" />
              :
            </h4>
          </div>
          <div class="col-md-2 col-sm-4">
            <spring:message code="general.heteronym" />
            <input type="range" class="range"
              onChange="changeWeight('heteronym', value)"
              value='${ldoDSession.getRecommendation().getHeteronymWeight()}'
              max="1" min="0" step="0.2">
          </div>
          <div class="col-md-2 col-sm-4">
            <spring:message code="general.date" />
            <input type="range" class="range"
              onChange="changeWeight('date', value)"
              value='${ldoDSession.getRecommendation().getDateWeight()}'
              max="1" min="0" step="0.2">
          </div>
          <div class="col-md-2 col-sm-4">
            <spring:message code="general.text" />
            <input type="range" class="range"
              onChange="changeWeight('text', value)"
              value='${ldoDSession.getRecommendation().getTextWeight()}'
              max="1" min="0" step="0.2">
          </div>
          <div class="col-md-2 col-sm-4">
            <spring:message code="general.taxonomy" />
            <input type="range" class="range"
              onChange="changeWeight('taxonomy', value)"
              value="${ldoDSession.getRecommendation().getTaxonomyWeight()}"
              max="1.0" min="0.0" step="0.2">
          </div>
        </div>
        <br>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" onclick="reload()"
            data-dismiss="modal">
            <spring:message code="general.close" />
          </button>
        </div>
      </div>
      <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
  </div>
  <!-- /.modal -->