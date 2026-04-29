/*
* Execução via web
* Exemplo de comando para iniciar o servidor via terminal:
* Botão direito do mouse >> Novo terminal:
* MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000" mvn clean package cargo:run
*
*/

package com.gwj;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ola") // Esta será a URL: http://localhost:9080/teste2/ola
public class OlaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Projeto GWJ Servlet no Tomcat 10!</h1>");
        out.println("<p>Hora atual: " + new java.util.Date() + "</p>");
        out.println("</body></html>");
    }
}
