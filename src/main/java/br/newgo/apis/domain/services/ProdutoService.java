package br.newgo.apis.domain.services;

import br.newgo.apis.infrastructure.entities.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.application.utils.ProdutoAtributos;
import br.newgo.apis.application.dtos.ProdutoDTO;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static br.newgo.apis.application.utils.ProdutoAtributos.*;
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
        validarProdutoDTO(produtoDTO, ATRIBUTOS_OBRIGATORIOS);
        return salvarEObterDto(produtoDTO);
    }

    public List<ProdutoDTO> criarLote(List<ProdutoDTO> produtos){
        return produtos.stream().peek(produto -> validarProdutoDTO(produto, ATRIBUTOS_OBRIGATORIOS))
                .map(this::salvarEObterDto)
                .collect(Collectors.toList());
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
        validarProdutoDTO(produtoDTO, ATRIBUTO_STATUS);
        if(!produtoDAO.atualizarStatusLativo(Boolean.parseBoolean(produtoDTO.getLativo()), obterPorHash(hash).getHash()))
            throw new NoSuchElementException("Status não atualizado.");
        return mapearParaDTO(obterPorHash(hash));
    }

    public ProdutoDTO atualizar(String hash, ProdutoDTO produtoDTO){
        Produto produto = obterPorHash(hash);
        produtoValidador.validarSeProdutoEstaAtivo(produto.isLativo());
        validarProdutoDTO(produtoDTO, ATRIBUTOS_ATUALIZAVEIS);
        atualizarInformacoesProduto(produto, produtoDTO);
        produtoDAO.atualizar(produto);

        return mapearParaDTO(obterPorHash(hash));
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

    private void validarProdutoDTO(ProdutoDTO produtoDTO, ProdutoAtributos atributos) {
        if(atributos == ATRIBUTO_STATUS)
            produtoValidador.verificarLativo(produtoDTO);
        else
            produtoValidador.validarProduto(produtoDTO);
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
        if (produto == null) {
            throw new NoSuchElementException("Produto não encontrado.");
        }
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
}