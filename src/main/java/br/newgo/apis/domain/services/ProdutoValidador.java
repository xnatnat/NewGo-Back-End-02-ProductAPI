package br.newgo.apis.domain.services;

import br.newgo.apis.domain.model.Produto;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.UUID;

/**
 * Classe responsável por validar dados relacionados a Entidade Produto.
 */
public class ProdutoValidacao {

    private final ProdutoDAO produtoDAO;
    public ProdutoValidacao(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void validarJsonProduto(JsonObject jsonProduto) {
        if(jsonProduto.has("nome")) {
            validarStringNulaOuVazia(jsonProduto, "nome");
            validarStringNulaOuVazia(jsonProduto, "ean13");
            validarNomeOuEan13Duplicado(jsonProduto.get("nome").getAsString(), jsonProduto.get("ean13").getAsString());
        }
        if(jsonProduto.has("preco"))
            validarDoubleNuloOuNegativo(jsonProduto, "preco");
        if(jsonProduto.has("quantidade"))
            validarDoubleNuloOuNegativo(jsonProduto, "quantidade");
        if(jsonProduto.has("estoqueMin"))
            validarDoubleNuloOuNegativo(jsonProduto, "estoqueMin");
    }

    public void validarStringNulaOuVazia(JsonObject jsonProduto, String chave){
        if (jsonProduto.get(chave).isJsonNull() || jsonProduto.get(chave).getAsString().isEmpty())
            throw new IllegalArgumentException("O atributo '"+ chave + "' não pode ser nulo ou vazio.");
    }

    private void validarDoubleNuloOuNegativo(JsonObject jsonObject, String chave) {
        if (jsonObject.get(chave).isJsonNull())
            jsonObject.add(chave, new JsonPrimitive(0.0));
        else if (jsonObject.get(chave).getAsDouble() < 0)
            throw new IllegalArgumentException("O atributo '" + chave + "' não pode ser negativo.");
    }

    private void validarNomeOuEan13Duplicado(String nome, String ean13)  {
        if (produtoDAO.existeProdutoComNomeOuEan13(nome, ean13)) {
            throw new IllegalArgumentException("Já existe um produto com o mesmo nome ou EAN-13.");
        }
    }

    public void verificarLativo(JsonObject jsonObject){
        String lativo = jsonObject.get("lativo").getAsString();
        if (!(lativo.equalsIgnoreCase("true") || lativo.equalsIgnoreCase("false")))
            throw new IllegalArgumentException("O atributo lativo deve ter valor TRUE ou FALSE");
    }

    /**
     * Valida o formato de um hash.
     *
     * @param hash O hash a ser validado.
     * @return O UUID do hash validado.
     * @throws IllegalArgumentException Se o formato do hash for inválido.
     */
    public UUID validarHash(String hash) {
        try {
            return UUID.fromString(hash);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Formato de hash inválido.", e);
        }
    }

    public void validarSeProdutoEstaAtivo(boolean lativo){
        if(!lativo)
            throw new IllegalStateException("Produto não está ativo.");
    }
}