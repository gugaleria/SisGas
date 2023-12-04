import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private Connection connection;

    // Construtor que recebe a conexão como parâmetro
    public ProdutoDAO(Connection connection) {
        this.connection = connection;
    }



    // Método para cadastrar um novo produto no banco de dados
    public boolean cadastrarProduto(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());

            int linhasAfetadas = statement.executeUpdate();

            // Se uma linha foi afetada, o produto foi inserido com sucesso
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para remover um produto do banco de dados
    public boolean removerProduto(int idProduto) {
        String sql = "DELETE FROM produtos WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProduto);

            int linhasAfetadas = statement.executeUpdate();

            // Se uma linha foi afetada, o produto foi removido com sucesso
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao remover produto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para recuperar todos os produtos do banco de dados
    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM produtos";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Produto produto = new Produto();
                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setPreco(resultSet.getDouble("preco"));
                produto.setQuantidade(resultSet.getInt("quantidade"));

                produtos.add(produto);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
            e.printStackTrace();
        }

        return produtos;
    }
}
