package br.newgo.apis.presentation.controller;

import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.RequestUtils;
import br.newgo.apis.infrastructure.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador servlet para informações de produtos.
 *
 * Esta classe implementa um servlet que lida com requisições HTTP para obter informações, atualizar e deletar produtos.
 * Ela recebe solicitações GET, PUT e DELETE para realizar operações em produtos e responde com os resultados correspondentes.
 */
public class ProdutoInfoController extends HttpServlet {
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
     * Lida com solicitações HTTP GET para obter informações de um produto.
     *
     * Este método recebe uma solicitação GET para obter informações detalhadas de um produto com base em seu
     * identificador único (hash). Ele responde com os dados do produto em formato JSON.
     *
     * @param req  O objeto HttpServletRequest que contém a solicitação HTTP.
     * @param resp O objeto HttpServletResponse usado para enviar a resposta HTTP.
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        String lativoParam = req.getParameter("lativo");

        if(lativoParam == null)
            ResponseUtils.escreverJson(resp, produtoService.obterDtoPorHash(RequestUtils.extrairHash(req)).toJson());

        else if(lativoParam.equalsIgnoreCase("true"))
            ResponseUtils.escreverJson(resp, produtoService.obterDtoAtivoPorHash(RequestUtils.extrairHash(req)).toJson());

        else
            throw new IllegalArgumentException("Valor do parametro inválido.");
    }

    /**
     * Lida com solicitações HTTP PUT para atualizar um produto.
     *
     * Este método recebe uma solicitação PUT para atualizar um produto com base em seu identificador único (hash).
     * Ele responde com os dados atualizados do produto em formato JSON.
     *
     * @param req  O objeto HttpServletRequest que contém a solicitação HTTP.
     * @param resp O objeto HttpServletResponse usado para enviar a resposta HTTP.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        ResponseUtils.escreverJson(resp,
                produtoService.atualizar(RequestUtils.extrairHash(req),
                                RequestUtils.lerCorpoDaRequisicao(req)).toJson());

    }

    /**
     * Lida com solicitações HTTP DELETE para deletar um produto.
     *
     * Este método recebe uma solicitação DELETE para excluir um produto com base em seu identificador único (hash).
     * Ele responde com um status HTTP "No Content" após a exclusão bem-sucedida do produto.
     *
     * @param req  O objeto HttpServletRequest que contém a solicitação HTTP.
     * @param resp O objeto HttpServletResponse usado para enviar a resposta HTTP.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        produtoService.deletar(RequestUtils.extrairHash(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}