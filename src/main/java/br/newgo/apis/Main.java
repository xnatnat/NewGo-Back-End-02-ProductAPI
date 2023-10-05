package br.newgo.apis;

import br.newgo.apis.infrastructure.server.EmbeddedTomcatServer;

public class Main {
    public static void main(String[] args) {
        EmbeddedTomcatServer tomcat = new EmbeddedTomcatServer();
        tomcat.start();
    }
}