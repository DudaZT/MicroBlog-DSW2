<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="edu.ifsp.microblog.modelo.Post" %>
<%@ page import="edu.ifsp.microblog.modelo.Usuario" %>
<%@ page import="edu.ifsp.microblog.util.Paginacao" %>
<%@ include file="../header.jsp" %>

<%
  String sucessoSessao = (String) session.getAttribute("sucesso");
  if (sucessoSessao != null) session.removeAttribute("sucesso");

  String erroSessao = (String) session.getAttribute("erro");
  if (erroSessao != null) session.removeAttribute("erro");

  Usuario perfil = (Usuario) request.getAttribute("perfil");
  List<Post> posts = (List<Post>) request.getAttribute("posts");
  java.util.Map<Integer, Integer> curtidasPorPost = (java.util.Map<Integer, Integer>) request.getAttribute("curtidasPorPost");
  java.util.Set<Integer> postsCurtidos = (java.util.Set<Integer>) request.getAttribute("postsCurtidos");
  Paginacao paginacao = (Paginacao) request.getAttribute("paginacao");
  String aba = (String) request.getAttribute("aba");
  boolean isSeguindo = (Boolean) request.getAttribute("isSeguindo");
  int seguidores = (Integer) request.getAttribute("seguidores");
  int seguindo = (Integer) request.getAttribute("seguindo");
  boolean isProprioUser = (Boolean) request.getAttribute("isProprioUser");

  DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  String ctx = request.getContextPath();
%>

