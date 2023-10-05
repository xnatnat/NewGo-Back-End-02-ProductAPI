package br.newgo.apis.application.servlets;

import br.newgo.apis.application.controller.ProdutoController;
import br.newgo.apis.application.dtos.RespostaDTO;
import br.newgo.apis.application.utils.RequestUtils;
import br.newgo.apis.application.utils.ResponseUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static br.newgo.apis.application.utils.JsonMapeador.mapearListaParaJson;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            String jsonResposta = "";

            if (req.getParameter("estoque-baixo") != null)
                jsonResposta = mapearParaJson(produtoController.obterTodosComEstoqueBaixo());

            else if (pathInfo == null || pathInfo.equals("/"))
                jsonResposta = mapearParaJson(produtoController.obterTodos());

            else if (pathInfo.equalsIgnoreCase("/ativos") || pathInfo.equalsIgnoreCase("/ativos/"))
                jsonResposta = mapearParaJson(produtoController.obterTodosPorStatus("true"));

            else if (pathInfo.equalsIgnoreCase("/inativos") || pathInfo.equalsIgnoreCase("/inativos/"))
                jsonResposta = mapearParaJson(produtoController.obterTodosPorStatus("false"));

            else {
                String[] pathParts = pathInfo.split("/");

                if (pathParts.length == 3 && pathParts[2].equalsIgnoreCase("ativo"))
                    jsonResposta = mapearParaJson(produtoController.obterDtoAtivoPorHash(pathParts[1]));

                else if (pathParts.length == 2)
                    jsonResposta = mapearParaJson(produtoController.obterDtoPorHash(pathParts[1]));
            }
            ResponseUtils.escreverJson(resp, jsonResposta);
        } catch (IllegalArgumentException | IllegalStateException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseUtils.escreverJson(resp, e.getMessage());
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String pathInfo = req.getPathInfo();
            String jsonRequisicao = RequestUtils.lerCorpoDaRequisicao(req);

            List<RespostaDTO<Object>> respostaDTOS = new ArrayList<>();

            if (pathInfo == null || pathInfo.equals("/"))
                respostaDTOS.add(produtoController.criar(jsonRequisicao));
            else if (pathInfo.equalsIgnoreCase("/lote") || pathInfo.equalsIgnoreCase("/lote/"))
                respostaDTOS = produtoController.criarLote(jsonRequisicao);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setHeader("Location",
                    RequestUtils.gerarLocationHeader(req, respostaDTOS));

            ResponseUtils.escreverJson(resp,
                    mapearListaParaJson(respostaDTOS));
        } catch (IllegalArgumentException | IllegalStateException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseUtils.escreverJson(resp, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String[] pathParts = req.getPathInfo().split("/");
            String jsonRequisicao = RequestUtils.lerCorpoDaRequisicao(req);

            List<RespostaDTO<Object>> respostaDTOS = new ArrayList<>();

            if (pathParts.length == 2 && pathParts[1].equalsIgnoreCase("atualizar-preco-lote"))
                respostaDTOS = produtoController.atualizarPrecoLote(jsonRequisicao);

            else if (pathParts.length == 2 && pathParts[1].equalsIgnoreCase("atualizar-estoque-lote"))
                respostaDTOS = produtoController.atualizarEstoqueLote(jsonRequisicao);

            else if (pathParts.length == 2)
                respostaDTOS.add(produtoController.atualizar(pathParts[1], jsonRequisicao));

            else if (pathParts.length == 3 && pathParts[2].equalsIgnoreCase("status"))
                respostaDTOS.add(produtoController.atualizarStatus(pathParts[1], jsonRequisicao));

            ResponseUtils.escreverJson(resp,
                    mapearListaParaJson(respostaDTOS));
        } catch (IllegalArgumentException | IllegalStateException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseUtils.escreverJson(resp, e.getMessage());
        }
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
        try {
            produtoController.deletar(RequestUtils.extrairHash(req));
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (IllegalArgumentException | IllegalStateException e){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ResponseUtils.escreverJson(resp, e.getMessage());
        }
    }
}