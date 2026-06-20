<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="edu.ifsp.microblog.modelo.Usuario" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Microblog</title>
  <!-- Bootstrap 5 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <!-- Bootstrap Icons -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
  <!-- Google Fonts (Inter) -->
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Inter', sans-serif;
      background-color: #f3f4f6;
      color: #1f2937;
    }
    .navbar {
      background-color: #1da1f2 !important;
    }
    .avatar-placeholder {
      background-color: #1da1f2;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
    }
    .card {
      border: none;
      border-radius: 12px;
      box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
    }
    .btn-primary {
      background-color: #1da1f2;
      border-color: #1da1f2;
    }
    .btn-primary:hover, .btn-primary:focus {
      background-color: #1a91da;
      border-color: #1a91da;
    }
    .text-primary-color {
      color: #1da1f2 !important;
    }
  </style>
</head>
<body>

<%
  Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
%>

<nav class="navbar navbar-expand-md navbar-dark shadow-sm py-2 mb-4">
  <div class="container">
    <a class="navbar-brand d-flex align-items-center fw-bold fs-4" href="<%= request.getContextPath() %>/feed">
      <i class="bi bi-chat-text-fill me-2 fs-3 text-white"></i>
      <span>Microblog</span>
    </a>

    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse justify-content-end" id="navbarContent">
      <div class="navbar-nav align-items-md-center">
        <% if (usuarioLogado != null) { %>
           <a class="nav-link text-white fw-semibold me-3 d-flex align-items-center" href="<%= request.getContextPath() %>/perfil">
            <% if (usuarioLogado.getFotoPerfil() != null && !usuarioLogado.getFotoPerfil().isBlank()) { %>
              <img src="<%= request.getContextPath() %>/uploads/<%= usuarioLogado.getFotoPerfil() %>" 
                   alt="Foto" 
                   class="rounded-circle me-2" 
                   style="width: 28px; height: 28px; object-fit: cover; border: 2px solid rgba(255,255,255,0.5);">
            <% } else { %>
              <i class="bi bi-person-circle me-1 fs-5"></i>
            <% } %>
            @<%= usuarioLogado.getUsername() %>
          </a>
          <a class="btn btn-light btn-sm fw-semibold text-primary-color px-3" href="<%= request.getContextPath() %>/auth/logout">
            <i class="bi bi-box-arrow-right me-1"></i>Sair
          </a>
        <% } else { %>
          <a class="nav-link text-white fw-semibold me-3 d-flex align-items-center" href="<%= request.getContextPath() %>/auth/login">Entrar</a>
          <a class="btn btn-light btn-sm fw-semibold text-primary-color px-3" href="<%= request.getContextPath() %>/auth/cadastro">Cadastrar</a>
        <% } %>
      </div>
    </div>
  </div>
</nav>