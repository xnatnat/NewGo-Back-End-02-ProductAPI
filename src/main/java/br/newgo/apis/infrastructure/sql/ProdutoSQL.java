package br.newgo.apis.infrastructure.sql;

/**
 * A classe ProdutoSQL define consultas SQL pré-definidas para interagir com a tabela de produtos em um banco de dados.
 * Ela fornece consultas para inserir, atualizar, verificar a existência, buscar por hash, buscar todos e deletar registros na tabela de produtos.
 */
public class ProdutoSQL {

    /**
     * Retorna uma consulta SQL para inserir um novo produto na tabela de produtos.
     *
     * @return Uma string contendo a consulta SQL de inserção.
     */
    public String inserir(){
        return "INSERT INTO PRODUTOS " +
                "(nome, descricao, ean13, preco, quantidade, estoque_min, dtcreate, dtupdate, lativo)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

}
