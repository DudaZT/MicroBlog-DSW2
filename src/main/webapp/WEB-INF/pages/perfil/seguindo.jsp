<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="edu.ifsp.microblog.modelo.Usuario" %>
<%@ include file="../header.jsp" %>

<%
  List<Usuario> seguindoLista = (List<Usuario>) request.getAttribute("seguindoLista");
  String ctx = request.getContextPath();
%>

<div class="container py-4">
  <div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
      <div class="card p-4">
        <h2 class="fw-bold mb-4 d-flex align-items-center">
          <i class="bi bi-people-fill text-primary-color me-2 fs-3"></i>
          <span>Quem você segue</span>
        </h2>

        <% if (seguindoLista == null || seguindoLista.isEmpty()) { %>
          <p class="text-muted text-center py-4 mb-0">Você ainda não segue ninguém.</p>
        <% } else { %>
          <div class="d-flex flex-column gap-3">
            <% for (Usuario u : seguindoLista) { %>
              <div class="d-flex align-items-center justify-content-between py-2 border-bottom border-light">
                <div class="d-flex align-items-center">
                  <% if (u.getFotoPerfil() != null && !u.getFotoPerfil().isBlank()) { %>
                    <img src="<%= ctx %>/uploads/<%= u.getFotoPerfil() %>" alt="Foto de <%= u.getNome() %>" class="rounded-circle me-3" style="width: 48px; height: 48px; object-fit: cover;">
                  <% } else { %>
                    <div class="avatar-placeholder rounded-circle me-3" style="width: 48px; height: 48px; font-size: 1.2rem;">
                      <%= u.getUsername().substring(0, 1).toUpperCase() %>
                    </div>
                  <% } %>
                  <div>
                    <a href="<%= ctx %>/perfil?username=<%= u.getUsername() %>" class="fw-bold text-dark text-decoration-none hover-underline d-block"><%= u.getNome() %></a>
                    <span class="text-muted small">@<%= u.getUsername() %></span>
                  </div>
                </div>
                <form method="post" action="<%= ctx %>/seguir">
                  <input type="hidden" name="seguidoId" value="<%= u.getId() %>">
                  <input type="hidden" name="username" value="<%= u.getUsername() %>">
                  <input type="hidden" name="origem" value="seguindo">
                  <button type="submit" class="btn btn-outline-secondary btn-sm rounded-pill px-3 fw-bold">Deixar de seguir</button>
                </form>
              </div>
            <% } %>
          </div>
        <% } %>
      </div>
    </div>
  </div>
</div>

<%@ include file="../footer.jsp" %>