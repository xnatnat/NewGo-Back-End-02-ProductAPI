package br.newgo.apis.infrastructure.utils;

import java.util.Arrays;
import java.util.List;

public enum ProdutoAtributos {
    ATRIBUTOS_OBRIGATORIOS(Arrays.asList("nome", "descricao", "ean13", "preco", "quantidade", "estoqueMin")),
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
