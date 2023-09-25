package br.newgo.apis.infrastructure.dao;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.ConexaoBancoDados;
import br.newgo.apis.infrastructure.sql.ProdutoSQL;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Esta classe representa um DAO (Data Access Object) para a entidade Produto.
 */
public class ProdutoDAO {

    ProdutoSQL produtoSQL;

    public ProdutoDAO() {
        produtoSQL = new ProdutoSQL();
    }

    public boolean existeProdutoComNomeOuEan13(String nome, String ean13) {
        try (Connection conexao = ConexaoBancoDados.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(produtoSQL.existeProdutoComNomeOuEan13(), Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nome);
            stmt.setString(2, ean13);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}