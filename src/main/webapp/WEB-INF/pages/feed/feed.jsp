<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>

<div class="container">

  <%-- Alertas de sessão --%>
  <c:if test="${not empty sessionScope.erro}">
    <div class="alert-erro">${sessionScope.erro}</div>
    <c:remove var="erro" scope="session"/>
  </c:if>
  <c:if test="${not empty sessionScope.sucesso}">
    <div class="alert-sucesso">${sessionScope.sucesso}</div>
    <c:remove var="sucesso" scope="session"/>
  </c:if>

  <%-- Caixa de novo post --%>
  <div class="card">
    <form method="post"
          action="${pageContext.request.contextPath}/post/criar"
          enctype="multipart/form-data">
      <div class="form-group">
        <textarea name="conteudo" rows="3" maxlength="280"
                  placeholder="O que está acontecendo?" required></textarea>
      </div>
      <div style="display:flex; justify-content:space-between; align-items:center; gap:1rem;">
        <input type="file" name="imagem" accept="image/*" style="flex:1;">
        <button type="submit" class="btn">Postar</button>
      </div>
    </form>
  </div>

  <%-- Lista de posts --%>
  <c:choose>
    <c:when test="${empty posts}">
      <div class="card" style="text-align:center; color:#888;">
        Nenhum post no feed ainda. Siga outros usuários ou publique algo!
      </div>
    </c:when>
    <c:otherwise>
      <c:forEach var="post" items="${posts}">
        <div class="card">

          <div class="post-header">
            <c:choose>
              <c:when test="${not empty post.autor.fotoPerfil}">
                <img src="${pageContext.request.contextPath}/uploads/${post.autor.fotoPerfil}"
                     alt="Foto de ${post.autor.nome}">
              </c:when>
              <c:otherwise>
                <div style="width:40px;height:40px;border-radius:50%;background:#1da1f2;
                            display:flex;align-items:center;justify-content:center;
                            color:white;font-weight:bold;font-size:1.1rem;">
                  ${post.autor.username[0]}
                </div>
              </c:otherwise>
            </c:choose>
            <div>
              <a href="${pageContext.request.contextPath}/perfil?username=${post.autor.username}"
                 class="post-autor">${post.autor.nome}</a>
              <span class="post-username">@${post.autor.username}</span>
            </div>
          </div>

          <p class="post-conteudo">${post.conteudo}</p>

          <c:if test="${not empty post.imagem}">
            <div class="post-imagem">
              <img src="${pageContext.request.contextPath}/uploads/${post.imagem}"
                   alt="Imagem do post">
            </div>
          </c:if>

          <div class="post-actions">
            <form method="post" action="${pageContext.request.contextPath}/post/curtir">
              <input type="hidden" name="postId"   value="${post.id}">
              <input type="hidden" name="origem"   value="feed">
              <button type="submit" class="btn-like">❤ Curtir</button>
            </form>
            <span style="color:#aaa;">
              <fmt:formatDate value="${post.dtCriacao}" pattern="dd/MM/yyyy HH:mm"/>
            </span>
          </div>

        </div>
      </c:forEach>
    </c:otherwise>
  </c:choose>

  <%-- Paginação --%>
  <div class="paginacao">
    <c:if test="${page > 1}">
      <a href="${pageContext.request.contextPath}/feed?page=${page - 1}">← Anterior</a>
    </c:if>
    <span style="padding:0.4rem 0.8rem;">Página ${page}</span>
    <c:if test="${temProxima}">
      <a href="${pageContext.request.contextPath}/feed?page=${page + 1}">Próxima →</a>
    </c:if>
  </div>

</div>

<%@ include file="../footer.jsp" %>
