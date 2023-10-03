package br.newgo.apis.application.servlets;

import br.newgo.apis.application.controller.ProdutoController;
import br.newgo.apis.application.dtos.ProdutoDTO;
import br.newgo.apis.application.utils.ProdutoMapeador;
import br.newgo.apis.application.utils.RequestUtils;
import br.newgo.apis.application.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static br.newgo.apis.application.utils.JsonMapeador.mapearParaJson;

public class ProdutoServlet extends HttpServlet {
    private ProdutoController produtoController;

    /**
     * Inicializa o servlet e cria uma instância do serviço de produtos.
     * Lança uma exceção RuntimeException se ocorrer um erro durante a inicialização.
     */
    @Override
    public void init()  {
        try {
            super.init();
        } catch (ServletException e) {
            throw new RuntimeException("Erro ao inicializar o servlet: " + e.getMessage(), e);
        }
        produtoController = new ProdutoController();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        String pathInfo = req.getPathInfo();
        String jsonResposta = "";

        if(req.getParameter("estoque-baixo") != null)
            jsonResposta = mapearParaJson(produtoController.obterTodosComEstoqueBaixo());

        else if(pathInfo == null || pathInfo.equals("/"))
            jsonResposta = mapearParaJson(produtoController.obterTodos());

        else if(pathInfo.equalsIgnoreCase("/ativos"))
            jsonResposta = mapearParaJson(produtoController.obterTodosPorStatus("true"));

        else if(pathInfo.equalsIgnoreCase("/inativos"))
            jsonResposta = mapearParaJson(produtoController.obterTodosPorStatus("false"));

        else {
            String[] pathParts = pathInfo.split("/");

            if(pathParts.length == 3 && pathParts[2].equalsIgnoreCase("ativo"))
                jsonResposta = mapearParaJson(produtoController.obterDtoAtivoPorHash(pathParts[1]));

            else if (pathParts.length == 2)
                jsonResposta = mapearParaJson(produtoController.obterDtoPorHash(pathParts[1]));
        }
        ResponseUtils.escreverJson(resp, jsonResposta);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        String jsonRequisicao = RequestUtils.lerCorpoDaRequisicao(req);
        List<ProdutoDTO> produtos = new ArrayList<>();

        if(pathInfo == null || pathInfo.equals("/"))
            produtos.add(produtoController.criar(jsonRequisicao));

        else if(pathInfo.equalsIgnoreCase("/lote"))
            produtos = produtoController.criarLote(jsonRequisicao);

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location",
                RequestUtils.gerarLocationHeader(req, produtos));

        ResponseUtils.escreverJson(resp,
                ProdutoMapeador.mapearParaJson(produtos));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String[] pathParts = req.getPathInfo().split("/");
        String jsonRequisicao = RequestUtils.lerCorpoDaRequisicao(req);
        ProdutoDTO produtoDTO = null;

        if(pathParts.length == 2)
            produtoDTO = produtoController.atualizar(pathParts[1], jsonRequisicao);

        else if (pathParts.length == 3 && pathParts[2].equalsIgnoreCase("status"))
            produtoDTO = produtoController.atualizarStatus(pathParts[1], jsonRequisicao);

        ResponseUtils.escreverJson(resp,
                mapearParaJson(produtoDTO));

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
        produtoController.deletar(RequestUtils.extrairHash(req));
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
}