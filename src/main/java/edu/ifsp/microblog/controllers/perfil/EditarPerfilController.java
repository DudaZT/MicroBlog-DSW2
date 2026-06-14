package edu.ifsp.microblog.controllers.perfil;

import java.io.IOException;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.service.ServiceException;
import edu.ifsp.microblog.service.UsuarioService;
import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.UploadUtil;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

// edição de perfil (nome, bio e foto)

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize       = 3 * 1024 * 1024,
    maxRequestSize    = 5 * 1024 * 1024
)
public class EditarPerfilController extends HttpServlet {

    private final UsuarioService usuarioService = new UsuarioService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            
            return;
        }
        
        req.setAttribute("usuario", logado);
        ViewHelper.forward(req, res, "perfil/editarPerfil");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            
            return;
        }

        String nome = req.getParameter("nome");
        String bio  = req.getParameter("bio");
        Part   foto = req.getPart("fotoPerfil");

        logado.setNome(nome);
        logado.setBio(bio);

        // Upload de nova foto (opcional)
        if (foto != null && foto.getSize() > 0) {
            try {
                String ext    = UploadUtil.extrairExtensao(foto);
                String caminho = UploadUtil.salvar(foto, ext);
                logado.setFotoPerfil(caminho);
                logado.setFotoMd5(caminho.contains(".") ? caminho.substring(0, caminho.lastIndexOf('.')) : caminho);
                
            } catch (IOException e) {
                req.setAttribute("erro", "Erro ao salvar foto: " + e.getMessage());
                req.setAttribute("usuario", logado);
                ViewHelper.forward(req, res, "perfil/editarPerfil");
                
                return;
            }
        }

        try {
            usuarioService.atualizarPerfil(logado);
            // Atualiza o objeto da sessão
            Auth.login(req, logado);
            ViewHelper.redirectWithSuccess(req, res, "/perfil", "Perfil atualizado com sucesso!");
        } catch (ServiceException e) {
            req.setAttribute("erro", e.getMessage());
            req.setAttribute("usuario", logado);
            ViewHelper.forward(req, res, "perfil/editarPerfil");
        }
    }
}