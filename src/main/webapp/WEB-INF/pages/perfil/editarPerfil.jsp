<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.ifsp.microblog.modelo.Usuario" %>
<%@ include file="../header.jsp" %>

<%
  String erro = (String) request.getAttribute("erro");
  Usuario usuario = (Usuario) request.getAttribute("usuario");
  String ctx = request.getContextPath();
%>

<div class="container py-4">
  <div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
      <div class="card p-4 shadow-sm">
        <h2 class="fw-bold mb-4 d-flex align-items-center">
          <i class="bi bi-person-gear text-primary-color me-2 fs-3"></i>
          <span>Editar Perfil</span>
        </h2>

        <% if (erro != null && !erro.isBlank()) { %>
          <div class="alert alert-danger alert-dismissible fade show mb-3" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><%= erro %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        <% } %>

        <form method="post" action="<%= ctx %>/perfil/editar" enctype="multipart/form-data">
          <%-- Foto atual --%>
          <div class="d-flex align-items-center gap-3 mb-4 p-3 bg-light rounded-3">
            <% if (usuario.getFotoPerfil() != null && !usuario.getFotoPerfil().isBlank()) { %>
              <img src="<%= ctx %>/uploads/<%= usuario.getFotoPerfil() %>"
                   alt="Foto atual"
                   class="rounded-circle border border-2 border-white shadow-sm"
                   style="width: 72px; height: 72px; object-fit: cover;">
            <% } else { %>
              <div class="avatar-placeholder rounded-circle border border-2 border-white shadow-sm"
                   style="width: 72px; height: 72px; font-size: 1.8rem;">
                <%= usuario.getUsername().substring(0, 1).toUpperCase() %>
              </div>
            <% } %>
            <div class="flex-grow-1">
              <label class="form-label fw-semibold mb-1">Foto de perfil</label>
              <input type="file" name="fotoPerfil" accept="image/*" class="form-control form-control-sm">
              <div class="form-text text-muted small mt-1">Deixe vazio para manter a foto atual.</div>
            </div>
          </div>

          <%-- Nome --%>
          <div class="mb-3">
            <label for="nome" class="form-label fw-semibold">Nome</label>
            <input type="text" id="nome" name="nome" class="form-control" value="<%= usuario.getNome() %>" required maxlength="100">
          </div>

          <%-- Bio --%>
          <div class="mb-4">
            <label for="bio" class="form-label fw-semibold">Bio</label>
            <textarea id="bio" name="bio" class="form-control" rows="3" maxlength="200" style="resize: none;" placeholder="Conte um pouco sobre você..."><%= usuario.getBio() != null ? usuario.getBio() : "" %></textarea>
          </div>

          <%-- Ações --%>
          <div class="d-flex gap-2 justify-content-end">
            <a href="<%= ctx %>/perfil" class="btn btn-outline-secondary px-4 rounded-pill">Cancelar</a>
            <button type="submit" class="btn btn-primary px-4 rounded-pill">Salvar Alterações</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<%@ include file="../footer.jsp" %>