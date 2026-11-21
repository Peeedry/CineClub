package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL =
            "jdbc:mysql://localhost:3306/cineclub";

    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection conectar() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conexao =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Conectado com sucesso ao MySQL");

            return conexao;

        } catch (ClassNotFoundException e) {

            System.out.println("Driver MySQL não encontrado!");

            return null;

        } catch (SQLException e) {

            System.out.println("Erro ao conectar: " + e.getMessage());

            return null;
        }
    }
}