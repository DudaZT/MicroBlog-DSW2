package edu.ifsp.microblog.util;

import edu.ifsp.microblog.modelo.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class Auth {

    public static final String SESSION_USUARIO = "usuarioLogado";

    private Auth() {}

    // retorna o usuário da sessão ou null se não autenticado
    public static Usuario getUsuarioLogado(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        
        if (session == null) return null;
        return (Usuario) session.getAttribute(SESSION_USUARIO);
    }


    // verifica se há usuário autenticado
    public static boolean isLogado(HttpServletRequest req) {
        return getUsuarioLogado(req) != null;
    }

    // armazena o usuário na sessão (login)
    public static void login(HttpServletRequest req, Usuario usuario) {
        req.getSession().setAttribute(SESSION_USUARIO, usuario);
    }


    // Invalida a sessão (logout).
    public static void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        
        if (session != null) session.invalidate();
    }
}