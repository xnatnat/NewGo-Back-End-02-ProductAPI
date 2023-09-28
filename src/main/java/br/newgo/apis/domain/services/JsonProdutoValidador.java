package br.newgo.apis.domain.services;

import br.newgo.apis.infrastructure.utils.ProdutoAtributos;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * Classe responsável por validar objetos JSON de produtos.
 */
public class JsonProdutoValidador {

    /**
     * Valida um objeto JSON de produto com base nos atributos especificados.
     *
     * @param objetoJson O objeto JSON a ser validado.
     * @param atributos Os atributos que devem ser considerados na validação.
     * @throws IllegalArgumentException Se o JSON não atender aos critérios de validação.
     */
    public void validarObjetoJson(JsonObject objetoJson, ProdutoAtributos atributos) {
        if(atributos != ProdutoAtributos.ATRIBUTOS_ATUALIZAVEIS)
            validarAtributosObrigatorios(objetoJson, atributos);
        validarAtributosPermitidos(objetoJson, atributos);
        validarValoresVazios(objetoJson);
    }

    /**
     * Valida os atributos obrigatórios em um objeto JSON de produto.
     *
     * @param objetoJson O objeto JSON a ser validado.
     * @param atributos Os atributos obrigatórios que devem estar presentes.
     * @throws IllegalArgumentException Se algum atributo obrigatório estiver ausente.
     */
    private void validarAtributosObrigatorios(JsonObject objetoJson, ProdutoAtributos atributos) {
        atributos.getAtributos().forEach(atributo -> {
            if(!objetoJson.has(atributo))
                throw new IllegalArgumentException("JSON inválido. O atributo obrigatório '" + atributo + "' está ausente.");
        });
    }

    /**
     * Valida os atributos permitidos em um objeto JSON de produto.
     *
     * @param objetoJson O objeto JSON a ser validado.
     * @param atributos Os atributos permitidos que não devem ser desconhecidos.
     * @throws IllegalArgumentException Se algum atributo desconhecido for encontrado.
     */
    private void validarAtributosPermitidos(JsonObject objetoJson, ProdutoAtributos atributos) {
        List<String> listAtributos = atributos.getAtributos();
        objetoJson.keySet().forEach(atributo -> {
            if(!listAtributos.contains(atributo))
                throw new IllegalArgumentException("JSON inválido. O atributo '" + atributo + "' não é permitido.");
        });
    }

    /**
     * Valida os valores vazios em um objeto JSON de produto.
     *
     * @param objetoJson O objeto JSON a ser validado.
     * @throws IllegalArgumentException Se algum valor de atributo estiver vazio.
     */
    private void validarValoresVazios(JsonObject objetoJson) {
        objetoJson.keySet().forEach(atributo -> {
            JsonElement elemento = objetoJson.get(atributo);
            if (elemento.isJsonNull() && elemento.isJsonPrimitive() && elemento.getAsString().isEmpty())
                throw new IllegalArgumentException("JSON inválido. O valor do atributo '" + atributo + "' está vazio.");
        });
    }
}