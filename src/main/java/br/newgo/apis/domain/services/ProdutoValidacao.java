package br.newgo.apis.domain.services;

import br.newgo.apis.domain.model.Produto;
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
    public ProdutoDTO validarProduto(ProdutoDTO produto) {
        validarNomeNuloOuVazio(produto.getNome());
        validarNomeOuEan13Duplicado(produto.getNome(), produto.getEan13());
        produto = validarValoresNulos(produto);
        validarValoresNegativos(produto.getPreco(), produto.getQuantidade(), produto.getEstoqueMin());
        return produto;
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
    public void validarValoresNegativos(double preco, double quantidade, double estoqueMin)  {
        if (preco < 0 || quantidade < 0 || estoqueMin < 0) {
            throw new IllegalArgumentException("Não é permitido cadastrar produtos com preço, quantidade ou estoque mínimo negativos.");
        }
    }

    /**
     * Valida e atualiza os valores nulos em um objeto ProdutoDTO, atribuindo 0.0 se forem nulos.
     *
     * @param produto O objeto ProdutoDTO a ser validado e atualizado.
     * @return O objeto ProdutoDTO após a validação e atualização dos valores nulos.
     */
    private ProdutoDTO validarValoresNulos(ProdutoDTO produto){
        if (produto.getPreco() == null) {
            produto.setPreco(0.0);
        }
        if (produto.getQuantidade() == null) {
            produto.setQuantidade(0.0);
        }
        if (produto.getEstoqueMin() == null) {
            produto.setEstoqueMin(0.0);
        }
        return produto;
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

    public void validarSeProdutoEstaAtivo(Produto produto){
        if(!produto.isLativo())
            throw new IllegalStateException("Produto não está ativo.");
    }
}