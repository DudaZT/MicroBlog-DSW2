package edu.ifsp.microblog.controllers.post;

import java.io.IOException;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.PostService;
import edu.ifsp.microblog.service.ServiceException;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

// Cria um novo post (multipart para suportar upload de imagem )
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,       // 1 MB em memória
    maxFileSize       = 5 * 1024 * 1024,   // 5 MB por arquivo
    maxRequestSize    = 10 * 1024 * 1024   // 10 MB por requisição
)
public class CriarPostController extends HttpServlet {

    private final PostService postService = new PostService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            return;
        }

        String conteudo = req.getParameter("conteudo");
        Part imagemPart = req.getPart("imagem");

        try {
            postService.criarPost(logado, conteudo, imagemPart);
            ViewHelper.redirect(req, res, "/feed");
        } catch (ServiceException e) {
            ViewHelper.redirectWithError(req, res, "/feed", e.getMessage());
        }
    }
}