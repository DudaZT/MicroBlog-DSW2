package edu.ifsp.microblog.modelo;

import java.time.LocalDateTime;

public class Post {

    private int id;
    private Usuario autor;
    private String conteudo;    // máx. 280 caracteres
    private String imagem;      // caminho relativo
    private String imagemMd5;   // MD5 da imagem para deduplicação
    private LocalDateTime dtCriacao;

    public int getId() { 
    	return id; 
    }
    
    public void setId(int id) { 
    	this.id = id; 
    }

    public Usuario getAutor() { 
    	return autor; 
    }
    
    public void setAutor(Usuario autor) { 
    	this.autor = autor; 
    }

    public String getConteudo() { 
    	return conteudo; 
    }
    
    public void setConteudo(String conteudo) { 
    	this.conteudo = conteudo; 
    }

    public String getImagem() { 
    	return imagem; 
    }
    
    public void setImagem(String imagem) { 
    	this.imagem = imagem; 
    }

    public String getImagemMd5() { 
    	return imagemMd5; 
    }
    
    public void setImagemMd5(String imagemMd5) { 
    	this.imagemMd5 = imagemMd5; 
    }

    public LocalDateTime getDtCriacao() { 
    	return dtCriacao; 
    }
    
    public void setDtCriacao(LocalDateTime dtCriacao) { 
    	this.dtCriacao = dtCriacao; 
    }
}