package edu.ifsp.microblog.service;

import java.io.IOException;
import java.util.List;

import edu.ifsp.microblog.modelo.Post;
import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.persistencia.CurtidaDAO;
import edu.ifsp.microblog.persistencia.PostDAO;
import edu.ifsp.microblog.util.Paginacao;
import edu.ifsp.microblog.util.UploadUtil;
import jakarta.servlet.http.Part;


public class PostService {

    private final PostDAO    postDAO    = new PostDAO();
    private final CurtidaDAO curtidaDAO = new CurtidaDAO();

    public static final int PAGE_SIZE = 10;

    // cria um post de texto, a imagem é opcional 
    public Post criarPost(Usuario autor, String conteudo, Part imagemPart) {
        if (conteudo == null || conteudo.isBlank()) {
            throw new ServiceException("O conteúdo do post não pode ser vazio.");
        }
        
        if (conteudo.length() > 280) {
            throw new ServiceException("O post não pode ter mais de 280 caracteres.");
        }

        Post post = new Post();
        post.setAutor(autor);
        post.setConteudo(conteudo.trim());

        // Upload de imagem (opcional)
        if (imagemPart != null && imagemPart.getSize() > 0) {
            try {
                String ext    = UploadUtil.extrairExtensao(imagemPart);
                String caminho = UploadUtil.salvar(imagemPart, ext);
                post.setImagem(caminho);
                
                // O MD5 do arquivo já é o próprio nome
                post.setImagemMd5(caminho.contains(".") ? caminho.substring(0, caminho.lastIndexOf('.')) : caminho);
                
            } catch (IOException e) {
                throw new ServiceException("Erro ao salvar imagem: " + e.getMessage());
            }
        }

        return postDAO.save(post);
    }

    // Feed paginado do usuário (posts próprios + de quem segue)
    public List<Post> getFeedPaged(int usuarioId, int page) {
        return postDAO.findFeedPaged(usuarioId, page, PAGE_SIZE);
    }

    // Posts do perfil de um usuário, paginados
    public List<Post> getPostsPerfilPaged(int usuarioId, int page) {
        return postDAO.findByUsuarioPaged(usuarioId, page, PAGE_SIZE);
    }

    // Posts curtidos por um usuário, paginados
    public List<Post> getPostsCurtidosPaged(int usuarioId, int page) {
        return postDAO.findCurtidosPaged(usuarioId, page, PAGE_SIZE);
    }

    // Paginação para o perfil de um usuário
    public Paginacao getPaginacaoPerfil(int usuarioId, int page) {
        int total = postDAO.countByUsuario(usuarioId);
        return new Paginacao(page, total, PAGE_SIZE);
    }

    // alterna curtida: curte se não curtiu, descurte se já curtiu
    public boolean toggleCurtida(int usuarioId, int postId) {
        if (curtidaDAO.exists(usuarioId, postId)) {
            curtidaDAO.descurtir(usuarioId, postId);
            
            return false; // descurtiu
        } else {
            curtidaDAO.curtir(usuarioId, postId);
            
            return true;  // curtiu
        }
    }

    public int contarCurtidas(int postId) {
        return curtidaDAO.countByPost(postId);
    }

    public boolean isCurtido(int usuarioId, int postId) {
        return curtidaDAO.exists(usuarioId, postId);
    }
    
    public void excluirPost(int postId, int usuarioId) {
        Post post = postDAO.findById(postId);
        
        if (post == null) {
            throw new ServiceException("Post não encontrado.");
        }
        if (post.getAutor().getId() != usuarioId) {
            throw new ServiceException("Você não pode excluir um post que não é seu.");
        }
        
        postDAO.delete(postId);
    }
}