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

    /**
     * Retorna uma consulta SQL para verificar se existe um produto com um determinado nome ou código EAN-13 na tabela de produtos.
     *
     * @return Uma string contendo a consulta SQL para verificar a existência.
     */
    public String existeProdutoComNomeOuEan13(){
        return "SELECT COUNT(*) FROM PRODUTOS WHERE nome = ? OR ean13 = ?";
    }
    
    /**
    * Gera uma consulta SQL para buscar produtos no banco de dados com base em um critério específico.
    *
    * @return Uma string contendo a consulta SQL para buscar produtos com base no critério fornecido.
    */
    public String buscarPor(String coluna) {
        return "SELECT * FROM PRODUTOS WHERE " + coluna + " = ?";
    }

    public String atualizarCampo(String campo) {
        return "UPDATE PRODUTOS SET " + campo + " = ?, dtupdate=? WHERE hash = ?";
    }

    public String atualizar(){
        return "UPDATE PRODUTOS SET descricao = ?, preco=?, quantidade=?, estoque_min=?, dtupdate=? WHERE hash = ?";
    }
  
    /**
     * Retorna uma consulta SQL para buscar todos os produtos na tabela de produtos.
     *
     * @return Uma string contendo a consulta SQL de busca de todos os produtos.
     */
    public String buscarTodos(){
        return "SELECT * FROM PRODUTOS";
    }
    
    /**
    * Gera uma consulta SQL para buscar todos os produtos com estoque abaixo do valor mínimo.
    *
    * @return Uma string contendo a consulta SQL para buscar produtos com estoque abaixo do valor mínimo.
    */
    public String buscarTodosComEstoqueBaixo() {
        return "SELECT * FROM PRODUTOS WHERE lativo = true AND quantidade < estoque_min";}

    /**
     * Retorna uma consulta SQL para deletar um produto da tabela de produtos com base no ID.
     *
     * @return Uma string contendo a consulta SQL de deleção.
     */
    public String deletar(){
        return "DELETE FROM PRODUTOS WHERE hash = ?";
    }
}