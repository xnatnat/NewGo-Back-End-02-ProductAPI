package br.newgo.apis.application.dtos;

public class RespostaDTO<T> {
    private String status;
    private String messagem;

    private T dado;

    public RespostaDTO(String status, String messagem, T data) {
        this.status = status;
        this.messagem = messagem;
        this.dado = data;
    }

    public RespostaDTO(String status, String messagem) {
        this.status = status;
        this.messagem = messagem;
    }

    public String getStatus() {
        return status;
    }

    public String getMessagem() {
        return messagem;
    }

    public T getDado() {
        return dado;
    }
}
