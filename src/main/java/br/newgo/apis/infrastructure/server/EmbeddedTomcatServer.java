package br.newgo.apis.infrastructure.server;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * Classe que encapsula a inicialização e parada de um servidor Tomcat incorporado.
 */
public class EmbeddedTomcatServer {
    private final Tomcat tomcat;

    /**
     * Inicializa um servidor Tomcat incorporado com configurações padrão.
     */
    public EmbeddedTomcatServer() {
        this.tomcat = new Tomcat();
        configurarTomcat();
    }

    /**
     * Configura o servidor Tomcat com as opções desejadas, como a porta e os recursos da aplicação web.
     */
    private void configurarTomcat() {
        tomcat.setPort(8080);
        configurarWebapp();
        tomcat.enableNaming();
        tomcat.getConnector();
    }

    /**
     * Configura os recursos da aplicação web, incluindo classes e diretórios.
     */
    private void configurarWebapp() {
        String webappDir = "src/main/webapp/";
        String webInfClassesDir = "target/classes";

        StandardContext context = adicionarAplicacaoWeb(webappDir);
        adicionarRecursosWebInf(context, webInfClassesDir);
    }

    /**
     * Adiciona a aplicação web ao contexto do Tomcat.
     *
     * @param webappDir O diretório da aplicação web.
     * @return O contexto da aplicação web.
     */
    private StandardContext adicionarAplicacaoWeb(String webappDir) {
        StandardContext context = (StandardContext) tomcat.addWebapp(
                "", new File(webappDir).getAbsolutePath());
        return context;
    }

    /**
     * Adiciona os recursos da pasta WEB-INF/classes ao contexto da aplicação web.
     *
     * @param context O contexto da aplicação web.
     * @param webInfClassesDir O diretório das classes do WEB-INF.
     */
    private void adicionarRecursosWebInf(StandardContext context, String webInfClassesDir) {
        WebResourceRoot resources = new StandardRoot(context);

        String webInfClassesPath = "/WEB-INF/classes";

        resources.addPreResources(new DirResourceSet(resources, webInfClassesPath,
                new File(webInfClassesDir).getAbsolutePath(), "/"));

        context.setResources(resources);
    }

    /**
     * Inicia o servidor Tomcat incorporado e aguarda conexões.
     */
    public void start() {
        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            handleException(e);
        }
    }

    /**
     * Para o servidor Tomcat incorporado de forma limpa.
     */
    public void stop() {
        try {
            tomcat.stop();
        } catch (LifecycleException e) {
            handleException(e);
        }
    }

    /**
     * Trata exceções do ciclo de vida do Tomcat imprimindo rastreamentos de pilha.
     *
     * @param e A exceção a ser tratada.
     */
    private void handleException(LifecycleException e) {
        e.printStackTrace();
    }
}