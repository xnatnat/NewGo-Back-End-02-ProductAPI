package br.newgo.apis.infrastructure.utils;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.presentation.dtos.ProdutoDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Esta classe é responsável por converter objetos da classe Produto para objetos da classe ProdutoDTO
 * e vice-versa, além de fornecer funcionalidades para conversão de listas de objetos ProdutoDTO em JSON.
 */
public class ProdutoMapeador {
    /**
     * Converte um objeto da classe Produto para um objeto da classe ProdutoDTO.
     *
     * @param produto O objeto da classe Produto a ser convertido.
     * @return Um objeto da classe ProdutoDTO contendo os mesmos dados do objeto Produto original.
     */
    public static ProdutoDTO mapearParaDTO(Produto produto){
        return new ProdutoDTO(produto);
    }

    /**
     * Converte um objeto JsonObject em um objeto da classe Produto.
     *
     * @param jsonObject O objeto JsonObject a ser convertido.
     * @return Um objeto da classe Produto com base nos dados do JsonObject.
     */
    public static Produto mapearParaProduto(JsonObject jsonObject){
        return new Gson().fromJson(jsonObject, Produto.class);
    }

    /**
     * Converte uma lista de objetos ProdutoDTO em uma representação JSON.
     *
     * @param produtoDTOS A lista de objetos ProdutoDTO a ser convertida em JSON.
     * @return Uma string JSON representando a lista de objetos ProdutoDTO fornecida.
     */
    public static String mapearParaJson(List<ProdutoDTO> produtoDTOS){
        return new Gson().toJson(produtoDTOS);
    }
}
