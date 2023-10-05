package br.newgo.apis.application.utils;

import java.util.Arrays;
import java.util.List;

public enum ProdutoAtributos {
    ATRIBUTOS_OBRIGATORIOS_SALVAR(Arrays.asList("nome", "descricao", "ean13", "preco", "quantidade", "estoqueMin")),
    ATRIBUTOS_OBRIGATORIOS_ATUALIZAR_PRECO_LOTE(Arrays.asList("hash", "operacao", "valor")),
    ATRIBUTOS_OBRIGATORIOS_ATUALIZAR_ESTOQUE_LOTE(Arrays.asList("hash", "valor")),
    ATRIBUTOS_ATUALIZAVEIS(Arrays.asList("descricao", "preco", "quantidade", "estoqueMin")),
    ATRIBUTO_STATUS(Arrays.asList("lativo"));

    private final List<String> atributos;

    ProdutoAtributos(List<String> atributos){
        this.atributos = atributos;
    }

    public List<String> getAtributos(){
        return atributos;
    }
}
