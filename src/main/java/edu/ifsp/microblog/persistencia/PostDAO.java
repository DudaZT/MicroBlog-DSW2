package edu.ifsp.microblog.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.ifsp.microblog.modelo.Post;
import edu.ifsp.microblog.modelo.Usuario;


// Acesso a dados da entidade Post
public class PostDAO {

    // mapeamento

    private Post mapRow(ResultSet rs) throws SQLException {
        Post p = new Post();
        p.setId(rs.getInt("p_id"));
        p.setConteudo(rs.getString("conteudo"));
        p.setImagem(rs.getString("imagem"));
        p.setImagemMd5(rs.getString("imagem_md5"));
        
        var ts = rs.getTimestamp("p_dt");
        
        if (ts != null) {
        	p.setDtCriacao(ts.toLocalDateTime());
        }

        Usuario u = new Usuario();
        
        u.setId(rs.getInt("u_id"));
        u.setUsername(rs.getString("username"));
        u.setNome(rs.getString("nome"));
        u.setFotoPerfil(rs.getString("foto_perfil"));
        p.setAutor(u);
        
        return p;
    }

    private static final String SELECT_BASE =
        "SELECT p.id AS p_id, p.conteudo, p.imagem, p.imagem_md5, p.dt_criacao AS p_dt, "
      + "       u.id AS u_id, u.username, u.nome, u.foto_perfil "
      + "FROM post p "
      + "JOIN usuario u ON p.usuario_id = u.id ";

    // consultas

    // Feed paginado: posts do próprio usuário + posts de quem ele segue
    public List<Post> findFeedPaged(int usuarioId, int page, int pageSize) {
        String sql = SELECT_BASE
            + "WHERE p.usuario_id = ? "
            + "   OR p.usuario_id IN (SELECT seguido_id FROM seguidor WHERE seguidor_id = ?) "
            + "ORDER BY p.dt_criacao DESC "
            + "LIMIT ?, ?";

        List<Post> posts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, usuarioId);
            ps.setInt(3, (page - 1) * pageSize);
            ps.setInt(4, pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) posts.add(mapRow(rs));
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return posts;
    }

    // Posts de um perfil específico, paginados
    public List<Post> findByUsuarioPaged(int usuarioId, int page, int pageSize) {
        String sql = SELECT_BASE
            + "WHERE p.usuario_id = ? "
            + "ORDER BY p.dt_criacao DESC "
            + "LIMIT ?, ?";

        List<Post> posts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, (page - 1) * pageSize);
            ps.setInt(3, pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) posts.add(mapRow(rs));
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return posts;
    }

    // Posts curtidos por um usuário, paginados (aba "Curtidas" do perfil).
    public List<Post> findCurtidosPaged(int usuarioId, int page, int pageSize) {
        String sql = SELECT_BASE
            + "JOIN curtida c ON c.post_id = p.id "
            + "WHERE c.usuario_id = ? "
            + "ORDER BY c.dt_criacao DESC "
            + "LIMIT ?, ?";

        List<Post> posts = new ArrayList<>();
        
        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setInt(2, (page - 1) * pageSize);
            ps.setInt(3, pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) posts.add(mapRow(rs));
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return posts;
    }

    // Conta total de posts de um usuário (para calcular número de páginas)
    public int countByUsuario(int usuarioId) {
        String sql = "SELECT COUNT(*) FROM post WHERE usuario_id = ?";
        
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

    // operações

    public Post save(Post post) {
        String sql = "INSERT INTO post (usuario_id, conteudo, imagem, imagem_md5, dt_criacao) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false);
            
            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, post.getAutor().getId());
                ps.setString(2, post.getConteudo());
                ps.setString(3, post.getImagem());
                ps.setString(4, post.getImagemMd5());
                ps.setObject(5, LocalDateTime.now());
                ps.executeUpdate();
                
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) post.setId(keys.getInt(1));
                }
                
                conn.commit();
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        
        return post;
    }

    public void delete(int postId) {
        String deleteCurtidas = "DELETE FROM curtida WHERE post_id = ?";
        String deletePost = "DELETE FROM post WHERE id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection()) {
            conn.setAutoCommit(false); // Inicia transação
            
            try {
                // 1. Primeiro remove as curtidas do post
                try (PreparedStatement ps = conn.prepareStatement(deleteCurtidas)) {
                    ps.setInt(1, postId);
                    ps.executeUpdate();
                }
                
                // 2. Depois remove o post
                try (PreparedStatement ps = conn.prepareStatement(deletePost)) {
                    ps.setInt(1, postId);
                    ps.executeUpdate();
                }
                
                conn.commit(); // Confirma a transação
                
            } catch (SQLException e) {
                conn.rollback(); // Desfaz em caso de erro
                throw e;
            }
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public Post findById(int id) {
        String sql = SELECT_BASE + "WHERE p.id = ?";
        try (Connection conn = DatabaseConnector.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return null;
    }
}