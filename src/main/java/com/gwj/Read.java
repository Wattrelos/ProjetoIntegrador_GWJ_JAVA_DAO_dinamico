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
import java.lang.reflect.Method;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gwj.model.dataAccessObject.DataAccessObject;
import com.gwj.model.dataTransferObject.EntityMapper;
import com.gwj.model.domain.IEntity;
import com.gwj.model.domain.factory.SimpleObjectFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/read-json") // Esta será a URL: http://localhost:9080/teste2/lista-cliente-json
public class Read extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ESTA LINHA É OBRIGATÓRIA para transformar LocalDateTime em JSON:
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // ESTA LINHA É OBRIGATÓRIA

        String entityName = request.getParameter("entity");
        
        if (entityName != null && !entityName.trim().isEmpty()) { // Verifica se o parâmetro existe e tem conteúdo (não é só espaço em branco).
            IEntity entidade = SimpleObjectFactory.create(entityName);
            DataAccessObject dataAccessObject = new DataAccessObject();
            List<IEntity> listaEntity = dataAccessObject.read(EntityMapper.fillEntity(entidade, request));
            System.out.println("Read: Retornou de dataAccessObject.read");

            // 2. Configurar a resposta para JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // 3. Converter a lista para JSON usando Jackson
            String jsonResposta = mapper.writeValueAsString(listaEntity);

            // 4. Escrever a resposta
            PrintWriter out = response.getWriter();
            out.print(jsonResposta);
            out.flush();
            
        } else {
            // O parâmetro não foi enviado na URL ou está vazio
            // Define o tipo de conteúdo como HTML
            response.setContentType("text/html");
        
            // Obtém o objeto PrintWriter para escrever a resposta
            PrintWriter out = response.getWriter();
            
            // Imprime "Hello World" no navegador
            out.println("<h1>Nenhum valor recebido!</h1>");
        }
        
    }
}
