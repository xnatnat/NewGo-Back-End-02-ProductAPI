package br.newgo.apis.application.utils;

import br.newgo.apis.application.dtos.ProdutoDTO;
import br.newgo.apis.application.dtos.RespostaDTO;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
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
    public static String lerCorpoDaRequisicao(HttpServletRequest req)  {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o corpo da requisição: " + e.getMessage(), e);
        }
    }

    public static String gerarLocationHeader(HttpServletRequest req, List<RespostaDTO<Object>> respostas) {
        String requestUrl = req.getRequestURL().toString() + "/";

        List<String> uris = respostas.stream()
                .filter(resposta -> resposta.getDado() instanceof ProdutoDTO)
                .map(resposta -> requestUrl + ((ProdutoDTO) resposta.getDado()).getHash())
                .collect(Collectors.toList());

        return String.join(",", uris);
    }

    /**
     * Extrai um valor de hash a partir do caminho da requisição HTTP.
     *
     * @param req O HttpServletRequest contendo o caminho da requisição.
     * @return O valor de hash extraído do caminho da requisição.
     */
    public static String extrairHash(HttpServletRequest req) {
        return req.getPathInfo().substring(1);
    }
}