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
          <h2 class="fw-bold mt-2">Entrar no Microblog</h2>
          <p class="text-muted">Conecte-se para ver o que está acontecendo</p>
        </div>

        <% if (erro != null && !erro.isBlank()) { %>
          <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><%= erro %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
          </div>
        <% } %>

        <form method="post" action="<%= request.getContextPath() %>/auth/login">
          <div class="mb-3">
            <label for="username" class="form-label fw-semibold">Username</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><i class="bi bi-person-fill text-muted"></i></span>
              <input type="text" id="username" name="username" class="form-control border-start-0 bg-light" required autofocus placeholder="seu_username">
            </div>
          </div>
          <div class="mb-4">
            <label for="senha" class="form-label fw-semibold">Senha</label>
            <div class="input-group">
              <span class="input-group-text bg-light border-end-0"><i class="bi bi-lock-fill text-muted"></i></span>
              <input type="password" id="senha" name="senha" class="form-control border-start-0 bg-light" required placeholder="Sua senha">
            </div>
          </div>
          <button type="submit" class="btn btn-primary w-100 fw-bold py-2 rounded-pill fs-5">Entrar</button>
        </form>

        <hr class="text-muted my-4">

        <p class="text-center mb-0">
          Não tem conta? <a href="<%= request.getContextPath() %>/auth/cadastro" class="text-primary-color fw-bold text-decoration-none hover-underline">Cadastre-se</a>
        </p>
      </div>
    </div>
  </div>
</div>

<%@ include file="../footer.jsp" %>