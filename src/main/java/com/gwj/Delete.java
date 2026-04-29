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

@WebServlet("/delete-json") // Esta será a URL: http://localhost:9080/create-json?entity=[nome da classe em domain/entities/]
public class Delete extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // ESTA LINHA É OBRIGATÓRIA para transformar LocalDateTime em JSON:
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // ESTA LINHA É OBRIGATÓRIA

        // Obtém o nome da classe pelo GET da URL:
        String entityName = request.getParameter("entity");
        
        if (entityName != null && !entityName.trim().isEmpty()) { // Verifica se o parâmetro existe e tem conteúdo (não é só espaço em branco).
            IEntity entidade = SimpleObjectFactory.create(entityName);
            DataAccessObject dataAccessObject = new DataAccessObject();
            // Preenche a entidade com os dados da requisição (request) e envia para o DAO deletar a entidade no banco de dados.
            Long primaryKey = dataAccessObject.delete(EntityMapper.fillEntity(entidade, request)); //
            System.out.println("Delete: Retornou de dataAccessObject.delete");
            System.out.println("Delete: Chave primária Long = " + primaryKey + " da entidade excluída.");
            /*
            if(primaryKey > 0){ // Se há chave primária, significa que a entidade persistiu no banco de dados.
                entidade.setId(primaryKey);
                List<IEntity> listaEntity = dataAccessObject.read(entidade);
                // 2. Configurar a resposta para página JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // 3. Converter a lista para JSON usando Jackson
                String jsonResposta = mapper.writeValueAsString(listaEntity);

                // 4. Escrever a resposta
                PrintWriter out = response.getWriter();
                out.print(jsonResposta);
                out.flush();
            }
            */
        } else { // Caso o Read retornar vazio (nome de uma classe que não existe, ou nenhuma entidade encontrada pelo critério de pesquisa):
            // O parâmetro não foi enviado na URL ou está vazio
            // Define o tipo de conteúdo como HTML
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            // Imprime resposta no navegador
            out.println("<h1>Nenhuma chave primária recebida!</h1>");
        }
        
    }
}
