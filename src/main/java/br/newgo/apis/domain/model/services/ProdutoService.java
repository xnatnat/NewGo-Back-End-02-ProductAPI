package br.newgo.apis.domain.model.services;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.ProdutoMapeador;
import br.newgo.apis.presentation.dtos.ProdutoDTO;

import java.util.NoSuchElementException;
import java.util.UUID;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;
    private final ProdutoValidacao produtoValidacao;
    private final ProdutoMapeador produtoMapeador;

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
     * Obtém um produto com base no hash especificado.
     *
     * @param hash O hash do produto a ser obtido.
     * @return O produto correspondente ao hash.
     * @throws IllegalArgumentException Se o formato do hash for inválido.
     * @throws NoSuchElementException   Se o produto não for encontrado.
     */
    private Produto obterPorHash(String hash){
        UUID uuid = UUID.fromString(hash);
        Produto produto = produtoDAO.buscarPorHash(uuid);

        if (produto == null) {
            throw new NoSuchElementException("Produto não encontrado.");
        }

        return produto;
    }

    public ProdutoDTO obterDtoPorHash(String hash){
        return produtoMapeador.converterParaDTO(obterPorHash(hash));
    }
}
