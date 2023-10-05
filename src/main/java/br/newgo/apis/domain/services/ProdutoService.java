package br.newgo.apis.domain.services;

import br.newgo.apis.application.dtos.AtualizacaoLoteProdutoDTO;
import br.newgo.apis.infrastructure.entities.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.application.dtos.ProdutoDTO;

import java.util.List;
import java.util.NoSuchElementException;

import static br.newgo.apis.application.utils.ProdutoMapeador.*;

/**
 * Classe que fornece serviços relacionados a produtos.
 */
public class ProdutoService {
    private final ProdutoDAO produtoDAO;
    private final ProdutoValidador produtoValidador;

    public ProdutoService(ProdutoDAO produtoDAO){
        this.produtoDAO = produtoDAO;
        this.produtoValidador = new ProdutoValidador(produtoDAO);
    }

    public ProdutoDTO criar(ProdutoDTO produtoDTO) {
        produtoValidador.validarAtributosCriarProduto(produtoDTO);
        return salvarEObterDto(produtoDTO);
    }

    /**
     * Obtém um objeto ProdutoDTO com base no hash fornecido.
     *
     * @param hash O hash do produto a ser obtido.
     * @return Um objeto ProdutoDTO representando o produto correspondente ao hash.
     */
    public ProdutoDTO obterDtoPorHash(String hash){
        return mapearParaDTO(obterPorHash(hash));
    }

    /**
     * Obtém um objeto ProdutoDTO ativo com base no hash fornecido.
     *
     * @param hash O hash do produto a ser obtido.
     * @return Um objeto ProdutoDTO representando o produto ativo correspondente ao hash.
     * @throws NoSuchElementException Se o produto não estiver ativo.
     */
    public ProdutoDTO obterDtoAtivoPorHash(String hash){
        ProdutoDTO produto = obterDtoPorHash(hash);
        produtoValidador.validarSeProdutoEstaAtivo(Boolean.parseBoolean(produto.getLativo()));

        return produto;
    }

    /**
     * Obtém uma lista de todos os produtos disponíveis no sistema.
     *
     * @return Uma lista de objetos ProdutoDTO representando todos os produtos.
     */
    public List<ProdutoDTO> obterTodos(){
        return mapearParaListaDeDTOS(produtoDAO.buscarTodos());
    }

    /**
     * Obtém uma lista de todos os produtos com base no status (ativo/inativo) fornecido.
     *
     * @param status O status (true para ativo, false para inativo) dos produtos a serem obtidos.
     * @return Uma lista de objetos ProdutoDTO representando os produtos com o status especificado.
     */
    public List<ProdutoDTO> obterTodosPorStatus(String status) {
        return mapearParaListaDeDTOS(
                produtoDAO.buscarTodosPorStatus(
                            Boolean.parseBoolean(status)));
    }

    public List<ProdutoDTO> obterTodosComEstoqueBaixo(){
        return mapearParaListaDeDTOS(produtoDAO.buscarTodosComEstoqueBaixo());
    }

    public ProdutoDTO atualizarStatusLativo(String hash, ProdutoDTO produtoDTO){
        produtoValidador.verificarLativo(produtoDTO);
        produtoDAO.atualizarStatusLativo(Boolean.valueOf(produtoDTO.getLativo()), obterPorHash(hash).getHash());

        return mapearParaDTO(obterPorHash(hash));
    }

    public ProdutoDTO atualizar(String hash, ProdutoDTO produtoDTO){
        Produto produto = obterPorHash(hash);

        produtoValidador.validarSeProdutoEstaAtivo(produto.isLativo());
        produtoValidador.validarAtributosDoubleEmProdutoDTO(produtoDTO);

        atualizarInformacoesProduto(produto, produtoDTO);

        produtoDAO.atualizar(produto);

        return mapearParaDTO(obterPorHash(hash));
    }

