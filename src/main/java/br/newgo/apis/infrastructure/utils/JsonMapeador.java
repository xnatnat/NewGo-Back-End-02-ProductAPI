package br.newgo.apis.infrastructure.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    /**
     * Cria um fluxo (Stream) de objetos JSON a partir de uma string JSON que pode conter um array de objetos JSON ou um único objeto JSON.
     *
     * @param json A string JSON a ser convertida em um fluxo de objetos JSON.
     * @return Um fluxo de objetos JSON representando os elementos contidos na string JSON.
     * @throws IllegalArgumentException Se o JSON não for nem um objeto nem um array.
     */
    public static Stream<JsonObject> mapearParaObjetosJson(String json) {
        JsonElement jsonProdutos = mapearParaElementoJson(json);

        if (jsonProdutos.isJsonArray()) {
            return StreamSupport.stream(jsonProdutos.getAsJsonArray().spliterator(), false)
                    .map(JsonMapeador::mapearParaObjetoJson);
        }

        if (jsonProdutos.isJsonObject()) {
            return Stream.of(mapearParaObjetoJson(jsonProdutos));
        }

        throw new IllegalArgumentException("O JSON não é nem um objeto nem um array.");
    }
}
