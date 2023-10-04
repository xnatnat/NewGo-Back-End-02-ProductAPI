package br.newgo.apis.domain.services;

import br.newgo.apis.application.dtos.ProdutoDTO;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;

import java.util.UUID;

public class ProdutoValidador {
    private final ProdutoDAO produtoDAO;
    public ProdutoValidador(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void validarProduto(ProdutoDTO produto) {
        if(produto.getNome() != null) {
            validarStringNulaOuVazia(produto.getNome(), "nome");
            validarStringNulaOuVazia(produto.getEan13(), "ean13");
            validarNomeOuEan13Duplicado(produto.getNome(), produto.getEan13());
        }
        validarDoubleNegativo(produto.getPreco(), "preco");
        validarDoubleNegativo(produto.getQuantidade(), "quantidade");
        validarDoubleNegativo(produto.getEstoqueMin(), "estoqueMin");
    }

    public void validarDoubleNegativo(double valor, String atributo) {
        if(valor < 0)
            throw new IllegalArgumentException("O atributo '" + atributo + "' não pode ser negativo.");
    }

    private void validarStringNulaOuVazia(String valor, String atributo){
        if (valor == null || valor.isEmpty())
            throw new IllegalArgumentException("O atributo '"+ atributo + "' não pode ser nulo ou vazio.");
    }

    /**
     * Valida se já existe um produto com o mesmo nome ou EAN-13 no banco de dados.
     *
     * @param nome  O nome do produto.
     * @param ean13 O código EAN-13 do produto.
     * @throws IllegalArgumentException Se já existir um produto com o mesmo nome ou EAN-13.
     */
    private void validarNomeOuEan13Duplicado(String nome, String ean13)  {
        if (produtoDAO.existeProdutoComNomeOuEan13(nome, ean13)) {
            throw new IllegalArgumentException("Já existe um produto com o mesmo nome ou EAN-13.");
        }
    }

    public void verificarLativo(ProdutoDTO produtoDTO){
        String lativo = produtoDTO.getLativo();
        if (!(lativo.equalsIgnoreCase("true")||lativo.equalsIgnoreCase("false")))
            throw new IllegalArgumentException("O atributo lativo deve ter valor TRUE ou FALSE");
    }

    /**
     * Valida o formato de um hash e retorna o UUID correspondente.
     *
     * @param hash O hash a ser validado.
     * @return O UUID do hash validado.
     * @throws IllegalArgumentException Se o formato do hash for inválido.
     */
    public UUID validarERetornarHash(String hash) {
        try {
            return UUID.fromString(hash);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de hash inválido.", e);
        }
    }

    /**
     * Valida se um produto está ativo.
     *
     * @param lativo O estado de ativação do produto.
     * @throws IllegalStateException Se o produto não estiver ativo.
     */
    public void validarSeProdutoEstaAtivo(Boolean lativo){
        if(!lativo)
            throw new IllegalStateException("Produto não está ativo.");
    }
}