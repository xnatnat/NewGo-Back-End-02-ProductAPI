package br.newgo.apis.domain.model.services;

import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

/**
 * Classe responsável por validar dados relacionados a Entidade Produto.
 */
public class ProdutoValidacao {

    private final ProdutoDAO produtoDAO;
    private static final String[] CAMPOS_OBRIGATORIOS = {
            "nome", "descricao", "ean13", "preco", "quantidade", "estoqueMin"
    };
    public ProdutoValidacao(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    /**
     * Valida um produto inserido, verificando campos obrigatórios, campos não permitidos e valores.
     *
     * @param produtoInserido O JSON representando o produto a ser validado.
     * @throws IllegalArgumentException Se o produto inserido for inválido.
     */
    public void validarProdutoInserido(String produtoInserido) {
        JsonObject produtoJson = parseJson(produtoInserido);

        verificarCamposObrigatorios(produtoJson);
        verificarCamposNaoPermitidos(produtoJson);
        validarValores(produtoJson);
    }

    /**
     * Valida os valores (nome, EAN-13, preço, quantidade e estoque mínimo) de um produto a partir de um objeto JSON.
     *
     * @param json O objeto JSON contendo os valores a serem validados.
     * @throws IllegalArgumentException Se algum dos valores (nome nulo ou vazio, nome ou EAN-13 duplicados,
     *                                  preço, quantidade ou estoque mínimo negativos) for inválido.
     */
    private void validarValores(JsonObject json) {
        String nome = json.get("nome").getAsString();
        String ean13 = json.get("ean13").getAsString();
        double preco = json.get("preco").getAsDouble();
        double quantidade = json.get("quantidade").getAsDouble();
        double estoqueMin = json.get("estoqueMin").getAsDouble();

        validarNomeNuloOuVazio(nome);
        validarNomeOuEan13Duplicado(nome, ean13);
        validarValoresNegativos(preco, quantidade, estoqueMin);
    }

    /**
     * Valida se o nome é nulo ou vazio.
     *
     * @param nome O nome a ser validado.
     * @throws IllegalArgumentException Se o nome for nulo ou vazio.
     */
    private void validarNomeNuloOuVazio(String nome)  {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Não é possível cadastrar um produto sem nome.");
        }
    }

    /**
     * Valida se já existe um produto com o mesmo nome ou código EAN-13 no banco de dados.
     *
     * @param nome  O nome do produto a ser validado.
     * @param ean13 O código EAN-13 do produto a ser validado.
     * @throws IllegalArgumentException Se um produto com o mesmo nome ou EAN-13 já existir no banco de dados.
     */
    private void validarNomeOuEan13Duplicado(String nome, String ean13)  {
        if (produtoDAO.existeProdutoComNomeOuEan13(nome, ean13)) {
            throw new IllegalArgumentException("Já existe um produto com o mesmo nome ou EAN-13.");
        }
    }

    /**
     * Valida se os valores (preço, quantidade e estoque mínimo) são negativos.
     *
     * @param preco       O preço a ser validado.
     * @param quantidade  A quantidade a ser validada.
     * @param estoqueMin  O estoque mínimo a ser validado.
     * @throws IllegalArgumentException Se algum dos valores (preço, quantidade ou estoque mínimo) for negativo.
     */
    private void validarValoresNegativos(double preco, double quantidade, double estoqueMin)  {
        if (preco < 0 || quantidade < 0 || estoqueMin < 0) {
            throw new IllegalArgumentException("Não é permitido cadastrar produtos com preço, quantidade ou estoque mínimo negativos.");
        }
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