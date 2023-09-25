package br.newgo.apis.domain.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Classe responsável por validar JSONs de produtos.
 */
public class JsonValidacao {
    /**
     * Campos obrigatórios para um JSON de produto.
     */
    private static final String[] CAMPOS_OBRIGATORIOS = {
            "nome", "descricao", "ean13", "preco", "quantidade", "estoqueMin"
    };

    /**
     * Valida um JSON de produto, verificando a presença de campos obrigatórios e a ausência de campos não permitidos.
     *
     * @param json O JSON a ser validado.
     * @throws IllegalArgumentException Se o JSON for inválido.
     */
    public void validarJson(String json) {
        JsonObject produtoJson = parseJson(json);
        verificarCamposObrigatorios(produtoJson);
        verificarCamposNaoPermitidos(produtoJson);
    }

    /**
     * Analisa o JSON fornecido e o converte em um objeto JsonObject.
     *
     * @param json O JSON a ser analisado.
     * @return Um objeto JsonObject representando o JSON analisado.
     * @throws IllegalArgumentException Se o formato do JSON for inválido.
     */
    private JsonObject parseJson(String json) {
        try {
            return JsonParser.parseString(json).getAsJsonObject();
        } catch (com.google.gson.JsonSyntaxException e) {
            throw new IllegalArgumentException("Formato JSON inválido", e);
        }
    }

    /**
     * Verifica a presença dos campos obrigatórios em um objeto JsonObject.
     *
     * @param produtoJson O JsonObject a ser verificado.
     * @throws IllegalArgumentException Se campos obrigatórios estiverem ausentes.
     */
    private void verificarCamposObrigatorios(JsonObject produtoJson) {
        for (String campo : CAMPOS_OBRIGATORIOS) {
            if (!produtoJson.has(campo)) {
                throw new IllegalArgumentException("JSON inválido. O campo obrigatório '" + campo + "' está ausente.");
            }
        }
    }

    /**
     * Verifica a presença de campos não permitidos em um objeto JsonObject.
     *
     * @param produtoJson O JsonObject a ser verificado.
     * @throws IllegalArgumentException Se campos não permitidos estiverem presentes.
     */
    private void verificarCamposNaoPermitidos(JsonObject produtoJson) {
        for (String campo : produtoJson.keySet()) {
            if (!containsIgnoreCase(CAMPOS_OBRIGATORIOS, campo)) {
                throw new IllegalArgumentException("JSON inválido. O campo '" + campo + "' não é permitido.");
            }
        }
    }

    /**
     * Verifica se um array de strings contém um determinado valor, ignorando diferenças de caixa.
     *
     * @param array  O array de strings a ser verificado.
     * @param target O valor a ser procurado no array.
     * @return true se o valor estiver presente no array (ignorando caixa), caso contrário, false.
     */
    private boolean containsIgnoreCase(String[] array, String target) {
        for (String s : array) {
            if (s.equalsIgnoreCase(target)) {
                return true;
            }
        }
        return false;
    }
}