package edu.ifsp.microblog.controllers.perfil;

import java.io.IOException;
import java.util.List;

import edu.ifsp.microblog.modelo.Post;
import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.PostService;
import edu.ifsp.microblog.service.SeguidorService;
import edu.ifsp.microblog.service.UsuarioService;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.Paginacao;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// exibe o perfil de um usuário
// Aba "posts" ou aba "curtidas" controlada pelo parâmetro "aba"
public class PerfilController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();
    private final PostService postService = new PostService();
    private final SeguidorService seguidorService = new SeguidorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            return;
        }

        String username = req.getParameter("username");
        
        if (username == null || username.isBlank()) {
            username = logado.getUsername();
        }

        Usuario perfil = usuarioService.buscarPorUsername(username);
        
        if (perfil == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuário não encontrado.");
            
            return;
        }

        String aba = req.getParameter("aba") != null ? req.getParameter("aba") : "posts";
        int page = Paginacao.parsePage(req.getParameter("page"));

        List<Post> posts;
        Paginacao paginacao;

        if ("curtidas".equals(aba)) {
            posts = postService.getPostsCurtidosPaged(perfil.getId(), page);
            paginacao = postService.getPaginacaoPerfil(perfil.getId(), page); // aprox.
        } else {
            posts = postService.getPostsPerfilPaged(perfil.getId(), page);
            paginacao = postService.getPaginacaoPerfil(perfil.getId(), page);
        }

        boolean isSeguindo = seguidorService.isSeguindo(logado.getId(), perfil.getId());
        int seguidores = seguidorService.countSeguidores(perfil.getId());
        int seguindo = seguidorService.countSeguindo(perfil.getId());
        boolean isProprioUser = logado.getId() == perfil.getId();

        java.util.Map<Integer, Integer> curtidasPorPost = new java.util.HashMap<>();
        java.util.Set<Integer> postsCurtidos = new java.util.HashSet<>();
        
        for (Post p : posts) {
            curtidasPorPost.put(p.getId(), postService.contarCurtidas(p.getId()));
            
            if (postService.isCurtido(logado.getId(), p.getId())) {
                postsCurtidos.add(p.getId());
            }
        }
        
        req.setAttribute("perfil", perfil);
        req.setAttribute("posts", posts);
        req.setAttribute("paginacao", paginacao);
        req.setAttribute("aba", aba);
        req.setAttribute("isSeguindo", isSeguindo);
        req.setAttribute("seguidores", seguidores);
        req.setAttribute("seguindo", seguindo);
        req.setAttribute("isProprioUser", isProprioUser);
        req.setAttribute("curtidasPorPost", curtidasPorPost);
        req.setAttribute("postsCurtidos", postsCurtidos);

        ViewHelper.forward(req, res, "perfil/perfil");
    }
}