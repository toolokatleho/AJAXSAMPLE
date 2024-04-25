/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.ajax;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;


/**
 *
 * @author TOOLO
 */

public class AutoCompleteServlet extends HttpServlet {
    private ComposerData compData = new ComposerData();
    private HashMap composers = compData.getComposers();
    
    private ServletContext context;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    String action = request.getParameter("action");
    String targetId = request.getParameter("id");
    StringBuffer sb = new StringBuffer();

    if (targetId != null) {
        targetId = targetId.trim().toLowerCase();
    } else {
        context.getRequestDispatcher("/error.jsp").forward(request, response);
        return; // Stop further execution if targetId is null
    }

    boolean namesAdded = false;
    if ("complete".equals(action)) { // Use equals() for string comparison

        // check if user sent empty string
        if (!targetId.isEmpty()) {

            Iterator it = composers.keySet().iterator();

            while (it.hasNext()) {
                String id = (String) it.next();
                Composer composer = (Composer) composers.get(id);

                if (composer.getFirstName().toLowerCase().startsWith(targetId) ||
                    composer.getLastName().toLowerCase().startsWith(targetId) ||
                    (composer.getFirstName() + " " + composer.getLastName()).toLowerCase().startsWith(targetId)) {

                    sb.append("<composer>");
                    sb.append("<id>").append(composer.getId()).append("</id>");
                    sb.append("<firstName>").append(composer.getFirstName()).append("</firstName>");
                    sb.append("<lastName>").append(composer.getLastName()).append("</lastName>");
                    sb.append("</composer>");
                    namesAdded = true;
                }
            }
        }

        if (namesAdded) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<composers>" + sb.toString() + "</composers>");
        } else {
            //nothing to show
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    } else if ("lookup".equals(action)) { // Use equals() for string comparison

        // put the target composer in the request scope to display
        if (targetId != null && composers.containsKey(targetId.trim())) {
            request.setAttribute("composer", composers.get(targetId));
            context.getRequestDispatcher("/composer.jsp").forward(request, response);
        }
    }
}


    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
