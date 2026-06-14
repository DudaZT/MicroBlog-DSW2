package edu.ifsp.microblog.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import edu.ifsp.microblog.modelo.Usuario;

/**
 * Acesso a dados da entidade Usuario.
 */

public class UsuarioDAO {

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setNome(rs.getString("nome"));
        u.setBio(rs.getString("bio"));
        u.setFotoPerfil(rs.getString("foto_perfil"));
        u.setFotoMd5(rs.getString("foto_md5"));
        u.setSenhaHash(rs.getString("senha_hash"));
        
        // LocalDateTime a partir do timestamp
        var ts = rs.getTimestamp("dt_criacao");
        
        if (ts != null) {
        	u.setDtCriacao(ts.toLocalDateTime());
        }
        
        return u;
    }

    // consultas

    public Usuario findById(int id) {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            
        	ps.setInt(1, id);
        	
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return null;
    }

    public Usuario findByUsername(String username) {
        String sql = "SELECT * FROM usuario WHERE username = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        	
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return null;
    }

    public Usuario findByEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        	
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return null;
    }

    // operações

    /**
     * Insere um novo usuário.
     */
    public Usuario save(Usuario usuario) {
        String sql = "INSERT INTO usuario (username, email, senha_hash, nome, bio, foto_perfil, foto_md5, dt_criacao) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // início da transação
            
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, usuario.getUsername());
                ps.setString(2, usuario.getEmail());
                ps.setString(3, usuario.getSenhaHash());
                ps.setString(4, usuario.getNome());
                ps.setString(5, usuario.getBio());
                ps.setString(6, usuario.getFotoPerfil());
                ps.setString(7, usuario.getFotoMd5());
                ps.setObject(8, LocalDateTime.now());
                ps.executeUpdate();
                
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) usuario.setId(keys.getInt(1));
                }
                
                conn.commit();
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return usuario;
    }

    public void update(Usuario usuario) {
        String sql = "UPDATE usuario SET nome = ?, bio = ?, foto_perfil = ?, foto_md5 = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        	
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getBio());
            ps.setString(3, usuario.getFotoPerfil());
            ps.setString(4, usuario.getFotoMd5());
            ps.setInt(5, usuario.getId());
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}