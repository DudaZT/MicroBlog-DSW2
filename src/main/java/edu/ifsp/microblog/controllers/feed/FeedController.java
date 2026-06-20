package edu.ifsp.microblog.controllers.feed;

import java.io.IOException;
import java.util.List;

import edu.ifsp.microblog.modelo.Post;
import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.PostService;
import edu.ifsp.microblog.service.UsuarioService;
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
    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            
            return;
        }

        int page = Paginacao.parsePage(req.getParameter("page"));
        List<Post> posts = postService.getFeedPaged(logado.getId(), page);

        java.util.Map<Integer, Integer> curtidasPorPost = new java.util.HashMap<>();
        java.util.Set<Integer> postsCurtidos = new java.util.HashSet<>();
        
        for (Post p : posts) {
            curtidasPorPost.put(p.getId(), postService.contarCurtidas(p.getId()));
            if (postService.isCurtido(logado.getId(), p.getId())) {
                postsCurtidos.add(p.getId());
            }
        }

        // a paginação é controlada por "tem próxima" baseado no tamanho retornado
        boolean temProxima = posts.size() == PostService.PAGE_SIZE;

        
        
        List<Usuario> sugestoes = usuarioService.obterSugestoesSeguir(logado.getId(), 5);

        req.setAttribute("posts", posts);
        req.setAttribute("page", page);
        req.setAttribute("temProxima", temProxima);
        req.setAttribute("sugestoes",  sugestoes);
        req.setAttribute("curtidasPorPost", curtidasPorPost);
        req.setAttribute("postsCurtidos", postsCurtidos);
        
        ViewHelper.forward(req, res, "feed/feed");
    }
}