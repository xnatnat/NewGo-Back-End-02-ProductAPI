package br.newgo.apis.infrastructure.dao;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.ConexaoBancoDados;
import br.newgo.apis.infrastructure.sql.ProdutoSQL;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Esta classe representa um DAO (Data Access Object) para a entidade Produto.
 */
public class ProdutoDAO {
    ProdutoSQL produtoSQL;

    public ProdutoDAO() {
        produtoSQL = new ProdutoSQL();
    }

    /**
     * Salva um produto no banco de dados e retorna o valor do hash gerado durante a inserção.
     *
     * @param produto O objeto Produto a ser salvo no banco de dados.
     * @return O valor do hash gerado durante a inserção no banco de dados.
     * @throws RuntimeException Se ocorrer um erro ao salvar o produto ou ao obter o hash gerado.
     */
    public String salvar(Produto produto) {
        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(produtoSQL.inserir(), Statement.RETURN_GENERATED_KEYS)) {

            setDadosDoProdutoParaInsercao(produto, stmt);
            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getString(2);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar o produto: " + e.getMessage(), e);
        }
        return "";
    }

    /**
     * Verifica se existe um produto com um determinado nome ou EAN13 no banco de dados.
     *
     * @param nome  O nome do produto a ser verificado.
     * @param ean13 O código EAN13 do produto a ser verificado.
     * @return True se um produto com o nome ou EAN13 especificado existe no banco de dados, caso contrário, False.
     * @throws RuntimeException Se ocorrer um erro durante a execução da consulta no banco de dados.
     */
    public boolean existeProdutoComNomeOuEan13(String nome, String ean13) {
        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(produtoSQL.existeProdutoComNomeOuEan13())) {

            stmt.setString(1, nome);
            stmt.setString(2, ean13);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                } else
                    return false;
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao executar a consulta: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter a conexão com o banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Busca um produto no banco de dados com base no seu hash (UUID).
     *
     * @param hash O UUID que identifica exclusivamente o produto a ser buscado.
     * @return Um objeto Produto representando o produto encontrado, ou null se não encontrado.
     * @throws RuntimeException Se ocorrer um erro ao executar a consulta SQL ou ao obter a conexão com o banco de dados.
     */
    public Produto buscarPorHash(UUID hash) {
        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(produtoSQL.buscarPorHash())) {

            stmt.setObject(1, hash);

            try (ResultSet resultado = stmt.executeQuery()) {
                if (resultado.next()) {
                    return criarProduto(resultado);
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao executar a consulta: " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter a conexão com o banco de dados: " + e.getMessage(), e);
        }
    }

    /**
     * Configura os parâmetros do PreparedStatement para inserir um Produto no banco de dados.
     * @param produto O objeto Produto a ser inserido.
     * @param stmt O PreparedStatement a ser configurado.
     * @throws SQLException Se ocorrer um erro ao configurar os parâmetros.
     */
    private void setDadosDoProdutoParaInsercao(Produto produto, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, produto.getNome());
        stmt.setString(2, produto.getDescricao());
        stmt.setString(3, produto.getEan13());
        stmt.setDouble(4, produto.getPreco());
        stmt.setDouble(5, produto.getQuantidade());
        stmt.setDouble(6, produto.getEstoqueMin());
        stmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        stmt.setNull(8, Types.TIMESTAMP);
        stmt.setBoolean(9, false);
    }

    /**
     * Cria um objeto Produto com base nos dados de um ResultSet.
     *
     * @param resultado O ResultSet contendo os dados do produto.
     * @return Um objeto Produto preenchido com os dados do ResultSet.
     * @throws SQLException Se ocorrer um erro ao acessar os dados do ResultSet.
     */
    private Produto criarProduto(ResultSet resultado) throws SQLException {
        Produto produto = new Produto(
                resultado.getString("nome"),
                resultado.getString("descricao"),
                resultado.getString("ean13"),
                resultado.getDouble("preco"),
                resultado.getDouble("quantidade"),
                resultado.getDouble("estoque_min"));
        produto.setHash(UUID.fromString(resultado.getString("hash")));
        produto.setLativo(resultado.getBoolean("lativo"));
        produto.setDtCreate(resultado.getTimestamp("dtcreate").toLocalDateTime());
        LocalDateTime dtUpdate = resultado.getTimestamp("dtupdate") != null
                ? resultado.getTimestamp("dtupdate").toLocalDateTime()
                : null;

        return produto;
    }
}