package edu.ifsp.microblog.controllers.seguir;

import java.io.IOException;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.SeguidorService;
import edu.ifsp.microblog.service.ServiceException;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// alterna o estado de seguir um usuário e redireciona ao perfil
public class SeguirController extends HttpServlet {

    private final SeguidorService seguidorService = new SeguidorService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            
            return;
        }

        String seguidoUsername = req.getParameter("username");
        String seguidoIdStr = req.getParameter("seguidoId");
        String origem = req.getParameter("origem");

        try {
            int seguidoId = Integer.parseInt(seguidoIdStr);
            seguidorService.toggleSeguir(logado.getId(), seguidoId);
            
        } catch (NumberFormatException | ServiceException e) {
            // ignora erros — redireciona de volta
        }

        if ("feed".equals(origem)) {
            ViewHelper.redirect(req, res, "/feed");
        } else if ("seguindo".equals(origem)) {
            ViewHelper.redirect(req, res, "/perfil/seguindo");
        } else {
            ViewHelper.redirect(req, res, "/perfil?username=" + seguidoUsername);
        }
    }
}