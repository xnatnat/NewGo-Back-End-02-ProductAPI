package br.newgo.apis.domain.model.services;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;

/**
 * Classe responsável por validar objetos da classe Produto antes de serem armazenados no banco de dados.
 */
public class ProdutoValidacao {

    private ProdutoDAO produtoDAO;
    public ProdutoValidacao(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    /**
     * Valida um produto verificando seu nome, existência no banco de dados e valores negativos.
     * @param produto O produto a ser validado.
     * @throws IllegalArgumentException Se o nome for nulo ou vazio, se um produto com o mesmo nome ou EAN-13 já existe no banco de dados
     *                                  ou se algum dos valores (preço, quantidade ou estoque mínimo) for negativo.
     */
    public void validarProduto(Produto produto) {

        validarNome(produto);
        validarExistenciaDoProduto(produto);
        validarValoresNegativos(produto);

    }

    /**
     * Valida o nome de um produto, garantindo que não seja nulo ou vazio.
     * @param produto O produto a ser validado.
     * @throws IllegalArgumentException Se o nome do produto for nulo ou vazio.
     */
    private void validarNome(Produto produto)  {
        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("Não é possível cadastrar um produto sem nome.");
        }
    }

    /**
     * Valida a existência de um produto com o mesmo nome ou código EAN-13 no banco de dados.
     * @param produto O produto a ser validado.
     * @throws IllegalArgumentException Se um produto com o mesmo nome ou EAN-13 já existir no banco de dados.
     */
    private void validarExistenciaDoProduto(Produto produto)  {
        if (produtoDAO.existeProdutoComNomeOuEan13(produto.getNome(), produto.getEan13())) {
            throw new IllegalArgumentException("Já existe um produto com o mesmo nome ou EAN-13.");
        }
    }

    /**
     * Valida valores negativos para preço, quantidade e estoque mínimo de um produto.
     * @param produto O produto a ser validado.
     * @throws IllegalArgumentException Se algum dos valores (preço, quantidade ou estoque mínimo) for negativo.
     */
    private void validarValoresNegativos(Produto produto)  {
        if (produto.getPreco() < 0 || produto.getQuantidade() < 0 || produto.getEstoqueMin() < 0) {
            throw new IllegalArgumentException("Não é permitido cadastrar produtos com preço, quantidade ou estoque mínimo negativos.");
        }
    }

}