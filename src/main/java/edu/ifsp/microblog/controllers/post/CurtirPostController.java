package edu.ifsp.microblog.controllers.post;

import java.io.IOException;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.PostService;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// alterna curtida em um post e redireciona de volta à origem
public class CurtirPostController extends HttpServlet {

    private final PostService postService = new PostService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            
            return;
        }

        String postIdStr = req.getParameter("postId");
        String origem = req.getParameter("origem"); // "feed" ou "perfil"

        try {
            int postId = Integer.parseInt(postIdStr);
            postService.toggleCurtida(logado.getId(), postId);
        } catch (NumberFormatException e) {
            // postId inválido — ignora
        }

        // Redireciona para onde o usuário estava
        if ("perfil".equals(origem)) {
            String username = req.getParameter("username");
            ViewHelper.redirect(req, res, "/perfil?username=" + username);
        } else {
            ViewHelper.redirect(req, res, "/feed");
        }
    }
}