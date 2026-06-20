<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="edu.ifsp.microblog.modelo.Post" %>
<%@ page import="edu.ifsp.microblog.modelo.Usuario" %>
<%@ include file="../header.jsp" %>

<%
  String erroSessao = (String) session.getAttribute("erro");
  if (erroSessao != null) session.removeAttribute("erro");

  String sucessoSessao = (String) session.getAttribute("sucesso");
  if (sucessoSessao != null) session.removeAttribute("sucesso");

  List<Post> posts = (List<Post>) request.getAttribute("posts");
  java.util.Map<Integer, Integer> curtidasPorPost = (java.util.Map<Integer, Integer>) request.getAttribute("curtidasPorPost");
  java.util.Set<Integer> postsCurtidos = (java.util.Set<Integer>) request.getAttribute("postsCurtidos");
  
  int paginaAtual = (Integer) request.getAttribute("page"); 
  boolean temProxima = (Boolean) request.getAttribute("temProxima");
  List<Usuario> sugestoes = (List<Usuario>) request.getAttribute("sugestoes");

  DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
  String ctx = request.getContextPath();
  
  
%>

<div class="container py-4">
  <div class="row">
    <!-- Coluna do Feed (Esquerda) -->
    <div class="col-lg-8 col-md-12 mb-4">
      <%-- Alertas de sessão --%>
      <% if (erroSessao != null && !erroSessao.isBlank()) { %>
        <div class="alert alert-danger alert-dismissible fade show mb-4" role="alert">
          <i class="bi bi-exclamation-triangle-fill me-2"></i><%= erroSessao %>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <% } %>
      <% if (sucessoSessao != null && !sucessoSessao.isBlank()) { %>
        <div class="alert alert-success alert-dismissible fade show mb-4" role="alert">
          <i class="bi bi-check-circle-fill me-2"></i><%= sucessoSessao %>
          <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
      <% } %>

      <%-- Caixa de novo post --%>
      <div class="card p-4 mb-4">
        <form method="post" action="<%= ctx %>/post/criar" enctype="multipart/form-data">
          <div class="mb-3">
            <textarea name="conteudo" class="form-control border-light-subtle shadow-sm" rows="3" maxlength="280" placeholder="O que está acontecendo?" required style="resize: none; border-radius: 8px;"></textarea>
          </div>
          <div class="d-flex align-items-center justify-content-between flex-wrap gap-2">
            <div class="input-group input-group-sm w-auto flex-grow-1" style="max-width: 320px;">
              <label class="input-group-text bg-white text-muted border-end-0" style="border-radius: 8px 0 0 8px;"><i class="bi bi-image"></i></label>
              <input type="file" name="imagem" accept="image/jpeg, image/png, image/webp" class="form-control border-start-0" style="border-radius: 0 8px 8px 0;">
            </div>
            <button type="submit" class="btn btn-primary fw-semibold px-4 py-2" style="border-radius: 20px;">Postar</button>
          </div>
        </form>
      </div>

      <%-- Lista de posts --%>
      <% if (posts == null || posts.isEmpty()) { %>
        <div class="card p-5 text-center text-muted">
          <i class="bi bi-chat-dots fs-1 mb-3 text-secondary"></i>
          <p class="mb-0">Nenhum post no feed ainda. Siga outros usuários ou publique algo!</p>
        </div>
      <% } else {
           for (Post post : posts) {
             Usuario autor = post.getAutor();
      %>
        <div class="card p-4 mb-3">
          <div class="d-flex align-items-center justify-content-between mb-3 flex-wrap gap-2">
            <div class="d-flex align-items-center">
              <% if (autor.getFotoPerfil() != null && !autor.getFotoPerfil().isBlank()) { %>
                <img src="<%= ctx %>/uploads/<%= autor.getFotoPerfil() %>" alt="Foto de <%= autor.getNome() %>" class="rounded-circle me-3" style="width: 48px; height: 48px; object-fit: cover;">
              <% } else { %>
                <div class="avatar-placeholder rounded-circle me-3" style="width: 48px; height: 48px; font-size: 1.2rem;">
                  <%= autor.getUsername().substring(0, 1).toUpperCase() %>
                </div>
              <% } %>
              <div>
                <a href="<%= ctx %>/perfil?username=<%= autor.getUsername() %>" class="fw-bold text-dark text-decoration-none hover-underline d-block"><%= autor.getNome() %></a>
                <span class="text-muted small">@<%= autor.getUsername() %></span>
              </div>
            </div>
            <small class="text-muted"><i class="bi bi-clock me-1"></i><%= post.getDtCriacao() != null ? post.getDtCriacao().format(dtFormatter) : "" %></small>
          </div>

          <p class="fs-5 mb-3" style="white-space: pre-wrap; word-break: break-word;"><%= post.getConteudo() %></p>

          <% if (post.getImagem() != null && !post.getImagem().isBlank()) { %>
            <div class="mb-3">
              <img src="<%= ctx %>/uploads/<%= post.getImagem() %>" alt="Imagem do post" class="img-fluid rounded-3 w-100" style="max-height: 400px; object-fit: cover;">
            </div>
          <% } %>

           <div class="d-flex align-items-center gap-3">
            <form method="post" action="<%= ctx %>/post/curtir">
              <input type="hidden" name="postId" value="<%= post.getId() %>">
              <input type="hidden" name="origem" value="feed">
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
            <% if (usuarioLogado != null && usuarioLogado.getId() == autor.getId()) { %>
              <form method="post" action="<%= ctx %>/post/excluir" 
                    onsubmit="return confirm('Tem certeza que deseja excluir este post?');">
                <input type="hidden" name="postId" value="<%= post.getId() %>">
                <input type="hidden" name="origem" value="feed">
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
      <% if (paginaAtual > 1 || temProxima) { %>
		  <nav class="mt-4">
		    <ul class="pagination justify-content-center">
		      <li class="page-item <%= paginaAtual <= 1 ? "disabled" : "" %>">
		        <a class="page-link" href="<%= ctx %>/feed?page=<%= paginaAtual - 1 %>"><i class="bi bi-chevron-left"></i> Anterior</a>
		      </li>
		      <li class="page-item disabled">
		        <span class="page-link text-dark bg-white">Página <%= paginaAtual %></span>
		      </li>
		      <li class="page-item <%= !temProxima ? "disabled" : "" %>">
		        <a class="page-link" href="<%= ctx %>/feed?page=<%= paginaAtual + 1 %>">Próxima <i class="bi bi-chevron-right"></i></a>
		      </li>
		    </ul>
		  </nav>
		<% } %>
    </div>

    <!-- Coluna Lateral (Direita) - Talvez você conheça -->
    <div class="col-lg-4 col-md-12">
      <div class="card p-4">
        <h5 class="fw-bold mb-3 d-flex align-items-center">
          <i class="bi bi-people-fill text-primary-color me-2 fs-4"></i>
          <span>Talvez você conheça</span>
        </h5>

        <% if (sugestoes == null || sugestoes.isEmpty()) { %>
          <p class="text-muted small mb-0 text-center py-3">Não há sugestões de perfis disponíveis no momento.</p>
        <% } else { %>
          <div class="d-flex flex-column gap-3">
            <% for (Usuario sugestao : sugestoes) { %>
              <div class="d-flex align-items-center justify-content-between py-2 border-bottom border-light">
                <div class="d-flex align-items-center">
                  <% if (sugestao.getFotoPerfil() != null && !sugestao.getFotoPerfil().isBlank()) { %>
                    <img src="<%= ctx %>/uploads/<%= sugestao.getFotoPerfil() %>" alt="Foto de <%= sugestao.getNome() %>" class="rounded-circle me-3" style="width: 40px; height: 40px; object-fit: cover;">
                  <% } else { %>
                    <div class="avatar-placeholder rounded-circle me-3" style="width: 40px; height: 40px; font-size: 1rem;">
                      <%= sugestao.getUsername().substring(0, 1).toUpperCase() %>
                    </div>
                  <% } %>
                  <div style="max-width: 140px;">
                    <a href="<%= ctx %>/perfil?username=<%= sugestao.getUsername() %>" class="fw-bold text-dark text-decoration-none hover-underline text-truncate d-block small"><%= sugestao.getNome() %></a>
                    <span class="text-muted small text-truncate d-block">@<%= sugestao.getUsername() %></span>
                  </div>
                </div>
                <form method="post" action="<%= ctx %>/seguir">
                  <input type="hidden" name="seguidoId" value="<%= sugestao.getId() %>">
                  <input type="hidden" name="username" value="<%= sugestao.getUsername() %>">
                  <input type="hidden" name="origem" value="feed">
                  <button type="submit" class="btn btn-outline-primary btn-sm rounded-pill px-3 fw-bold">Seguir</button>
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