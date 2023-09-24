package br.newgo.apis.infrastructure;

import br.newgo.apis.infrastructure.config.ConfigProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A classe ConexaoDB é responsável por estabelecer e gerenciar conexões com o banco de dados.
 */
public class ConexaoBancoDados {

    /**
     * Obtém uma conexão com o banco de dados utilizando as configurações definidas no arquivo de propriedades.
     *
     * @return Uma conexão com o banco de dados.
     * @throws RuntimeException Se ocorrer um erro ao obter a conexão.
     */
    public static Connection obterConexao() {
        carregarDriverBancoDados();

        String url = ConfigProperties.getProperty("db.url");
        String usuario = ConfigProperties.getProperty("db.usuario");
        String senha = ConfigProperties.getProperty("db.senha");

        try {
            return DriverManager.getConnection(url, usuario, senha);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão com o banco de dados.", e);
        }
    }

    /**
     * Fecha a conexão com o banco de dados.
     *
     * @param conexao A conexão a ser fechada.
     * @throws RuntimeException Se ocorrer um erro ao fechar a conexão.
     */
    public static void fecharConexao(Connection conexao) {
        if (conexao != null) {
            try {
                conexao.close();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar a conexão com o banco de dados.", e);
            }
        }
    }

    private static void carregarDriverBancoDados() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do banco de dados não encontrado.", e);
        }
    }
}