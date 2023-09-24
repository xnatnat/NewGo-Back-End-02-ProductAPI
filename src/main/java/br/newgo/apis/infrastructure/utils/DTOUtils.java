package br.newgo.apis.infrastructure.utils;

import br.newgo.apis.presentation.dtos.ProdutoDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe utilitária para operações relacionadas a objetos DTO (Data Transfer Object).
 */
public class DTOUtils {

    /**
     * Extrai um objeto ProdutoDTO a partir do corpo de uma requisição HTTP.
     *
     * @param req O HttpServletRequest contendo o corpo da requisição.
     * @return Um objeto ProdutoDTO extraído do corpo da requisição.
     */
    public static ProdutoDTO extrairProdutoDTODaRequisicao(HttpServletRequest req) {
        String requestBody = RequestUtils.lerCorpoDaRequisicao(req);
        return ProdutoDTO.fromJson(requestBody);
    }
}

