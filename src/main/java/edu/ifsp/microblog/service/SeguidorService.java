package edu.ifsp.microblog.service;

import edu.ifsp.microblog.persistencia.SeguidorDAO;

// seguir/deixar de seguir usuários.
public class SeguidorService {

    private final SeguidorDAO seguidorDAO = new SeguidorDAO();


    // Alterna o estado de seguir: segue se não seguia, deixa de seguir se já seguia.
 
    public boolean toggleSeguir(int seguidorId, int seguidoId) {
        if (seguidorId == seguidoId) {
            throw new ServiceException("Um usuário não pode seguir a si mesmo.");
        }
        
        if (seguidorDAO.isSeguindo(seguidorId, seguidoId)) {
            seguidorDAO.deixarDeSeguir(seguidorId, seguidoId);
            
            return false;
        } else {
            seguidorDAO.seguir(seguidorId, seguidoId);
            
            return true;
        }
    }

    public boolean isSeguindo(int seguidorId, int seguidoId) {
        return seguidorDAO.isSeguindo(seguidorId, seguidoId);
    }

    public int countSeguidores(int usuarioId) {
        return seguidorDAO.countSeguidores(usuarioId);
    }

    public int countSeguindo(int usuarioId) {
        return seguidorDAO.countSeguindo(usuarioId);
    }
}