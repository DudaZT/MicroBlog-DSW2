package edu.ifsp.microblog.service;

import java.util.List;

import edu.ifsp.microblog.modelo.Usuario;
import edu.ifsp.microblog.persistencia.UsuarioDAO;
import edu.ifsp.microblog.util.HashUtil;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // autentica um usuário pelo username e senha
    public Usuario autenticar(String username, String senha) {
        Usuario usuario = usuarioDAO.findByUsername(username);
        
        if (usuario == null) return null;
        
        String senhaHash = HashUtil.sha256(senha);
        
        return senhaHash.equals(usuario.getSenhaHash()) ? usuario : null;
    }

    // cadastra um novo usuário

    public Usuario cadastrar(String username, String email, String nome, String senha) {
        if (usuarioDAO.findByUsername(username) != null) {
            throw new ServiceException("Username '" + username + "' já está em uso.");
        }
        
        if (usuarioDAO.findByEmail(email) != null) {
            throw new ServiceException("E-mail '" + email + "' já está cadastrado.");
        }

        Usuario u = new Usuario();
        u.setUsername(username.trim().toLowerCase());
        u.setEmail(email.trim().toLowerCase());
        u.setNome(nome.trim());
        u.setSenhaHash(HashUtil.sha256(senha));
        return usuarioDAO.save(u);
    }

    // atualiza dados de perfil (nome, bio e foto).
    public void atualizarPerfil(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new ServiceException("O nome não pode ser vazio.");
        }
        
        usuarioDAO.update(usuario);
    }

    public Usuario buscarPorUsername(String username) {
        return usuarioDAO.findByUsername(username);
    }

    public Usuario buscarPorId(int id) {
        return usuarioDAO.findById(id);
    }

    public List<Usuario> obterSugestoesSeguir(int usuarioId, int limit) {
        return usuarioDAO.findSugestoesSeguir(usuarioId, limit);
    }
}