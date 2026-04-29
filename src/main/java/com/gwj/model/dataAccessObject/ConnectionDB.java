package com.gwj.model.dataAccessObject;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class ConnectionDB {

    // 1. Instância única e estática da própria classe
    private static ConnectionDB instance;
    private Connection connection;
	
 // Configurações do banco (ajuste conforme seu servidor)
    private static final String URL      = "jdbc:mariadb://localhost:3306/gwj2";
    private static final String USER     = "desenvolvedor";
    private static final String PASSWORD = "b2#FbXPQTu4FYw";
    
 // 2. Construtor privado: impede o uso de 'new MariaDbSingleton()'
    private ConnectionDB() {
        try {
            // Carrega o driver MariaDB
            Class.forName("org.mariadb.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco", e);
        }
    }

    // 3. Método público estático para obter a única instância
    public static synchronized ConnectionDB getInstance() {
        if (instance == null) {
            instance = new ConnectionDB();
        }
        return instance;
    }

    // Método para obter a conexão SQL
    public Connection getConnection() {
        return connection;
    }


}
