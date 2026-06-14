package edu.ifsp.microblog.controllers.auth;

import java.io.IOException;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.UsuarioService;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Gerencia login (GET → formulário, POST → autenticar).
public class LoginController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Se já logado, redireciona para o feed
        if (Auth.isLogado(req)) {
            ViewHelper.redirect(req, res, "/feed");
            
            return;
        }
        
        ViewHelper.forward(req, res, "auth/login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String senha = req.getParameter("senha");

        Usuario usuario = usuarioService.autenticar(username, senha);

        if (usuario == null) {
            req.setAttribute("erro", "Usuário ou senha inválidos.");
            ViewHelper.forward(req, res, "auth/login");
            return;
        }

        Auth.login(req, usuario);
        ViewHelper.redirect(req, res, "/feed");
    }
}