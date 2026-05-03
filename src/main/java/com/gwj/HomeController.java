package com.gwj;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet("") // Mapeia a raiz http://localhost:9090/
public class HomeController extends HttpServlet {

    private TemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    @Override
    public void init() throws ServletException {
        // 1. Configura como o Thymeleaf vai encontrar os arquivos
        this.application = JakartaServletWebApplication.buildApplication(getServletContext()); // JakartaServletWebApplication: É a forma moderna (Thymeleaf 3.1) de integrar com as APIs jakarta.*.
        
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(application);
        
        resolver.setPrefix("/WEB-INF/templates/");  // setPrefix("/WEB-INF/templates/"): Diz ao Thymeleaf para olhar dentro da pasta protegida. Arquivos em WEB-INF não podem ser acessados diretamente pela URL, o que aumenta a segurança.
        resolver.setSuffix(".html");                // setSuffix(".html"): Permite que você chame apenas "home" no método process, e ele buscará por home.html
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); // Útil para desenvolvimento

        // 2. Inicializa o motor
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // 3. Prepara o contexto da requisição (Thymeleaf 3.1+)
        var exchange = application.buildExchange(req, resp);
        WebContext context = new WebContext(exchange); // WebContext: É onde você coloca as variáveis Java que quer exibir no HTML usando ${variavel}.
        
        // Exemplo de envio de dados para a página
        context.setVariable("mensagem", "Olá do Servlet com Thymeleaf!");

        
        // Define o tipo de conteúdo e o charset para o navegador
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        // 4. Renderiza o template "home" (.html é adicionado pelo sufixo)
        templateEngine.process("home", context, resp.getWriter());
    }
}
