package br.newgo.apis.application.dtos;

public class PrecoLoteDTO {
    private String hash;
    private String operacao;
    private double valor;

    public PrecoLoteDTO(String hash, String operacao, double valor) {
        this.hash = hash;
        this.operacao = operacao;
        this.valor = valor;
    }

    public String getHash() {
        return hash;
    }

    public String getOperacao() {
        return operacao;
    }

    public double getValor() {
        return valor;
    }
}
