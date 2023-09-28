package br.newgo.apis.presentation.controller;

import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.ProdutoMapeador;
import br.newgo.apis.infrastructure.utils.RequestUtils;
import br.newgo.apis.infrastructure.utils.ResponseUtils;
import br.newgo.apis.presentation.dtos.ProdutoDTO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Esta classe representa um controlador para operações relacionadas a produtos em um servlet.
 * Ele lida com as solicitações HTTP GET e POST para gerenciar produtos.
 */
public class ProdutoController extends HttpServlet {
    private ProdutoService produtoService;

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
        produtoService = new ProdutoService(new ProdutoDAO());
    }

    /**
     * Manipula solicitações HTTP POST para criar produtos.
     *
     * @param req  O objeto HttpServletRequest que representa a solicitação.
     * @param resp O objeto HttpServletResponse que representa a resposta.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){

        List<ProdutoDTO> produtos = produtoService.criar(RequestUtils.lerCorpoDaRequisicao(req));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location",
                RequestUtils.gerarLocationHeader(req, produtos));

        ResponseUtils.escreverJson(resp, ProdutoMapeador.mapearParaJson(produtos));
    }

    /**
     * Manipula solicitações HTTP GET para obter todos os produtos ou produtos por status.
     *
     * @param req  O objeto HttpServletRequest que representa a solicitação.
     * @param resp O objeto HttpServletResponse que representa a resposta.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

        String lativoParam = req.getParameter("lativo");

        if(lativoParam == null)
            ResponseUtils.escreverJson(resp, new Gson().toJson(produtoService.obterTodos()));

        else if(lativoParam.equalsIgnoreCase("true") || lativoParam.equalsIgnoreCase("false"))
            ResponseUtils.escreverJson(resp, new Gson().toJson(produtoService.obterTodosPorStatus(lativoParam)));

        else
            throw new IllegalArgumentException("Valor do parametro inválido.");
    }
}
