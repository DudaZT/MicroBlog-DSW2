package edu.ifsp.microblog.persistencia;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

// Fornece conexões JDBC via pool configurado no Tomcat (JNDI).
// O pool é definido em META-INF/context.xml com o nome "jdbc/microblog"
public class DatabaseConnector {

    private static DataSource dataSource;

    static {
        try {
            Context initCtx = new InitialContext();
            Context envCtx  = (Context) initCtx.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/microblog");
        } catch (NamingException e) {
            throw new RuntimeException("Falha ao obter DataSource via JNDI", e);
        }
    }

    private DatabaseConnector() {}

    // Retorna uma conexão do pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}