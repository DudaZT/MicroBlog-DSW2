package edu.ifsp.microblog.controllers.post;

import java.io.IOException;
import java.util.Set;

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
    fileSizeThreshold = 1024 * 1024, // 1 MB em memória
    maxFileSize       = 5 * 1024 * 1024, // 5 MB por arquivo
    maxRequestSize    = 10 * 1024 * 1024 // 10 MB por requisição
)
public class CriarPostController extends HttpServlet {

    private final PostService postService = new PostService();
    
    private static final Set<String> EXTENSOES_PERMITIDAS = Set.of("jpg", "jpeg", "png", "webp");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Usuario logado = Auth.getUsuarioLogado(req);
        
        if (logado == null) {
            ViewHelper.redirect(req, res, "/auth/login");
            return;
        }

        String conteudo = null;
        Part imagemPart = null;

        try {
            conteudo = req.getParameter("conteudo");
            imagemPart = req.getPart("imagem");
        } catch (IllegalStateException e) {
            ViewHelper.redirectWithError(req, res, "/feed", 
                "Arquivo muito grande! O tamanho máximo permitido é 5 MB.");
            return;
        } catch (Exception e) {
            ViewHelper.redirectWithError(req, res, "/feed", 
                "Erro ao processar o upload.");
            return;
        }

        // Validação simples por extensão
        if (imagemPart != null && imagemPart.getSize() > 0) {
            String nomeArquivo = imagemPart.getSubmittedFileName();
            
            if (nomeArquivo == null || !nomeArquivo.contains(".")) {
                ViewHelper.redirectWithError(req, res, "/feed", 
                    "Arquivo inválido. Envie uma imagem JPG, PNG ou WebP.");
                return;
            }

            String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf('.') + 1).toLowerCase();
            
            if (!EXTENSOES_PERMITIDAS.contains(extensao)) {
                ViewHelper.redirectWithError(req, res, "/feed", 
                    "Tipo de arquivo não aceito. Envie apenas imagens JPG, PNG ou WebP.");
                return;
            }
        }

        try {
            postService.criarPost(logado, conteudo, imagemPart);
            ViewHelper.redirect(req, res, "/feed");
        } catch (ServiceException e) {
            ViewHelper.redirectWithError(req, res, "/feed", e.getMessage());
        }
    }
}