<div class="container py-4">
  <%-- Alertas de sessão --%>
  <% if (sucessoSessao != null && !sucessoSessao.isBlank()) { %>
    <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
      <i class="bi bi-check-circle-fill me-2"></i><%= sucessoSessao %>
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  <% } %>
  <% if (erroSessao != null && !erroSessao.isBlank()) { %>
    <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
      <i class="bi bi-exclamation-triangle-fill me-2"></i><%= erroSessao %>
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  <% } %>

  <%-- Cabeçalho do perfil --%>
  <div class="card p-4 mb-4">
    <div class="d-flex align-items-center flex-wrap gap-4">
      <%-- Foto de perfil --%>
      <div>
        <% if (perfil.getFotoPerfil() != null && !perfil.getFotoPerfil().isBlank()) { %>
          <img src="<%= ctx %>/uploads/<%= perfil.getFotoPerfil() %>"
               alt="Foto de <%= perfil.getNome() %>"
               class="rounded-circle border border-3 border-light shadow-sm"
               style="width: 100px; height: 100px; object-fit: cover;">
        <% } else { %>
          <div class="avatar-placeholder rounded-circle border border-3 border-light shadow-sm"
               style="width: 100px; height: 100px; font-size: 2.5rem;">
            <%= perfil.getUsername().substring(0, 1).toUpperCase() %>
          </div>
        <% } %>
      </div>

      <%-- Informações do Usuário --%>
      <div class="flex-grow-1">
        <h2 class="fw-bold mb-1"><%= perfil.getNome() %></h2>
        <p class="text-muted mb-2">@<%= perfil.getUsername() %></p>

        <% if (perfil.getBio() != null && !perfil.getBio().isBlank()) { %>
          <p class="mb-3 fs-5"><%= perfil.getBio() %></p>
        <% } %>

        <div class="d-flex gap-4">
          <div>
            <span class="fw-bold text-dark fs-5"><%= seguidores %></span>
            <span class="text-muted small ms-1">seguidores</span>
          </div>
          <div>
            <% if (isProprioUser) { %>
              <a href="<%= ctx %>/perfil/seguindo" class="text-decoration-none">
                <span class="fw-bold text-dark fs-5"><%= seguindo %></span>
                <span class="text-muted small ms-1">seguindo</span>
              </a>
            <% } else { %>
              <span class="fw-bold text-dark fs-5"><%= seguindo %></span>
              <span class="text-muted small ms-1">seguindo</span>
            <% } %>
          </div>
        </div>
      </div>

      <%-- Botões de Ação --%>
      <div class="ms-md-auto align-self-md-start">
        <% if (isProprioUser) { %>
          <a href="<%= ctx %>/perfil/editar" class="btn btn-outline-secondary fw-semibold rounded-pill px-4">
            <i class="bi bi-pencil-square me-1"></i>Editar perfil
          </a>
        <% } else { %>
          <form method="post" action="<%= ctx %>/seguir">
            <input type="hidden" name="seguidoId" value="<%= perfil.getId() %>">
            <input type="hidden" name="username" value="<%= perfil.getUsername() %>">
            <% if (isSeguindo) { %>
              <button type="submit" class="btn btn-secondary fw-semibold rounded-pill px-4">
                Deixar de seguir
              </button>
            <% } else { %>
              <button type="submit" class="btn btn-primary fw-semibold rounded-pill px-4">
                <i class="bi bi-person-plus-fill me-1"></i>Seguir
              </button>
            <% } %>
          </form>
        <% } %>
      </div>
    </div>
  </div>

  <%-- Abas: Posts / Curtidas --%>
  <ul class="nav nav-tabs mb-4 border-bottom-2">
    <li class="nav-item">
      <a class="nav-link fw-bold px-4 py-2 <%= "posts".equals(aba) ? "active text-primary" : "text-secondary" %>"
         href="<%= ctx %>/perfil?username=<%= perfil.getUsername() %>&aba=posts">
        <i class="bi bi-grid-3x3-gap me-2"></i>Posts
      </a>
    </li>
    <li class="nav-item">
      <a class="nav-link fw-bold px-4 py-2 <%= "curtidas".equals(aba) ? "active text-danger" : "text-secondary" %>"
         href="<%= ctx %>/perfil?username=<%= perfil.getUsername() %>&aba=curtidas">
        <i class="bi bi-heart-fill me-2"></i>Curtidas
      </a>
    </li>
  </ul>

  <%-- Lista de posts --%>
  <% if (posts == null || posts.isEmpty()) { %>
    <div class="card p-5 text-center text-muted">
      <i class="bi bi-chat-dots-fill fs-1 mb-3 text-secondary"></i>
      <p class="mb-0">
        <%= "curtidas".equals(aba) ? "Nenhum post curtido ainda." : "Nenhuma publicação ainda." %>
      </p>
    </div>
  <% } else {
       for (Post post : posts) {
         Usuario autor = post.getAutor();
  %>
    <div class="card p-4 mb-3">
      <div class="d-flex align-items-center justify-content-between mb-3 flex-wrap gap-2">
        <div class="d-flex align-items-center">
          <% if (autor.getFotoPerfil() != null && !autor.getFotoPerfil().isBlank()) { %>
            <img src="<%= ctx %>/uploads/<%= autor.getFotoPerfil() %>"
                 alt="Foto" class="rounded-circle me-3" style="width: 48px; height: 48px; object-fit: cover;">
          <% } else { %>
            <div class="avatar-placeholder rounded-circle me-3" style="width: 48px; height: 48px; font-size: 1.2rem;">
              <%= autor.getUsername().substring(0, 1).toUpperCase() %>
            </div>
          <% } %>
          <div>
            <a href="<%= ctx %>/perfil?username=<%= autor.getUsername() %>"
               class="fw-bold text-dark text-decoration-none hover-underline d-block"><%= autor.getNome() %></a>
            <span class="text-muted small">@<%= autor.getUsername() %></span>
          </div>
        </div>
        <small class="text-muted"><i class="bi bi-clock me-1"></i><%= post.getDtCriacao() != null ? post.getDtCriacao().format(dtFormatter) : "" %></small>
      </div>

      <p class="fs-5 mb-3" style="white-space: pre-wrap; word-break: break-word;"><%= post.getConteudo() %></p>

      <% if (post.getImagem() != null && !post.getImagem().isBlank()) { %>
        <div class="mb-3">
          <img src="<%= ctx %>/uploads/<%= post.getImagem() %>" alt="Imagem" class="img-fluid rounded-3 w-100" style="max-height: 400px; object-fit: cover;">
        </div>
      <% } %>

      <div class="d-flex align-items-center gap-3">
        <form method="post" action="<%= ctx %>/post/curtir">
          <input type="hidden" name="postId" value="<%= post.getId() %>">
          <input type="hidden" name="origem" value="perfil">
          <input type="hidden" name="username" value="<%= perfil.getUsername() %>">
          <%
            boolean curtido = postsCurtidos.contains(post.getId());
            int totalCurtidas = curtidasPorPost.getOrDefault(post.getId(), 0);
          %>
          <button type="submit" class="btn btn-link <%= curtido ? "text-danger" : "text-muted" %> text-decoration-none p-0 d-flex align-items-center gap-1">
            <i class="bi <%= curtido ? "bi-heart-fill" : "bi-heart" %> fs-5"></i>
            <span class="fw-medium"><%= totalCurtidas %></span>
          </button>
        </form>

        <%-- Botão excluir (apenas para o dono do post) --%>
        <% if (usuarioLogado != null && isProprioUser) { %>
          <form method="post" action="<%= ctx %>/post/excluir" 
                onsubmit="return confirm('Tem certeza que deseja excluir este post?');">
            <input type="hidden" name="postId" value="<%= post.getId() %>">
            <input type="hidden" name="origem" value="perfil">
            <button type="submit" class="btn btn-link text-muted text-decoration-none p-0 d-flex align-items-center gap-1"
                    title="Excluir post">
              <i class="bi bi-trash fs-5"></i>
            </button>
          </form>
        <% } %>
      </div>
    </div>
  <%
       }
     }
  %>

  <%-- Paginação --%>
  <% if (paginacao.getTotalPaginas() > 1) { %>
    <nav class="mt-4">
      <ul class="pagination justify-content-center">
        <li class="page-item <%= !paginacao.temAnterior() ? "disabled" : "" %>">
          <a class="page-link" href="<%= ctx %>/perfil?username=<%= perfil.getUsername() %>&aba=<%= aba %>&page=<%= paginacao.anterior() %>"><i class="bi bi-chevron-left"></i> Anterior</a>
        </li>
        <li class="page-item disabled">
          <span class="page-link text-dark bg-white">Página <%= paginacao.getPaginaAtual() %> de <%= paginacao.getTotalPaginas() %></span>
        </li>
        <li class="page-item <%= !paginacao.temProxima() ? "disabled" : "" %>">
          <a class="page-link" href="<%= ctx %>/perfil?username=<%= perfil.getUsername() %>&aba=<%= aba %>&page=<%= paginacao.proxima() %>">Próxima <i class="bi bi-chevron-right"></i></a>
        </li>
      </ul>
    </nav>
  <% } %>
</div>

<%@ include file="../footer.jsp" %>