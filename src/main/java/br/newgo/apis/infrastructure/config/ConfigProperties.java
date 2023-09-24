package br.newgo.apis.infrastructure.config;


import br.newgo.apis.infrastructure.ConexaoBancoDados;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A classe ConfigProperties é responsável por carregar as configurações do aplicativo a partir de um arquivo
 * de propriedades e fornecer métodos para acessar essas configurações.
 */
public class ConfigProperties {
    private static final Properties properties;

    /**
     * Inicializa as propriedades do sistema a partir do arquivo "config.properties".
     * Este bloco estático é executado quando a classe é carregada pela primeira vez.
     * As propriedades são carregadas a partir do arquivo e ficam disponíveis para uso em toda a classe.
     *
     * @throws RuntimeException Se ocorrer um erro ao carregar as propriedades.
     */
    static {
        properties = new Properties();
        try (InputStream input = ConexaoBancoDados.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar config.properties", e);
        }
    }

    /**
     * Obtém o valor da propriedade associada à chave especificada.
     *
     * @param key A chave da propriedade a ser obtida.
     * @return O valor da propriedade ou null se a chave não for encontrada.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}