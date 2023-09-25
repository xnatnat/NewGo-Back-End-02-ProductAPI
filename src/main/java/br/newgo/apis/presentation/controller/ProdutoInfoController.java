package br.newgo.apis.presentation.controller;

import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.RequestUtils;
import br.newgo.apis.infrastructure.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controlador servlet para informações de produtos.
 *
 * Esta classe implementa um servlet que lida com requisições HTTP para obter informações, atualizar e deletar produtos.
 * Ela recebe solicitações GET, PUT e DELETE para realizar operações em produtos e responde com os resultados
 * correspondentes.
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
     * <p>
     * Este método recebe uma solicitação GET para obter informações detalhadas de um produto com base em seu
     * identificador único (hash). Ele responde com os dados do produto em formato JSON.
     *
     * @param req  O objeto HttpServletRequest que contém a solicitação HTTP.
     * @param resp O objeto HttpServletResponse usado para enviar a resposta HTTP.
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ResponseUtils.escreverJson(resp, produtoService.obterDtoPorHash(RequestUtils.extrairHash(req)).toJson());
    }
}