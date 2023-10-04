package br.newgo.apis.application.controller;

import br.newgo.apis.application.dtos.RespostaDTO;
import br.newgo.apis.application.utils.ProdutoMapeador;
import br.newgo.apis.domain.services.JsonProdutoValidador;
import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.application.dtos.ProdutoDTO;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static br.newgo.apis.application.utils.JsonMapeador.*;
import static br.newgo.apis.application.utils.ProdutoAtributos.*;
import static br.newgo.apis.application.utils.ProdutoMapeador.mapearParaPrecoLoteDTO;

public class ProdutoController{
    private final ProdutoService produtoService;
    private final JsonProdutoValidador jsonProdutoValidador;

    public ProdutoController() {
        produtoService = new ProdutoService(new ProdutoDAO());
        jsonProdutoValidador = new JsonProdutoValidador();
    }

    public RespostaDTO<Object> criar(String jsonRequisicao) {
        ProdutoDTO produtoDTO = salvarProduto(mapearParaObjetoJson(jsonRequisicao));
        return new RespostaDTO<Object>("Sucesso", "Produto cadastrado com sucesso.", produtoDTO);
    }

    public List<RespostaDTO<Object>> criarLote(String jsonRequisicao) {
        List<RespostaDTO<Object>> respostasDTO  = new ArrayList<>();

        for(JsonObject objetoJson: mapearParaListaDeObjetosJson(jsonRequisicao)){
            try{
                ProdutoDTO produtoDTO =  salvarProduto(objetoJson);
                addRespostaSucesso(produtoDTO, "Produto cadastrado com sucesso.", respostasDTO);

            }catch (IllegalArgumentException e){
                addRespostaErro(objetoJson, e.getMessage(), respostasDTO);
            }
        }
        return respostasDTO;
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

    public RespostaDTO<Object> atualizar(String hash, String jsonRequisicao) {
        JsonObject objetoJson = mapearParaObjetoJson(jsonRequisicao);
        jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTOS_ATUALIZAVEIS);
        ProdutoDTO produtoDTO = produtoService.atualizar(hash, ProdutoMapeador.mapearParaDTO(objetoJson));
        return new RespostaDTO<Object>("Sucesso", "Produto atualizado com sucesso.", produtoDTO);
    }

    public RespostaDTO<Object> atualizarStatus(String hash, String jsonRequisicao) {
        JsonObject objetoJson = mapearParaObjetoJson(jsonRequisicao);
        jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTO_STATUS);
        ProdutoDTO produtoDTO = produtoService.atualizarStatusLativo(hash, ProdutoMapeador.mapearParaDTO(objetoJson));

        return new RespostaDTO<Object>("Sucesso", "Produto atualizado com sucesso.", produtoDTO);
    }

    public List<RespostaDTO<Object>> atualizarPrecoLote(String jsonRequisicao) {
        List<RespostaDTO<Object>> respostasDTO = new ArrayList<>();
        for (JsonObject objetoJson : mapearParaListaDeObjetosJson(jsonRequisicao)) {
            try {
                jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTOS_OBRIGATORIOS_ATUALIZAR_PRECO_LOTE);
                ProdutoDTO produtoDTO = produtoService.atualizarPrecoEmLote(mapearParaPrecoLoteDTO(objetoJson));
                addRespostaSucesso(produtoDTO, "Preço atualizado com sucesso.", respostasDTO);

            }catch (IllegalArgumentException | IllegalStateException e){
                addRespostaErro(objetoJson, e.getMessage(), respostasDTO);
            }
        }
        return respostasDTO;
    }

    public void deletar(String hash) {
        produtoService.deletar(hash);
    }

    private ProdutoDTO salvarProduto(JsonObject objetoJson){
        jsonProdutoValidador.validarObjetoJson(objetoJson, ATRIBUTOS_OBRIGATORIOS_SALVAR);
        return produtoService.criar(ProdutoMapeador.mapearParaDTO(objetoJson));
    }

    private void addRespostaSucesso(ProdutoDTO produtoDTO, String mensagem, List<RespostaDTO<Object>> respostasDTO){
        respostasDTO.add(new RespostaDTO<Object>("Sucesso",mensagem, produtoDTO));
    }

    private void addRespostaErro(JsonObject objetoJson, String mensagem, List<RespostaDTO<Object>> respostasDTO){
        respostasDTO.add(new RespostaDTO<Object>("Erro", mensagem, objetoJson));
    }
}
