package br.newgo.apis.domain.services;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.ProdutoMapeador;
import br.newgo.apis.presentation.dtos.ProdutoDTO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;
    private final ProdutoValidacao produtoValidacao;
    private final ProdutoMapeador produtoMapeador;
    private final JsonValidacao jsonValidacao;

    public ProdutoService(ProdutoDAO produtoDAO){
        this.produtoDAO = produtoDAO;
        this.produtoValidacao = new ProdutoValidacao(produtoDAO);
        this.produtoMapeador = new ProdutoMapeador();
        this.jsonValidacao = new JsonValidacao();
    }

    /**
     * Cria um novo produto a partir dos dados inseridos.
     *
     * @param produtoInserido O JSON representando o produto a ser criado.
     * @return Um objeto ProdutoDTO representando o produto criado.
     * @throws IllegalArgumentException Se o JSON inserido for inválido ou se algum dos dados do produto não atender às regras de validação.
     */
    public ProdutoDTO criar(String produtoInserido) {
        jsonValidacao.validarJson(produtoInserido);

        ProdutoDTO produtoDTO = produtoValidacao.validarProduto(ProdutoDTO.fromJson(produtoInserido));

        Produto produto = produtoMapeador.converterParaProduto(produtoDTO);

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
        UUID uuid = produtoValidacao.validarHash(hash);
        Produto produto = produtoDAO.buscarPorHash(uuid);

        if (produto == null) {
            throw new NoSuchElementException("Produto não encontrado.");
        }

        return produto;
    }

    /**
     * Obtém um objeto ProdutoDTO com base no hash fornecido.
     *
     * @param hash O hash do produto a ser obtido.
     * @return Um objeto ProdutoDTO representando o produto correspondente ao hash.
     */
    public ProdutoDTO obterDtoPorHash(String hash){
        return produtoMapeador.converterParaDTO(obterPorHash(hash));
    }

    /**
     * Obtém uma lista de todos os produtos disponíveis no sistema.
     *
     * @return Uma lista de objetos ProdutoDTO representando todos os produtos.
     */
    public List<ProdutoDTO> obterTodos(){
        return produtoDAO.buscarTodos().stream()
                .map(ProdutoDTO::new)
                .collect(Collectors.toList());
    }

    public void deletar(String hash) {
        if(!produtoDAO.deletar(produtoValidacao.validarHash(hash)))
            throw new NoSuchElementException("Produto não encontrado.");
    }
}