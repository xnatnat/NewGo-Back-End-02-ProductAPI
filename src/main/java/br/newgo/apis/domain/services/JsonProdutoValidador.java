package br.newgo.apis.domain.services;

import br.newgo.apis.infrastructure.utils.ProdutoAtributos;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ValidadorJsonProduto {
    public Stream<JsonObject> extrairObjetosJson(String json) {

        JsonElement jsonProdutos = parseParaJsonElement(json);

        if (jsonProdutos.isJsonArray()) {
            return StreamSupport.stream(jsonProdutos.getAsJsonArray().spliterator(), false)
                    .map(JsonElement::getAsJsonObject);
        }

        if (jsonProdutos.isJsonObject()) {
            return Stream.of(obterJsonObject(jsonProdutos));
        }
        throw new IllegalArgumentException("O JSON não é nem um objeto nem um array.");
    }

    private JsonElement parseParaJsonElement(String json){
        return new JsonParser().parse(json);
    }

    private JsonObject obterJsonObject(JsonElement json){
        return json.getAsJsonObject();
    }

    public void validarObjetoJson(JsonObject objetoJson, ProdutoAtributos atributos) {
        if(atributos != ProdutoAtributos.ATRIBUTOS_ATUALIZAVEIS)
            validarAtributosObrigatorios(objetoJson, atributos);
        validarAtributosPermitidos(objetoJson, atributos);
        validarValoresVazios(objetoJson);
    }

    private void validarAtributosObrigatorios(JsonObject objetoJson, ProdutoAtributos atributos) {
        atributos.getAtributos().forEach(atributo -> validarAtributoObrigatorio(objetoJson, atributo));
    }

    private void validarAtributoObrigatorio(JsonObject objetoJson, String atributo){
        if(!objetoJson.has(atributo))
            throw new IllegalArgumentException("JSON inválido. O atributo obrigatório '" + atributo + "' está ausente.");
    }

    private void validarAtributosPermitidos(JsonObject objetoJson, ProdutoAtributos atributos) {
        List<String> listAtributos = atributos.getAtributos();
        objetoJson.keySet().forEach(atributo -> validarAtributoPermitido(atributo, listAtributos));
    }

    private void validarAtributoPermitido(String atributo, List<String> atributosPermitidos){
        if(!atributosPermitidos.contains(atributo))
            throw new IllegalArgumentException("JSON inválido. O atributo '" + atributo + "' não é permitido.");
    }

    private void validarValoresVazios(JsonObject objetoJson) {
        objetoJson.keySet().forEach(atributo -> verificarValorVazio(objetoJson, atributo));
    }

    private void validarValorVazio(JsonObject objetoJson, String atributo){
        JsonElement elemento = objetoJson.get(atributo);
        if (elemento != null && elemento.isJsonPrimitive() && elemento.getAsString().isEmpty()) {
            throw new IllegalArgumentException("JSON inválido. O valor do atributo '" + atributo + "' está vazio.");
        }
    }
}

