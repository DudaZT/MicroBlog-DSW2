<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../header.jsp" %>

<div class="container" style="max-width:400px;">
  <div class="card" style="margin-top:3rem;">
    <h2 style="margin-bottom:1.2rem; text-align:center;">Entrar no Microblog</h2>

    <c:if test="${not empty erro}">
      <div class="alert-erro">${erro}</div>
    </c:if>
    <c:if test="${not empty sessionScope.erro}">
      <div class="alert-erro">${sessionScope.erro}</div>
      <c:remove var="erro" scope="session"/>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/auth/login">
      <div class="form-group">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required autofocus>
      </div>
      <div class="form-group">
        <label for="senha">Senha</label>
        <input type="password" id="senha" name="senha" required>
      </div>
      <button type="submit" class="btn" style="width:100%;">Entrar</button>
    </form>

    <p style="margin-top:1rem; text-align:center;">
      Não tem conta? <a href="${pageContext.request.contextPath}/auth/cadastro">Cadastre-se</a>
    </p>
  </div>
</div>

<%@ include file="../footer.jsp" %>
