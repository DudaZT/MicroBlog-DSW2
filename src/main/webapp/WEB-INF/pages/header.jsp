<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Microblog</title>
  <style>
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body { font-family: Arial, sans-serif; background: #f0f2f5; color: #333; }

    /* Navbar */
    nav {
      background: #1da1f2;
      padding: 0.8rem 2rem;
      display: flex;
      align-items: center;
      justify-content: space-between;
      color: white;
    }
    nav a { color: white; text-decoration: none; font-weight: bold; margin-left: 1rem; }
    nav a:hover { text-decoration: underline; }

    /* Layout principal */
    .container { max-width: 600px; margin: 2rem auto; padding: 0 1rem; }

    /* Cards */
    .card {
      background: white;
      border-radius: 8px;
      padding: 1.2rem;
      margin-bottom: 1rem;
      box-shadow: 0 1px 3px rgba(0,0,0,0.1);
    }

    /* Formulários */
    .form-group { margin-bottom: 1rem; }
    label { display: block; font-weight: bold; margin-bottom: 0.3rem; }
    input[type=text], input[type=email], input[type=password], textarea, select {
      width: 100%; padding: 0.6rem; border: 1px solid #ccc;
      border-radius: 4px; font-size: 1rem;
    }
    textarea { resize: vertical; }
    .btn {
      background: #1da1f2; color: white; border: none; padding: 0.6rem 1.2rem;
      border-radius: 4px; cursor: pointer; font-size: 1rem; font-weight: bold;
    }
    .btn:hover { background: #0d8ecf; }
    .btn-danger { background: #e0245e; }
    .btn-danger:hover { background: #b8103f; }
    .btn-secondary { background: #aaa; }

    /* Alertas */
    .alert-erro    { background: #fce4ec; color: #c62828; padding: 0.8rem; border-radius: 4px; margin-bottom: 1rem; }
    .alert-sucesso { background: #e8f5e9; color: #2e7d32; padding: 0.8rem; border-radius: 4px; margin-bottom: 1rem; }

    /* Post */
    .post-header { display: flex; align-items: center; gap: 0.5rem; margin-bottom: 0.5rem; }
    .post-header img { width: 40px; height: 40px; border-radius: 50%; object-fit: cover; background: #ddd; }
    .post-autor { font-weight: bold; }
    .post-username { color: #666; font-size: 0.9rem; }
    .post-conteudo { margin: 0.5rem 0; line-height: 1.5; }
    .post-imagem img { max-width: 100%; border-radius: 8px; margin-top: 0.5rem; }
    .post-actions { display: flex; gap: 1rem; margin-top: 0.7rem; font-size: 0.9rem; color: #666; }
    .post-actions form { display: inline; }
    .btn-like { background: none; border: none; cursor: pointer; color: #e0245e; font-size: 0.9rem; }
    .btn-like:hover { text-decoration: underline; }

    /* Paginação */
    .paginacao { display: flex; gap: 0.5rem; justify-content: center; margin-top: 1rem; }
    .paginacao a { padding: 0.4rem 0.8rem; background: white; border: 1px solid #ccc; border-radius: 4px; color: #333; text-decoration: none; }
    .paginacao a:hover { background: #eee; }
  </style>
</head>
<body>
<nav>
  <a href="${pageContext.request.contextPath}/feed" style="font-size:1.3rem;">🐦 Microblog</a>
  <div>
    <c:if test="${not empty sessionScope.usuarioLogado}">
      <a href="${pageContext.request.contextPath}/perfil">@${sessionScope.usuarioLogado.username}</a>
      <a href="${pageContext.request.contextPath}/auth/logout">Sair</a>
    </c:if>
    <c:if test="${empty sessionScope.usuarioLogado}">
      <a href="${pageContext.request.contextPath}/auth/login">Entrar</a>
      <a href="${pageContext.request.contextPath}/auth/cadastro">Cadastrar</a>
    </c:if>
  </div>
</nav>
