package br.newgo.apis.presentation.controller;

import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.ResponseUtils;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//TODO criar uma handlerServlet que receba todas requisições.
public class ProdutoEstoqueBaixoController extends HttpServlet {
    private ProdutoService produtoService;

    /**
     * Inicializa o servlet e cria uma instância do serviço de produtos.
     * Lança uma exceção RuntimeException se ocorrer um erro durante a inicialização.
     */
    @Override
    public void init() {
        try {
            super.init();
        } catch (ServletException e) {
            throw new RuntimeException("Erro ao inicializar o servlet: " + e.getMessage(), e);
        }
        produtoService = new ProdutoService(new ProdutoDAO());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ResponseUtils.escreverJson(resp, new Gson().toJson(produtoService.obterTodosComEstoqueBaixo()));
    }
}