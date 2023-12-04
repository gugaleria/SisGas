import javafx.application.Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class ConexaoBD {

    // Configuração de parâmetros
    static final String jdbcUrl = "jdbc:mysql://localhost:3306/sisgas";
    static final String username = "developer";
    static final String password = "34629469";

    public static void main(String[] args) {
        try {
            // Estabelecer a conexão
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Conexão bem-sucedida!");

            // Adicione aqui o código para realizar operações no banco de dados, se necessário

            Application.launch(InterfaceJavaFX.class, args);

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
