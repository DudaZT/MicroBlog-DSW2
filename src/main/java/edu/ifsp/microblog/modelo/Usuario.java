package edu.ifsp.microblog.modelo;

import java.time.LocalDateTime;

public class Usuario {

    private int id;
    private String username;
    private String email;
    private String senhaHash; // SHA-256 hex
    private String nome;
    private String bio;
    private String fotoPerfil; // caminho relativo no servidor
    private String fotoMd5; // MD5 da foto
    private LocalDateTime dtCriacao;

    public int getId() { 
    	return id; 
    }
    
    public void setId(int id) { 
    	this.id = id; 
    }

    public String getUsername() { 
    	return username; 
    }
    
    
    public void setUsername(String username) { 
    	this.username = username; 
    }

    public String getEmail() { 
    	return email; 
    }
    
    public void setEmail(String email) { 
    	this.email = email; 
    }

    public String getSenhaHash() { 
    	return senhaHash; 
    }
    
    public void setSenhaHash(String senhaHash) { 
    	this.senhaHash = senhaHash; 
    }

    public String getNome() { 
    	return nome; 
    }
    
    public void setNome(String nome) { 
    	this.nome = nome; 
    }

    public String getBio() { 
    	return bio; 
    }
    
    public void setBio(String bio) { 
    	this.bio = bio; 
    }

    public String getFotoPerfil() { 
    	return fotoPerfil; 
    }
    
    public void setFotoPerfil(String fotoPerfil) { 
    	this.fotoPerfil = fotoPerfil; 
    }

    public String getFotoMd5() { 
    	return fotoMd5; 
    }
    
    public void setFotoMd5(String fotoMd5) { 
    	this.fotoMd5 = fotoMd5; 
    }

    public LocalDateTime getDtCriacao() { 
    	return dtCriacao; 
    }
    
    public void setDtCriacao(LocalDateTime dtCriacao) { 
    	this.dtCriacao = dtCriacao; 
    }
}