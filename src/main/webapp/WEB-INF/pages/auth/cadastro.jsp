<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>

<%
  String erro = (String) request.getAttribute("erro");
%>

<div class="container py-5">
  <div class="row justify-content-center">
    <div class="col-12 col-sm-10 col-md-8 col-lg-5">
      <div class="card p-4 shadow-sm border-0">
        <div class="text-center mb-4">
          <i class="bi bi-chat-text-fill text-primary-color fs-1"></i>
          <h2 class="fw-bold mt-2">Criar Conta</h2>
          <p class="text-muted">Junte-se à comunidade do Microblog</p>
        </div>

        <% if (erro != null && !erro.isBlank()) { %>
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><%= erro %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/auth/cadastro">
          <div class="mb-3">
            <label for="nome" class="form-label fw-semibold">Nome completo</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><i class="bi bi-card-text text-muted"></i></span>
              <input type="text" id="nome" name="nome" class="form-control border-start-0 bg-light" required placeholder="Seu nome completo">
            </div>
          </div>

          <div class="mb-3">
            <label for="username" class="form-label fw-semibold">Username</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><span class="text-muted fw-bold">@</span></span>
              <input type="text" id="username" name="username" class="form-control border-start-0 bg-light" required pattern="[a-zA-Z0-9_]{3,50}"
                     title="Apenas letras, números e _ (3–50 caracteres)" placeholder="usuario">
            </div>
            <div class="form-text text-muted small">Mínimo 3 caracteres (apenas letras, números e sublinhado).</div>
          </div>

          <div class="mb-3">
            <label for="email" class="form-label fw-semibold">E-mail</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><i class="bi bi-envelope-fill text-muted"></i></span>
              <input type="email" id="email" name="email" class="form-control border-start-0 bg-light" required placeholder="seu@email.com">
            </div>
          </div>

          <div class="mb-4">
            <label for="senha" class="form-label fw-semibold">Senha</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock-fill text-muted"></i></span>
              <input type="password" id="senha" name="senha" class="form-control border-start-0 bg-light" required minlength="6" placeholder="Mínimo 6 caracteres">
            </div>
          </div>

          <button type="submit" class="btn btn-primary w-100 fw-bold py-2 rounded-pill fs-5">Criar conta</button>
        </form>

        <hr class="text-muted my-4">

        <p class="text-center mb-0">
          Já tem conta? <a href="<%= request.getContextPath() %>/auth/login" class="text-primary-color fw-bold text-decoration-none hover-underline">Entrar</a>
        </p>
      </div>
    </div>
  </div>
</div>

<%@ include file="../footer.jsp" %>