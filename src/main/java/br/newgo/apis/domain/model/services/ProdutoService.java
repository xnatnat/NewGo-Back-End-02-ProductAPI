package br.newgo.apis.domain.model.services;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.ProdutoMapeador;
import br.newgo.apis.presentation.dtos.ProdutoDTO;

import java.util.UUID;

public class ProdutoService {

    private ProdutoDAO produtoDAO;
    private ProdutoValidacao produtoValidacao;
    private ProdutoMapeador produtoMapeador;

    public ProdutoService(ProdutoDAO produtoDAO){
        this.produtoDAO = produtoDAO;
        this.produtoValidacao = new ProdutoValidacao(produtoDAO);
        this.produtoMapeador = new ProdutoMapeador();
    }

    /**
     * Cria um novo produto com base nos dados fornecidos em um objeto ProdutoDTO.
     *
     * @param produtoDTO O objeto ProdutoDTO contendo os dados do produto a ser criado.
     * @return Um objeto ProdutoDTO representando o produto criado.
     */
    public ProdutoDTO criar(ProdutoDTO produtoDTO) {

        Produto produto = produtoMapeador.converterParaProduto(produtoDTO);
        produtoValidacao.validarProduto(produto);

        return produtoMapeador.converterParaDTO(
                obterPorHash(
                        produtoDAO.salvar(produto)));
    }

    /**
     * Obtém um produto com base em seu hash (UUID).
     *
     * @param hash A representação em string do hash (UUID) do produto a ser obtido.
     * @return Um objeto Produto representando o produto com o hash correspondente.
     */
    private Produto obterPorHash(String hash){
        return produtoDAO.buscarPorHash(UUID.fromString(hash));
    }

}
