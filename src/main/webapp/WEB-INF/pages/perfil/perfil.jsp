<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>

<div class="container">

  <%-- Alertas de sessão --%>
  <c:if test="${not empty sessionScope.sucesso}">
    <div class="alert-sucesso">${sessionScope.sucesso}</div>
    <c:remove var="sucesso" scope="session"/>
  </c:if>
  <c:if test="${not empty sessionScope.erro}">
    <div class="alert-erro">${sessionScope.erro}</div>
    <c:remove var="erro" scope="session"/>
  </c:if>

  <%-- Cabeçalho do perfil --%>
  <div class="card">
    <div style="display:flex; align-items:center; gap:1.2rem;">

      <%-- Foto de perfil --%>
      <c:choose>
        <c:when test="${not empty perfil.fotoPerfil}">
          <img src="${pageContext.request.contextPath}/uploads/${perfil.fotoPerfil}"
               alt="Foto de ${perfil.nome}"
               style="width:80px;height:80px;border-radius:50%;object-fit:cover;">
        </c:when>
        <c:otherwise>
          <div style="width:80px;height:80px;border-radius:50%;background:#1da1f2;
                      display:flex;align-items:center;justify-content:center;
                      color:white;font-size:2rem;font-weight:bold;">
            <c:otherwise>
			    <span style="font-size:2rem;">👤</span>
			</c:otherwise>
          </div>
        </c:otherwise>
      </c:choose>

      <%-- Info --%>
      <div style="flex:1;">
        <h2>${perfil.nome}</h2>
        <p style="color:#666;">@${perfil.username}</p>
        <c:if test="${not empty perfil.bio}">
          <p style="margin-top:0.4rem;">${perfil.bio}</p>
        </c:if>
        <div style="margin-top:0.6rem; display:flex; gap:1.5rem; font-size:0.9rem;">
          <span><strong>${seguidores}</strong> seguidores</span>
          <span><strong>${seguindo}</strong> seguindo</span>
        </div>
      </div>

      <%-- Ações --%>
      <div style="display:flex; flex-direction:column; gap:0.5rem;">
        <c:choose>
          <c:when test="${isProprioUser}">
            <a href="${pageContext.request.contextPath}/perfil/editar" class="btn">Editar perfil</a>
          </c:when>
          <c:otherwise>
            <form method="post" action="${pageContext.request.contextPath}/seguir">
              <input type="hidden" name="seguidoId"  value="${perfil.id}">
              <input type="hidden" name="username"   value="${perfil.username}">
              <c:choose>
                <c:when test="${isSeguindo}">
                  <button type="submit" class="btn btn-secondary">Deixar de seguir</button>
                </c:when>
                <c:otherwise>
                  <button type="submit" class="btn">Seguir</button>
                </c:otherwise>
              </c:choose>
            </form>
          </c:otherwise>
        </c:choose>
      </div>

    </div>
  </div>

  <%-- Abas: Posts / Curtidas --%>
  <div style="display:flex; gap:0; margin-bottom:1rem; border-bottom:2px solid #ddd;">
    <a href="${pageContext.request.contextPath}/perfil?username=${perfil.username}&aba=posts"
       style="padding:0.6rem 1.5rem; text-decoration:none; font-weight:bold;
              color:${aba == 'posts' ? '#1da1f2' : '#666'};
              border-bottom:${aba == 'posts' ? '2px solid #1da1f2' : 'none'};
              margin-bottom:-2px;">
      Posts
    </a>
    <a href="${pageContext.request.contextPath}/perfil?username=${perfil.username}&aba=curtidas"
       style="padding:0.6rem 1.5rem; text-decoration:none; font-weight:bold;
              color:${aba == 'curtidas' ? '#1da1f2' : '#666'};
              border-bottom:${aba == 'curtidas' ? '2px solid #1da1f2' : 'none'};
              margin-bottom:-2px;">
      ❤ Curtidas
    </a>
  </div>

  <%-- Lista de posts --%>
  <c:choose>
    <c:when test="${empty posts}">
      <div class="card" style="text-align:center; color:#888;">
        <c:choose>
          <c:when test="${aba == 'curtidas'}">Nenhum post curtido ainda.</c:when>
          <c:otherwise>Nenhuma publicação ainda.</c:otherwise>
        </c:choose>
      </div>
    </c:when>
    <c:otherwise>
      <c:forEach var="post" items="${posts}">
        <div class="card">

          <div class="post-header">
            <c:choose>
              <c:when test="${not empty post.autor.fotoPerfil}">
                <img src="${pageContext.request.contextPath}/uploads/${post.autor.fotoPerfil}"
                     alt="Foto">
              </c:when>
              <c:otherwise>
                <div style="width:40px;height:40px;border-radius:50%;background:#1da1f2;
                            display:flex;align-items:center;justify-content:center;
                            color:white;font-weight:bold;">
                  	<c:otherwise>
					    <span style="font-size:2rem;">👤</span>
					</c:otherwise>
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
              <img src="${pageContext.request.contextPath}/uploads/${post.imagem}" alt="Imagem">
            </div>
          </c:if>

          <div class="post-actions">
            <form method="post" action="${pageContext.request.contextPath}/post/curtir">
              <input type="hidden" name="postId" value="${post.id}">
              <input type="hidden" name="origem" value="perfil">
              <input type="hidden" name="username" value="${perfil.username}">
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
  <c:if test="${paginacao.totalPaginas > 1}">
    <div class="paginacao">
      <c:if test="${paginacao.temAnterior()}">
        <a href="${pageContext.request.contextPath}/perfil?username=${perfil.username}&aba=${aba}&page=${paginacao.anterior()}">← Anterior</a>
      </c:if>
      <span style="padding:0.4rem 0.8rem;">
        Página ${paginacao.paginaAtual} de ${paginacao.totalPaginas}
      </span>
      <c:if test="${paginacao.temProxima()}">
        <a href="${pageContext.request.contextPath}/perfil?username=${perfil.username}&aba=${aba}&page=${paginacao.proxima()}">Próxima →</a>
      </c:if>
    </div>
  </c:if>

</div>

<%@ include file="../footer.jsp" %>
