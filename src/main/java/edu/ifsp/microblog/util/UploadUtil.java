package edu.ifsp.microblog.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import jakarta.servlet.http.Part;

// upload de arquivos (foto de perfil e imagens de posts).
// - Ao receber um arquivo, calcula seu MD5
// - Se já existir um arquivo com esse MD5 no storage, reutiliza o existente
// - senão, salva o novo arquivo usando o MD5 como nome
// faz com que o mesmo arquivo não seja armazenado duas vezes fisicamente
public class UploadUtil {

    // Diretório físico de uploads
    private static final String UPLOAD_DIR = System.getProperty("catalina.home") + "/microblog-uploads/";

    private UploadUtil() {}

    public static String salvar(Part part, String extensao) throws IOException {
        ensureUploadDirExists();

        // 1. Calcular MD5 
        String md5;
        try (InputStream is = part.getInputStream()) {
            md5 = HashUtil.md5(is);
        }

        String nomeArquivo = md5 + "." + extensao.toLowerCase();
        Path destino = Paths.get(UPLOAD_DIR, nomeArquivo);

        // 2. Só grava se ainda não existe 
        if (!Files.exists(destino)) {
            try (InputStream is = part.getInputStream()) {
                Files.copy(is, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        // 3. Retorna o caminho relativo (armazenado no banco)
        return nomeArquivo;
    }

    // Retorna o caminho físico absoluto para um arquivo de upload, dado o caminho relativo armazenado no banco
    public static File getFile(String caminhoRelativo) {
        return new File(UPLOAD_DIR + caminhoRelativo);
    }

   //Extrai a extensão do nome original do arquivo enviado retorna "jpg" como padrão caso não encontre extensão.
    public static String extrairExtensao(Part part) {
        String header = part.getHeader("content-disposition");
        
        if (header == null) return "jpg";
        
        for (String token : header.split(";")) {
            if (token.trim().startsWith("filename")) {
                String nome = token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
                int dot = nome.lastIndexOf('.');
                
                if (dot >= 0) return nome.substring(dot + 1);
            }
        }
        
        return "jpg";
    }

    private static void ensureUploadDirExists() throws IOException {
        Path dir = Paths.get(UPLOAD_DIR);
        
        if (!Files.exists(dir)) Files.createDirectories(dir);
    }
}