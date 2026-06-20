package edu.ifsp.microblog.controllers.upload;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import edu.ifsp.microblog.util.UploadUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Serve os arquivos físicos de upload (fotos de perfil e imagens de posts)
public class UploadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String caminho = req.getPathInfo(); // ex: "/abc123.jpg"

        if (caminho == null || caminho.isBlank() || caminho.equals("/")) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // remove a barra inicial e impede acesso a outras pastas (ex: ../../etc/passwd)
        String nomeArquivo = caminho.substring(1).replace("\\", "");
        if (nomeArquivo.contains("..") || nomeArquivo.contains("/")) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        File arquivo = UploadUtil.getFile(nomeArquivo);

        if (!arquivo.exists() || !arquivo.isFile()) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(arquivo.getName());
        res.setContentType(mime != null ? mime : "application/octet-stream");
        res.setContentLengthLong(arquivo.length());
        res.setHeader("Cache-Control", "public, max-age=86400");

        try (var in = Files.newInputStream(arquivo.toPath())) {
            in.transferTo(res.getOutputStream());
        }
    }
}