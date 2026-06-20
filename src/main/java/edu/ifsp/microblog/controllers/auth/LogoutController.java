package edu.ifsp.microblog.controllers.auth;

import java.io.IOException;

import edu.ifsp.microblog.util.Auth;
import edu.ifsp.microblog.util.ViewHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LogoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Auth.logout(req);
        ViewHelper.redirect(req, res, "/auth/login");
    }
}