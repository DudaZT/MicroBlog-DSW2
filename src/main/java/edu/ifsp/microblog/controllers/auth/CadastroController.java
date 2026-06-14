package edu.ifsp.microblog.controllers.auth;

import java.io.IOException;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.ServiceException;
import edu.ifsp.microblog.service.UsuarioService;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CadastroController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (Auth.isLogado(req)) {
            ViewHelper.redirect(req, res, "/feed");
            
            return;
        }
        
        ViewHelper.forward(req, res, "auth/cadastro");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String nome = req.getParameter("nome");
        String senha = req.getParameter("senha");

        try {
            Usuario novo = usuarioService.cadastrar(username, email, nome, senha);
            Auth.login(req, novo);
            ViewHelper.redirect(req, res, "/feed");
        } catch (ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            ViewHelper.forward(req, res, "auth/cadastro");
        }
    }
}