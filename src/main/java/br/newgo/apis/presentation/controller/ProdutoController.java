package br.newgo.apis.presentation.controller;

import br.newgo.apis.domain.services.ProdutoService;
import br.newgo.apis.infrastructure.dao.ProdutoDAO;
import br.newgo.apis.infrastructure.utils.RequestUtils;
import br.newgo.apis.infrastructure.utils.ResponseUtils;
import br.newgo.apis.presentation.dtos.ProdutoDTO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
     * Manipula solicitações HTTP POST para criar um novo produto.
     * @param req O objeto HttpServletRequest que representa a solicitação.
     * @param resp O objeto HttpServletResponse que representa a resposta.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        ProdutoDTO produtoDTO = produtoService.criar(RequestUtils.lerCorpoDaRequisicao(req));

        String uri = req.getRequestURL().toString() + "/" + produtoDTO.getHash();

        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setHeader("Location", uri);
        ResponseUtils.escreverJson(resp, produtoDTO.toJson());
    }

    /**
     * Manipula solicitações HTTP GET para obter todos os produtos.
     * @param req O objeto HttpServletRequest que representa a solicitação.
     * @param resp O objeto HttpServletResponse que representa a resposta.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        ResponseUtils.escreverJson(resp, new Gson().toJson(produtoService.obterTodos()));
    }
}
