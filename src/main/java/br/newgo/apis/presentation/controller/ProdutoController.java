package br.newgo.apis.presentation.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Esta classe representa um controlador para operações relacionadas a produtos em um servlet.
 * Ele lida com as solicitações HTTP GET e POST para gerenciar produtos.
 */
public class ProdutoController extends HttpServlet {

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
    }

}
