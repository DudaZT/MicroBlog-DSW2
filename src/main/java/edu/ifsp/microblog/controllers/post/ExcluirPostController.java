package edu.ifsp.microblog.controllers.post;

import java.io.IOException;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.PostService;
import edu.ifsp.microblog.service.ServiceException;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExcluirPostController extends HttpServlet {

    private final PostService postService = new PostService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            return;
        }

        try {
            int postId = Integer.parseInt(req.getParameter("postId"));
            postService.excluirPost(postId, logado.getId());
            
            // Redireciona de volta para a origem
            String origem = req.getParameter("origem");
            
            if ("perfil".equals(origem)) {
                ViewHelper.redirectWithSuccess(req, res, "/perfil", "Post excluído com sucesso.");
            } else {
                ViewHelper.redirectWithSuccess(req, res, "/feed", "Post excluído com sucesso.");
            }
            
        } catch (NumberFormatException e) {
            ViewHelper.redirectWithError(req, res, "/feed", "ID do post inválido.");
        } catch (ServiceException e) {
            ViewHelper.redirectWithError(req, res, "/feed", e.getMessage());
        }
    }
}