package edu.ifsp.microblog.util;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class ViewHelper {

    private static final String PAGES_PREFIX = "/WEB-INF/pages/";

    private ViewHelper() {}

    public static void forward(HttpServletRequest req, HttpServletResponse res, String view)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(PAGES_PREFIX + view + ".jsp");
        rd.forward(req, res);
    }

    public static void redirect(HttpServletRequest req, HttpServletResponse res, String path)
            throws IOException {
        res.sendRedirect(req.getContextPath() + path);
    }

    public static void redirectWithError(HttpServletRequest req, HttpServletResponse res,
                                          String path, String errorMsg) throws IOException {
        req.getSession().setAttribute("erro", errorMsg);
        redirect(req, res, path);
    }

    public static void redirectWithSuccess(HttpServletRequest req, HttpServletResponse res,
                                            String path, String successMsg) throws IOException {
        req.getSession().setAttribute("sucesso", successMsg);
        redirect(req, res, path);
    }
}