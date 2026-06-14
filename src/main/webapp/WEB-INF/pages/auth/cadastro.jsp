<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>

<div class="container" style="max-width:400px;">
  <div class="card" style="margin-top:3rem;">
    <h2 style="margin-bottom:1.2rem; text-align:center;">Criar Conta</h2>

    <c:if test="${not empty erro}">
      <div class="alert-erro">${erro}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/cadastro">
      <div class="form-group">
        <label for="nome">Nome completo</label>
        <input type="text" id="nome" name="nome" required>
      </div>
      <div class="form-group">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required pattern="[a-zA-Z0-9_]{3,50}"
               title="Apenas letras, números e _ (3–50 caracteres)">
      </div>
      <div class="form-group">
        <label for="email">E-mail</label>
        <input type="email" id="email" name="email" required>
      </div>
      <div class="form-group">
        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" required minlength="6">
      </div>
      <button type="submit" class="btn" style="width:100%;">Criar conta</button>
    </form>

    <p style="margin-top:1rem; text-align:center;">
      Já tem conta? <a href="${pageContext.request.contextPath}/auth/login">Entrar</a>
    </p>
  </div>
</div>

<%@ include file="../footer.jsp" %>
