package br.newgo.apis.infrastructure.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Classe utilitária para lidar com operações relacionadas a solicitações HTTP.
 */
public class RequestUtils {
    /**
     * Lê o corpo da requisição HTTP e o converte para uma string.
     *
     * @param req O objeto HttpServletRequest que representa a requisição HTTP.
     * @return Uma string contendo o corpo da requisição.
     * @throws RuntimeException Se ocorrer um erro ao ler o corpo da requisição.
     */
    static String lerCorpoDaRequisicao(HttpServletRequest req)  {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o corpo da requisição: " + e.getMessage(), e);
        }
    }
}
