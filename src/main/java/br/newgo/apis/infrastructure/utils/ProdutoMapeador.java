package br.newgo.apis.infrastructure.utils;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.presentation.dtos.ProdutoDTO;

/**
 * A classe ProdutoMapeador é responsável por converter objetos da classe Produto
 * para objetos da classe ProdutoDTO e vice-versa.
 */
public class ProdutoMapeador {
    /**
     * Converte um objeto da classe Produto para um objeto da classe ProdutoDTO.
     *
     * @param produto O objeto da classe Produto a ser convertido.
     * @return Um objeto da classe ProdutoDTO contendo os mesmos dados do objeto Produto original.
     */
    public ProdutoDTO converterParaDTO(Produto produto){
        return new ProdutoDTO(produto);
    }

    /**
     * Converte um objeto da classe ProdutoDTO para um objeto da classe Produto.
     *
     * @param produtoDTO O objeto da classe ProdutoDTO a ser convertido.
     * @return Um objeto da classe Produto contendo os mesmos dados do objeto ProdutoDTO original.
     */
    public Produto converterParaProduto(ProdutoDTO produtoDTO){
        return new Produto(
                produtoDTO.getNome(), produtoDTO.getDescricao(), produtoDTO.getEan13(), produtoDTO.getPreco(), produtoDTO.getQuantidade(), produtoDTO.getEstoqueMin()
        );
    }
}
