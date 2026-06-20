package edu.ifsp.microblog.controllers.perfil;

import java.io.IOException;
import java.util.List;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.SeguidorService;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Lista quem o usuário logado está seguindo, com opção de deixar de seguir
public class SeguindoController extends HttpServlet {

    private final SeguidorService seguidorService = new SeguidorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);

        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            return;
        }

        List<Usuario> seguindo = seguidorService.listarSeguindo(logado.getId());

        req.setAttribute("seguindoLista", seguindo);
        ViewHelper.forward(req, res, "perfil/seguindo");
    }
}