    public ProdutoDTO atualizarPrecoEmLote(AtualizacaoLoteProdutoDTO atualizacaoLoteProdutoDTO){

        Produto produto = obterPorHash(atualizacaoLoteProdutoDTO.getHash());

        produtoValidador.validarSeProdutoEstaAtivo(produto.isLativo());
        produtoValidador.validarDoubleNegativo(atualizacaoLoteProdutoDTO.getValor(), "valor");

        double valor = obterNovoPreco(produto.getPreco(),
                                        atualizacaoLoteProdutoDTO.getOperacao(),
                                        atualizacaoLoteProdutoDTO.getValor());

        produtoDAO.atualizarAtributoDouble("preco", valor, produto.getHash());

        return mapearParaDTO(obterPorHash(produto.getHash().toString()));
    }

    public ProdutoDTO atualizarEstoqueEmLote(AtualizacaoLoteProdutoDTO atualizacaoLoteProdutoDTO) {

        Produto produto = obterPorHash(atualizacaoLoteProdutoDTO.getHash());

        produtoValidador.validarSeProdutoEstaAtivo(produto.isLativo());
        produtoValidador.validarDoubleZero(atualizacaoLoteProdutoDTO.getValor(), "valor");

        double valor = obterNovoValorEstoque(produto.getQuantidade(), atualizacaoLoteProdutoDTO.getValor());

        produtoDAO.atualizarAtributoDouble("quantidade", valor, produto.getHash());

        return mapearParaDTO(obterPorHash(produto.getHash().toString()));
    }

        /**
         * Deleta um produto com base em seu hash.
         *
         * @param hash O hash do produto a ser deletado.
         * @throws NoSuchElementException Se o produto não puder ser deletado.
         */
    public void deletar(String hash) {
        if(!produtoDAO.deletar(produtoValidador.validarERetornarHash(hash)))
            throw new NoSuchElementException("Produto não deletado.");
    }

    private ProdutoDTO salvarEObterDto(ProdutoDTO produtoDTO){
        return obterDtoPorHash(
                produtoDAO.salvar(
                        mapearParaProduto(
                                produtoDTO)));
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
        Produto produto = produtoDAO.buscarPorHash(produtoValidador.validarERetornarHash(hash));
        if (produto == null)
            throw new NoSuchElementException("Produto não encontrado.");

        return produto;
    }

    private void atualizarInformacoesProduto(Produto produto, ProdutoDTO produtoDTO) {
        if(produtoDTO.getDescricao() != null && !produtoDTO.getDescricao().equals(produto.getDescricao()))
            produto.setDescricao(produtoDTO.getDescricao());

        if(produtoDTO.getPreco() != 0 && produtoDTO.getPreco() != produto.getPreco())
            produto.setPreco(produtoDTO.getPreco());

        if(produtoDTO.getQuantidade() != 0 && produtoDTO.getQuantidade() != produto.getQuantidade())
            produto.setQuantidade(produtoDTO.getQuantidade());

        if(produtoDTO.getEstoqueMin() != 0 && produtoDTO.getEstoqueMin() != produto.getEstoqueMin())
            produto.setEstoqueMin(produtoDTO.getEstoqueMin());
    }

    private double obterNovoPreco(double valorAtual, String operacao, double valor){
        double novoPreco = 0;

        if(operacao.equalsIgnoreCase("fixo"))
            novoPreco = valor;
        else if (operacao.equalsIgnoreCase("aumentar-valor"))
            novoPreco = valorAtual + valor;
        else if (operacao.equalsIgnoreCase("diminuir-valor"))
            novoPreco = valorAtual - valor;
        else if (operacao.equalsIgnoreCase("aumentar-percentualmente"))
            novoPreco = valorAtual + (valorAtual * (valor/100));
        else if (operacao.equalsIgnoreCase("diminuir-percentualmente"))
            novoPreco = valorAtual - (valorAtual * (valor/100));
        else
            throw new IllegalArgumentException("A operação '" + operacao + "' é inválida!");

        produtoValidador.validarDoubleNegativo(novoPreco, "novo preço");

        return novoPreco;
    }

    private double obterNovoValorEstoque(double quantidadeAtual, double valor){
        double novoValor = quantidadeAtual + (valor);

        produtoValidador.validarDoubleNegativo(novoValor, "novo estoque");

        return novoValor;
    }
}