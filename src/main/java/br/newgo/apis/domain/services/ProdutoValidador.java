package br.newgo.apis.domain.services;

import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.UUID;

/**
 * Classe responsável por validar objetos JSON de produtos.
 */
public class ProdutoValidador {

    private final ProdutoDAO produtoDAO;
    public ProdutoValidador(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    /**
     * Valida um objeto JSON de produto, verificando nome, EAN-13, preço, quantidade e estoque mínimo.
     *
     * @param objetoJson O objeto JSON a ser validado.
     * @throws IllegalArgumentException Se a validação falhar.
     */
    public void validarJsonProduto(JsonObject objetoJson) {
        if(objetoJson.has("nome")) {
            validarStringNulaOuVazia(objetoJson, "nome");
            validarStringNulaOuVazia(objetoJson, "ean13");
            validarNomeOuEan13Duplicado(objetoJson.get("nome").getAsString(), objetoJson.get("ean13").getAsString());
        }
        if(objetoJson.has("preco"))
            validarDoubleNuloOuNegativo(objetoJson, "preco");
        if(objetoJson.has("quantidade"))
            validarDoubleNuloOuNegativo(objetoJson, "quantidade");
        if(objetoJson.has("estoqueMin"))
            validarDoubleNuloOuNegativo(objetoJson, "estoqueMin");
    }

    /**
     * Valida uma string para garantir que não seja nula ou vazia.
     *
     * @param objetoJson O objeto JSON que contém a string a ser validada.
     * @param atributo   O nome do atributo que contém a string.
     * @throws IllegalArgumentException Se a string for nula ou vazia.
     */
    private void validarStringNulaOuVazia(JsonObject objetoJson, String atributo){
        JsonElement elemento = objetoJson.get(atributo);
        if (elemento.isJsonNull() || elemento.getAsString().isEmpty())
            throw new IllegalArgumentException("O atributo '"+ atributo + "' não pode ser nulo ou vazio.");
    }

    /**
     * Valida se já existe um produto com o mesmo nome ou EAN-13 no banco de dados.
     *
     * @param nome  O nome do produto.
     * @param ean13 O código EAN-13 do produto.
     * @throws IllegalArgumentException Se já existir um produto com o mesmo nome ou EAN-13.
     */
    private void validarNomeOuEan13Duplicado(String nome, String ean13)  {
        if (produtoDAO.existeProdutoComNomeOuEan13(nome, ean13)) {
            throw new IllegalArgumentException("Já existe um produto com o mesmo nome ou EAN-13.");
        }
    }

    /**
     * Valida um valor double para garantir que não seja nulo ou negativo.
     *
     * @param objetoJson O objeto JSON que contém o valor double a ser validado.
     * @param atributo   O nome do atributo que contém o valor double.
     * @throws IllegalArgumentException Se o valor for nulo ou negativo.
     */
    private void validarDoubleNuloOuNegativo(JsonObject objetoJson, String atributo) {
        JsonElement elemento = objetoJson.get(atributo);
        if (elemento.isJsonNull())
            objetoJson.add(atributo, new JsonPrimitive(0.0));
        else if (elemento.getAsDouble() < 0)
            throw new IllegalArgumentException("O atributo '" + atributo + "' não pode ser negativo.");
    }

    /**
     * Verifica se o atributo 'lativo' tem valor 'true' ou 'false'.
     *
     * @param objetoJson O objeto JSON a ser verificado.
     * @throws IllegalArgumentException Se o valor do atributo não for 'true' ou 'false'.
     */
    public void verificarLativo(JsonObject objetoJson){
        String lativo = objetoJson.get("lativo").getAsString();
        if (!(lativo.equalsIgnoreCase("true") || lativo.equalsIgnoreCase("false")))
            throw new IllegalArgumentException("O atributo lativo deve ter valor TRUE ou FALSE");
    }

    /**
     * Valida o formato de um hash e retorna o UUID correspondente.
     *
     * @param hash O hash a ser validado.
     * @return O UUID do hash validado.
     * @throws IllegalArgumentException Se o formato do hash for inválido.
     */
    public UUID validarERetornarHash(String hash) {
        try {
            return UUID.fromString(hash);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de hash inválido.", e);
        }
    }

    /**
     * Valida se um produto está ativo.
     *
     * @param lativo O estado de ativação do produto.
     * @throws IllegalStateException Se o produto não estiver ativo.
     */
    public void validarSeProdutoEstaAtivo(boolean lativo){
        if(!lativo)
            throw new IllegalStateException("Produto não está ativo.");
    }
}