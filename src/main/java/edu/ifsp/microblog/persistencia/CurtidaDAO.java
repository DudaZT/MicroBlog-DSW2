package edu.ifsp.microblog.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


// Acesso a dados da tabela curtida.
public class CurtidaDAO {

    public boolean exists(int usuarioId, int postId) {
        String sql = "SELECT * FROM curtida WHERE usuario_id = ? AND post_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, postId);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public int countByPost(int postId) {
        String sql = "SELECT COUNT(*) FROM curtida WHERE post_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return 0;
    }

    public void curtir(int usuarioId, int postId) {
        String sql = "INSERT INTO curtida (usuario_id, post_id, dt_criacao) VALUES (?, ?, NOW())";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, postId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public void descurtir(int usuarioId, int postId) {
        String sql = "DELETE FROM curtida WHERE usuario_id = ? AND post_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, postId);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}