package br.newgo.apis.application.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Classe utilitária para lidar com respostas HTTP.
 */
public class ResponseUtils {

    /**
     * Escreve uma resposta JSON em um HttpServletResponse.
     *
     * Este método configura o tipo de conteúdo como "application/json" e a codificação de caracteres como "UTF-8"
     * no HttpServletResponse e escreve a resposta JSON fornecida no corpo da resposta. Em caso de exceção de E/S
     * ao escrever a resposta, a exceção é tratada utilizando o serviço de tratamento de exceções especificado
     * e uma resposta de erro adequada é enviada.
     *
     * @param resp     O objeto HttpServletResponse no qual a resposta JSON será escrita.
     * @param resposta A resposta JSON a ser escrita no HttpServletResponse.
     * @throws RuntimeException Se ocorrer um erro ao lidar com a exceção de E/S.
     */
    public static void escreverJson(HttpServletResponse resp, String resposta) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print(resposta);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever a resposta JSON: " + e.getMessage(), e);
        }
    }
}