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
     * @param jsonInserido O JSON representando o produto a ser criado.
     * @return Um objeto ProdutoDTO representando o produto criado.
     * @throws IllegalArgumentException Se o JSON inserido for inválido ou se algum dos dados do produto não atender às regras de validação.
     */
    public ProdutoDTO criar(String jsonInserido) {
        jsonValidacao.validarJson(jsonInserido);

        ProdutoDTO produtoDTO = produtoValidacao.validarProduto(ProdutoDTO.fromJson(jsonInserido));

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

    public ProdutoDTO obterDtoPorHashSeProdutoAtivo(String hash){
        Produto produto = obterPorHash(hash);
        if(!produto.isLativo())
            throw new NoSuchElementException("Produto não está ativo.");
        return produtoMapeador.converterParaDTO(produto);
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

    public List<ProdutoDTO> obterTodosPorStatus(String lativoParam) {
        return produtoDAO.buscarTodosPorStatus(Boolean.parseBoolean(lativoParam))
                .stream()
                .map(ProdutoDTO::new)
                .collect(Collectors.toList());
    }

    public void atualizarStatusLativo(String hash, String jsonInserido){
        Boolean lativo = jsonValidacao.validarJsonStatusERetornarValor(jsonInserido);
        Produto produto = obterPorHash(hash);
        if(!produtoDAO.atualizarStatusLativo(lativo, produto.getHash()))
            throw new NoSuchElementException("Produto não atualizado.");
    }

    public ProdutoDTO atualizar(String hash, String jsonInserido){
        jsonValidacao.validarJsonAtualizacao(jsonInserido);

        Produto produto = obterPorHash(hash);

        produtoValidacao.validarSeProdutoEstaAtivo(produto);
        produto = atualizarInformacoesProduto(produto,ProdutoDTO.fromJson(jsonInserido));
        produtoValidacao.validarValoresNegativos(produto.getPreco(), produto.getQuantidade(), produto.getEstoqueMin());

        produtoDAO.atualizar(produto);

        return produtoMapeador.converterParaDTO(obterPorHash(hash));
    }

    public void deletar(String hash) {
        if(!produtoDAO.deletar(produtoValidacao.validarHash(hash)))
            throw new NoSuchElementException("Produto não deletado.");
    }

    private Produto atualizarInformacoesProduto(Produto produto, ProdutoDTO produtoDto) {
        if (produtoDto.getDescricao() != null) {
            produto.setDescricao(produtoDto.getDescricao());
        }

        if (produtoDto.getPreco() != null) {
            produto.setPreco(produtoDto.getPreco());
        }

        if (produtoDto.getQuantidade() != null) {
            produto.setQuantidade(produtoDto.getQuantidade());
        }

        if (produtoDto.getEstoqueMin() != null) {
            produto.setEstoqueMin(produtoDto.getEstoqueMin());
        }
        return produto;
    }
}