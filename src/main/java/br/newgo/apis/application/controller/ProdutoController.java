package br.newgo.apis.application.controller;

import br.newgo.apis.application.utils.ProdutoMapeador;
import br.newgo.apis.domain.services.JsonProdutoValidador;
import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.application.dtos.ProdutoDTO;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

import static br.newgo.apis.application.utils.JsonMapeador.mapearParaObjetoJson;
import static br.newgo.apis.application.utils.JsonMapeador.mapearParaStreamDeObjetosJson;
import static br.newgo.apis.application.utils.ProdutoAtributos.*;

public class ProdutoController{
    private final ProdutoService produtoService;
    private final JsonProdutoValidador jsonProdutoValidador;

    public ProdutoController() {
        produtoService = new ProdutoService(new ProdutoDAO());
        jsonProdutoValidador = new JsonProdutoValidador();
    }

    public ProdutoDTO criar(String jsonRequisicao) {
        JsonObject objetoJson = mapearParaObjetoJson(jsonRequisicao);
        jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTOS_OBRIGATORIOS);

        return produtoService.criar(ProdutoMapeador.mapearParaDTO(objetoJson));
    }

    public List<ProdutoDTO> criarLote(String jsonRequisicao) {
        return produtoService.criarLote(mapearParaStreamDeObjetosJson(jsonRequisicao)
                                .peek(objetoJson -> jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTOS_OBRIGATORIOS))
                                .map(ProdutoMapeador::mapearParaDTO)
                                .collect(Collectors.toList()));
    }

    public List<ProdutoDTO> obterTodos() {
        return produtoService.obterTodos();
    }

    public List<ProdutoDTO> obterTodosPorStatus(String status) {
        return produtoService.obterTodosPorStatus(status);
    }

    public List<ProdutoDTO> obterTodosComEstoqueBaixo() {
        return produtoService.obterTodosComEstoqueBaixo();
    }

    public ProdutoDTO obterDtoAtivoPorHash(String hash) {
        return produtoService.obterDtoAtivoPorHash(hash);
    }

    public ProdutoDTO obterDtoPorHash(String hash) {
        return produtoService.obterDtoPorHash(hash);
    }

    public ProdutoDTO atualizar(String hash, String jsonRequisicao) {
        JsonObject objetoJson = mapearParaObjetoJson(jsonRequisicao);
        jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTOS_ATUALIZAVEIS);

        return produtoService.atualizar(hash, ProdutoMapeador.mapearParaDTO(objetoJson));
    }

    public ProdutoDTO atualizarStatus(String hash, String jsonRequisicao) {
        JsonObject objetoJson = mapearParaObjetoJson(jsonRequisicao);
        jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTO_STATUS);

        return produtoService.atualizarStatusLativo(hash, ProdutoMapeador.mapearParaDTO(objetoJson));
    }

    public void deletar(String hash) {
        produtoService.deletar(hash);
    }
}
