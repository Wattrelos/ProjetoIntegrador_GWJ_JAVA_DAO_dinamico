# Projeto Integrador GWJ JAVA dinâmico.
## Padrões de projeto (GoF) aplicados até o momento:
- Singleton (conexão com o BD)
- Factory (construtor de objetos)
- DAO  (persistir objetos no BD dinamicamente, inclusive recursivamente, persistindo classes filhas)
- DTO (preenche objetos dinamicamente com dados do formulário HTTP ou com dados do BD)
- Templante (Para construír páginas web).
Esta é a outra forma de CRUD mais dinâmica e enxuta.

1. Primeiro, instale o banco de dados de testes:
gwj2.SQL

2. Crie um usuário. A boa prática é criar um usuário específico para o banco de dados, ao invés de usar root:
```
-- MySQL 8.0+: O comando GRANT ... IDENTIFIED BY foi removido. Primeiro você deve criar o usuário e depois dar as permissões:
-- 1. Cria o usuário primeiro
CREATE USER 'desenvolvedor'@'%' IDENTIFIED BY 'b2#FbXPQTu4FYw';
-- 2. Garante privilégios totais apenas no banco gwj2
GRANT ALL PRIVILEGES ON `gwj2`.* TO 'desenvolvedor'@'%';
-- 6. Aplica as mudanças
FLUSH PRIVILEGES;
```

_Por causa da limitação da conta de usuário no ambiente Windows, o comando foi alterado._
Para inicializar o servidor, execute o seguinte comando o terminal do VS Code:
```
MAVEN_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000" mvn clean package cargo:run
```

Se não houver mensagens de erro, basta testar no navegador:\
Thymeleaf:
```
http://localhost:9090/
```
Servlet:
```
http://localhost:9090/ola
```


3. Depois de implementar o banco de dados e inicializar o teu servidor, tu podes utilizar os seguintes comandos no navegador:\
Ver a lista de clientes;
```
http://localhost:9090/read-json?entity=Cliente
```
Ver um cliente específico pelo número ID:
```
http://localhost:9090/read-json?entity=Cliente&id=29
```
Ver endereços:
```
http://localhost:9090/read-json?entity=Endereco
```

Agora, para simular as requisições POST, recomendo o uso de um aplicativo, por exemplo, o Postman.
Isso é necessário para enviar dados via POST, para simular os comandos create, update e delete:
```
http://localhost:9090/create-json?entity=Cliente
```
acrescentar parâmetros no corpo da requisição (devem ser iguais aos nomes de atributos das classes domínio).
```
http://localhost:9090/update-json?entity=Cliente
```
acrescentar parâmetros no corpo da requisição (devem ser iguais aos nomes de atributos das classes domínio).
```
http://localhost:9090/delete-json?entity=Cliente
```
(acrescentar parâmetros no corpo da requisição com o key=id e Value= [número id do registro].
