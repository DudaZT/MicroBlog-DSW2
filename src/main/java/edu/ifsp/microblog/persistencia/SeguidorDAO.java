package edu.ifsp.microblog.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ifsp.microblog.modelo.Usuario;

// Acesso a dados da tabela seguidor.
public class SeguidorDAO {

    public boolean isSeguindo(int seguidorId, int seguidoId) {
        String sql = "SELECT * FROM seguidor WHERE seguidor_id = ? AND seguido_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seguidorId);
            ps.setInt(2, seguidoId);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public int countSeguidores(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM seguidor WHERE seguido_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return 0;
    }

    public int countSeguindo(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM seguidor WHERE seguidor_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return 0;
    }

    public List<Usuario> findSeguidores(int usuarioId) {
        String sql = "SELECT u.id, u.username, u.nome, u.foto_perfil "
                   + "FROM seguidor s JOIN usuario u ON s.seguidor_id = u.id "
                   + "WHERE s.seguido_id = ?";
        
        return queryUsuarios(sql, usuarioId);
    }

    public List<Usuario> findSeguindo(int usuarioId) {
        String sql = "SELECT u.id, u.username, u.nome, u.foto_perfil "
                   + "FROM seguidor s JOIN usuario u ON s.seguido_id = u.id "
                   + "WHERE s.seguidor_id = ?";
        
        return queryUsuarios(sql, usuarioId);
    }

    private List<Usuario> queryUsuarios(String sql, int param) {
        List<Usuario> lista = new ArrayList<>();
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, param);
            
            try (ResultSet rs = ps.executeQuery()) {
            	
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("username"));
                    u.setNome(rs.getString("nome"));
                    u.setFotoPerfil(rs.getString("foto_perfil"));
                    lista.add(u);
                }
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return lista;
    }

    public void seguir(int seguidorId, int seguidoId) {
        String sql = "INSERT INTO seguidor (seguidor_id, seguido_id, dt_criacao) VALUES (?, ?, NOW())";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seguidorId);
            ps.setInt(2, seguidoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void deixarDeSeguir(int seguidorId, int seguidoId) {
        String sql = "DELETE FROM seguidor WHERE seguidor_id = ? AND seguido_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seguidorId);
            ps.setInt(2, seguidoId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}