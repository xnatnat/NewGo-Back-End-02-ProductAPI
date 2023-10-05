package br.newgo.apis.application.dtos;

public class AtualizacaoLoteProdutoDTO {
    private String hash;
    private String operacao;
    private double valor;

    public AtualizacaoLoteProdutoDTO(String hash, String operacao, double valor) {
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
