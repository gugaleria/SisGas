import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class InterfaceJavaFX extends Application {
    private ProdutoDAO produtoDAO;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("SisGas - Gerenciamento de Produtos");

        // Configuração da Conexão
        Connection connection = DriverManager.getConnection(ConexaoBD.jdbcUrl, ConexaoBD.username, ConexaoBD.password);
        produtoDAO = new ProdutoDAO(connection);

        // Layout Principal
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Tabela para exibir produtos
        TableView<Produto> tabelaProdutos = criarTabelaProdutos();
        atualizarTabelaProdutos(tabelaProdutos);

        // Campos de entrada para adicionar novo produto
        TextField campoNome = new TextField();
        TextField campoPreco = new TextField();
        TextField campoQuantidade = new TextField();
        Button btnAdicionar = new Button("Adicionar Produto");
        btnAdicionar.setOnAction(e -> adicionarNovoProduto(campoNome.getText(), campoPreco.getText(), campoQuantidade.getText(), tabelaProdutos));

        // Botão para remover produto selecionado
        Button btnRemover = new Button("Remover Produto");
        btnRemover.setOnAction(e -> removerProdutoSelecionado(tabelaProdutos));

        layout.getChildren().addAll(tabelaProdutos, new Label("Nome:"), campoNome, new Label("Preço:"), campoPreco, new Label("Quantidade:"), campoQuantidade, btnAdicionar, btnRemover);

        // Configuração da Cena
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);

        // Exibição da Janela
        primaryStage.show();
    }

    private TableView<Produto> criarTabelaProdutos() {
        TableView<Produto> tabelaProdutos = new TableView<>();

        TableColumn<Produto, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

        TableColumn<Produto, Number> colunaPreco = new TableColumn<>("Preço");
        colunaPreco.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPreco()));

        TableColumn<Produto, Number> colunaQuantidade = new TableColumn<>("Quantidade");
        colunaQuantidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantidade()));

        tabelaProdutos.getColumns().addAll(colunaNome, colunaPreco, colunaQuantidade);

        return tabelaProdutos;
    }

    private void atualizarTabelaProdutos(TableView<Produto> tabelaProdutos) {
        tabelaProdutos.getItems().setAll(produtoDAO.listarProdutos());
    }

    private void adicionarNovoProduto(String nome, String preco, String quantidade, TableView<Produto> tabelaProdutos) {
        // Lógica para adicionar um novo produto
        double precoProduto = Double.parseDouble(preco);
        int quantidadeProduto = Integer.parseInt(quantidade);

        Produto novoProduto = new Produto(nome, precoProduto, quantidadeProduto);
        boolean cadastroSucesso = produtoDAO.cadastrarProduto(novoProduto);

        if (cadastroSucesso) {
            System.out.println("Produto cadastrado com sucesso!");
            atualizarTabelaProdutos(tabelaProdutos);
        } else {
            System.out.println("Erro ao cadastrar produto.");
        }
    }

    private void removerProdutoSelecionado(TableView<Produto> tabelaProdutos) {
        // Lógica para remover um produto
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();

        if (produtoSelecionado != null) {
            boolean remocaoSucesso = produtoDAO.removerProduto(produtoSelecionado.getId());

            if (remocaoSucesso) {
                System.out.println("Produto removido com sucesso!");
                atualizarTabelaProdutos(tabelaProdutos);
            } else {
                System.out.println("Erro ao remover produto.");
            }
        } else {
            System.out.println("Nenhum produto selecionado para remoção.");
        }
    }
}
