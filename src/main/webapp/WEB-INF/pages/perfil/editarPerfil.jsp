<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>

<div class="container" style="max-width:480px;">
  <div class="card" style="margin-top:2rem;">
    <h2 style="margin-bottom:1.2rem;">Editar Perfil</h2>

    <c:if test="${not empty erro}">
      <div class="alert-erro">${erro}</div>
    </c:if>

    <form method="post"
          action="${pageContext.request.contextPath}/perfil/editar"
          enctype="multipart/form-data">

      <%-- Foto atual --%>
      <div class="form-group" style="display:flex; align-items:center; gap:1rem;">
        <c:choose>
          <c:when test="${not empty usuario.fotoPerfil}">
            <img src="${pageContext.request.contextPath}/uploads/${usuario.fotoPerfil}"
                 alt="Foto atual"
                 style="width:64px;height:64px;border-radius:50%;object-fit:cover;">
          </c:when>
          <c:otherwise>
            <div style="width:64px;height:64px;border-radius:50%;background:#1da1f2;
                        display:flex;align-items:center;justify-content:center;
                        color:white;font-size:1.6rem;font-weight:bold;">
              ${usuario.username[0]}
            </div>
          </c:otherwise>
        </c:choose>
        <div>
          <label>Foto de perfil</label>
          <input type="file" name="fotoPerfil" accept="image/*">
          <small style="color:#888;">Deixe vazio para manter a atual</small>
        </div>
      </div>

      <div class="form-group">
        <label for="nome">Nome</label>
        <input type="text" id="nome" name="nome"
               value="${usuario.nome}" required maxlength="100">
      </div>

      <div class="form-group">
        <label for="bio">Bio</label>
        <textarea id="bio" name="bio" rows="3" maxlength="200"
                  placeholder="Conte um pouco sobre você...">${usuario.bio}</textarea>
      </div>

      <div style="display:flex; gap:0.8rem;">
        <button type="submit" class="btn">Salvar</button>
        <a href="${pageContext.request.contextPath}/perfil"
           class="btn btn-secondary" style="text-decoration:none;">Cancelar</a>
      </div>

    </form>
  </div>
</div>

<%@ include file="../footer.jsp" %>
