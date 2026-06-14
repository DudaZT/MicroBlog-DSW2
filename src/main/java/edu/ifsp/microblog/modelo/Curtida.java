package edu.ifsp.microblog.modelo;

import java.time.LocalDateTime;

/**
 * Representa uma curtida de um usuário em um post.
 */
public class Curtida {

    private int usuarioId;
    private int postId;
    private LocalDateTime dtCriacao;

    public int getUsuarioId() { 
    	return usuarioId; 
    }
    
    public void setUsuarioId(int usuarioId) { 
    	this.usuarioId = usuarioId; 
    }

    public int getPostId() { 
    	return postId; 
    }
    
    public void setPostId(int postId) { 
    	this.postId = postId; 
    }

    public LocalDateTime getDtCriacao() { 
    	return dtCriacao; 
    }
    
    public void setDtCriacao(LocalDateTime dtCriacao) { 
    	this.dtCriacao = dtCriacao; 
    }
}