package edu.ifsp.microblog.controllers.feed;

import java.io.IOException;
import java.util.List;

import edu.ifsp.microblog.modelo.Post;
import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.PostService;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.Paginacao;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// exibe o feed paginado do usuário logado
public class FeedController extends HttpServlet {

    private final PostService postService = new PostService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            
            return;
        }

        int page = Paginacao.parsePage(req.getParameter("page"));
        List<Post> posts = postService.getFeedPaged(logado.getId(), page);

        // Para o feed não há total simples de calcular sem query extra;
        // a paginação é controlada por "tem próxima" baseado no tamanho retornado.
        boolean temProxima = posts.size() == PostService.PAGE_SIZE;

        req.setAttribute("posts",      posts);
        req.setAttribute("page",       page);
        req.setAttribute("temProxima", temProxima);

        ViewHelper.forward(req, res, "feed/feed");
    }
}