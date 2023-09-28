package br.newgo.apis.domain.services;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.ProdutoAtributos;
import br.newgo.apis.infrastructure.utils.ProdutoMapeador;
import br.newgo.apis.presentation.dtos.ProdutoDTO;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static br.newgo.apis.infrastructure.utils.JsonMapeador.mapearParaObjetoJson;
import static br.newgo.apis.infrastructure.utils.JsonMapeador.mapearParaObjetosJson;
import static br.newgo.apis.infrastructure.utils.ProdutoAtributos.*;
import static br.newgo.apis.infrastructure.utils.ProdutoMapeador.mapearParaDTO;
import static br.newgo.apis.infrastructure.utils.ProdutoMapeador.mapearParaProduto;

/**
 * Classe que fornece serviços relacionados a produtos.
 */
public class ProdutoService {
    private final ProdutoDAO produtoDAO;
    private final ProdutoValidador produtoValidador;
    private final ProdutoMapeador produtoMapeador;
    private final JsonProdutoValidador jsonProdutoValidador;

    public ProdutoService(ProdutoDAO produtoDAO){
        this.produtoDAO = produtoDAO;
        this.produtoValidador = new ProdutoValidador(produtoDAO);
        this.produtoMapeador = new ProdutoMapeador();
        this.jsonProdutoValidador = new JsonProdutoValidador();
    }

    /**
     * Cria novos produtos com base em um JSON de entrada.
     *
     * @param jsonInserido O JSON contendo informações dos produtos a serem criados.
     * @return Uma lista de objetos ProdutoDTO representando os produtos criados.
     */
    public List<ProdutoDTO> criar(String jsonInserido) {
        return mapearParaObjetosJson(jsonInserido)
                .peek(objetoJson -> processarJsonProduto(objetoJson, ATRIBUTOS_OBRIGATORIOS))
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
        produtoValidador.validarSeProdutoEstaAtivo(produto.isLativo());

        return produto;
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

    /**
     * Obtém uma lista de todos os produtos com base no status (ativo/inativo) fornecido.
     *
     * @param lativoParam O status (true para ativo, false para inativo) dos produtos a serem obtidos.
     * @return Uma lista de objetos ProdutoDTO representando os produtos com o status especificado.
     */
    public List<ProdutoDTO> obterTodosPorStatus(String lativoParam) {
        return produtoDAO.buscarTodosPorStatus(
                Boolean.parseBoolean(lativoParam))
                .stream()
                .map(ProdutoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza o status ativo de um produto com base em um JSON de entrada.
     *
     * @param hash O hash do produto a ser atualizado.
     * @param jsonInserido O JSON contendo o novo status.
     */
    public void atualizarStatusLativo(String hash, String jsonInserido){
        JsonObject objetoJson = mapearParaObjetoJson(jsonInserido);

        processarJsonProduto(objetoJson, ATRIBUTO_STATUS);

        produtoDAO.atualizarStatusLativo(objetoJson.get("lativo").getAsBoolean(), obterPorHash(hash).getHash());
    }

    /**
     * Atualiza informações de um produto com base em um JSON de entrada.
     *
     * @param hash O hash do produto a ser atualizado.
     * @param jsonInserido O JSON contendo as novas informações.
     * @return Um objeto ProdutoDTO representando o produto atualizado.
     * @throws NoSuchElementException Se o produto não estiver ativo.
     */
    public ProdutoDTO atualizar(String hash, String jsonInserido){
        JsonObject objetoJson = mapearParaObjetoJson(jsonInserido);

        processarJsonProduto(objetoJson, ATRIBUTOS_ATUALIZAVEIS);
        Produto produto = obterPorHash(hash);

        produtoValidador.validarSeProdutoEstaAtivo(produto.isLativo());

        atualizarInformacoesProduto(produto, objetoJson);
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

    /**
     * Salva um objeto JSON como um novo produto e retorna o correspondente ProdutoDTO.
     *
     * @param jsonObject O objeto JSON a ser convertido e salvo como um novo produto.
     * @return Um objeto ProdutoDTO representando o produto recém-criado.
     */
    private ProdutoDTO salvarEObterDto(JsonObject jsonObject){
        return obterDtoPorHash(
                produtoDAO.salvar(
                        mapearParaProduto(
                                jsonObject)));
    }

    /**
     * Processa um objeto JSON de produto de acordo com os atributos especificados.
     *
     * @param objetoJson O objeto JSON a ser processado.
     * @param atributos Os atributos a serem considerados na validação.
     */
    private void processarJsonProduto(JsonObject objetoJson, ProdutoAtributos atributos) {
        jsonProdutoValidador.validarObjetoJson(objetoJson, atributos);
        if(atributos == ATRIBUTO_STATUS)
            produtoValidador.verificarLativo(objetoJson);
        produtoValidador.validarJsonProduto(objetoJson);
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

    /**
     * Atualiza as informações de um produto com base em um objeto JSON de entrada.
     *
     * @param produto O produto a ser atualizado.
     * @param jsonObject O objeto JSON contendo as novas informações.
     */
    private void atualizarInformacoesProduto(Produto produto, JsonObject jsonObject) {
        if(jsonObject.has("descricao"))
            produto.setDescricao(jsonObject.get("descricao").getAsString());

        if(jsonObject.has("preco"))
            produto.setPreco(jsonObject.get("preco").getAsDouble());

        if(jsonObject.has("quantidade"))
            produto.setQuantidade(jsonObject.get("quantidade").getAsDouble());

        if(jsonObject.has("estoqueMin"))
            produto.setEstoqueMin(jsonObject.get("estoqueMin").getAsDouble());
    }
}