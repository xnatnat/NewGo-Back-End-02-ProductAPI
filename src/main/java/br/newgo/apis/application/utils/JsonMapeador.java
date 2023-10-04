package br.newgo.apis.application.utils;

import br.newgo.apis.application.dtos.ProdutoDTO;
import br.newgo.apis.application.dtos.RespostaDTO;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Esta classe fornece métodos para mapear strings JSON em elementos JSON e objetos JSON,
 * bem como para criar streams de objetos JSON a partir de strings JSON.
 */
public class JsonMapeador {

    /**
     * Converte uma string JSON em um elemento JSON.
     *
     * @param json A string JSON a ser convertida.
     * @return Um elemento JSON representando a string JSON fornecida.
     */
    public static JsonElement mapearParaElementoJson(String json) {
        return new JsonParser().parse(json);
    }

    /**
     * Converte um elemento JSON em um objeto JSON.
     *
     * @param json O elemento JSON a ser convertido em objeto JSON.
     * @return Um objeto JSON representando o elemento JSON fornecido.
     */
    public static JsonObject mapearParaObjetoJson(JsonElement json) {
        return json.getAsJsonObject();
    }

    /**
     * Converte uma string JSON em um objeto JSON.
     *
     * @param json A string JSON a ser convertida em objeto JSON.
     * @return Um objeto JSON representando a string JSON fornecida.
     */
    public static JsonObject mapearParaObjetoJson(String json) {
        return mapearParaElementoJson(json).getAsJsonObject();
    }

    public static List<JsonObject> mapearParaListaDeObjetosJson(String json) {
        JsonElement jsonProdutos = mapearParaElementoJson(json);

        if (jsonProdutos.isJsonArray())
            return jsonProdutos.getAsJsonArray()
                    .asList()
                    .stream()
                    .map(JsonElement::getAsJsonObject)
                    .collect(Collectors.toList());

        throw new IllegalArgumentException("Não foi possível obter JSON de produtos em lote");
    }

    public static String mapearListaParaJson(List<RespostaDTO<Object>> respostas){
        return new Gson().toJson(respostas);
    }

    public static String mapearParaJson(ProdutoDTO produtoDTO){
        return new Gson().toJson(produtoDTO);
    }

    public static String mapearParaJson(List<ProdutoDTO> produtoDTO){
        return new Gson().toJson(produtoDTO);
    }
}
