package br.newgo.apis.domain.services;

import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.presentation.dtos.ProdutoDTO;

import java.util.UUID;

/**
 * Classe responsável por validar dados relacionados a Entidade Produto.
 */
public class ProdutoValidacao {

    private final ProdutoDAO produtoDAO;
    public ProdutoValidacao(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    /**
     * Valida um objeto Produto, verificando se o nome não é nulo ou vazio, se não existe um produto com o mesmo nome
     * ou código EAN-13 no banco de dados e se os valores de preço, quantidade e estoque mínimo não são negativos.
     *
     * @param produto O objeto Produto a ser validado.
     * @throws IllegalArgumentException Se o produto for inválido.
     */
    public void validarProduto(ProdutoDTO produto) {
        validarNomeNuloOuVazio(produto.getNome());
        validarNomeOuEan13Duplicado(produto.getNome(), produto.getEan13());
        validarValoresNegativos(produto.getPreco(), produto.getQuantidade(), produto.getEstoqueMin());
    }

    /**
     * Valida se o nome é nulo ou vazio.
     *
     * @param nome O nome a ser validado.
     * @throws IllegalArgumentException Se o nome for nulo ou vazio.
     */
    private void validarNomeNuloOuVazio(String nome)  {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Não é possível cadastrar um produto sem nome.");
        }
    }

    /**
     * Valida se já existe um produto com o mesmo nome ou código EAN-13 no banco de dados.
     *
     * @param nome  O nome do produto a ser validado.
     * @param ean13 O código EAN-13 do produto a ser validado.
     * @throws IllegalArgumentException Se um produto com o mesmo nome ou EAN-13 já existir no banco de dados.
     */
    private void validarNomeOuEan13Duplicado(String nome, String ean13)  {
        if (produtoDAO.existeProdutoComNomeOuEan13(nome, ean13)) {
            throw new IllegalArgumentException("Já existe um produto com o mesmo nome ou EAN-13.");
        }
    }

    /**
     * Valida se os valores (preço, quantidade e estoque mínimo) são negativos.
     *
     * @param preco       O preço a ser validado.
     * @param quantidade  A quantidade a ser validada.
     * @param estoqueMin  O estoque mínimo a ser validado.
     * @throws IllegalArgumentException Se algum dos valores (preço, quantidade ou estoque mínimo) for negativo.
     */
    private void validarValoresNegativos(double preco, double quantidade, double estoqueMin)  {
        if (preco < 0 || quantidade < 0 || estoqueMin < 0) {
            throw new IllegalArgumentException("Não é permitido cadastrar produtos com preço, quantidade ou estoque mínimo negativos.");
        }
    }

    /**
     * Valida o formato de um hash.
     *
     * @param hash O hash a ser validado.
     * @return O UUID do hash validado.
     * @throws IllegalArgumentException Se o formato do hash for inválido.
     */
    public UUID validarHash(String hash) {
        try {
            return UUID.fromString(hash);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de hash inválido.", e);
        }
    }
}