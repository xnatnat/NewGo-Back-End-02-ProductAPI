package br.newgo.apis.presentation.controller;

import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controlador servlet para atualização de status de produtos.
 *
 * Esta classe implementa um servlet que lida com requisições HTTP para atualizar o status de produtos.
 * Ela recebe solicitações PUT para alterar o status de um produto e responde com um status HTTP "No Content"
 * após a atualização bem-sucedida.
 */
public class ProdutoStatusController extends HttpServlet {

    private ProdutoService produtoService;

    /**
     * Inicializa o servlet e configura o serviço de produto.
     * <p>
     * Este método é chamado quando o servlet é inicializado e cria uma instância do serviço de produto
     * para processar as solicitações.
     *
     * @throws ServletException Se ocorrer um erro durante a inicialização do servlet.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        produtoService = new ProdutoService(new ProdutoDAO());
    }

    /**
     * Lida com solicitações HTTP PUT para atualizar o status de um produto.
     *
     * Este método recebe uma solicitação PUT para atualizar o status de um produto com base nos parâmetros
     * fornecidos na solicitação. Após a atualização bem-sucedida, ele define o status HTTP como "No Content".
     *
     * @param req  O objeto HttpServletRequest que contém a solicitação HTTP.
     * @param resp O objeto HttpServletResponse usado para enviar a resposta HTTP.
     * @throws IOException      Se ocorrer um erro de E/S durante o processamento da resposta.
     * @throws ServletException Se ocorrer um erro durante o processamento da solicitação.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        produtoService.atualizarStatusLativo(RequestUtils.extrairHash(req), RequestUtils.lerCorpoDaRequisicao(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
