package br.newgo.apis.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Esta classe representa um objeto de transferência de dados (DTO) para produtos.
 * Ela contém informações relevantes sobre um produto, como nome, descrição, preço, etc.
 */
public class ProdutoDTO {
    private UUID hash;
    private String nome;
    private String descricao;
    private String ean13;
    private double preco;
    private double quantidade;
    private double estoqueMin;

    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private String lativo;

    public ProdutoDTO(UUID hash, String nome, String descricao, String ean13, double preco, double quantidade, double estoqueMin, LocalDateTime dtCreate, LocalDateTime dtUpdate, String lativo) {
        this.hash = hash;
        this.nome = nome;
        this.descricao = descricao;
        this.ean13 = ean13;
        this.preco = preco;
        this.quantidade = quantidade;
        this.estoqueMin = estoqueMin;
        this.dtCreate = dtCreate;
        this.dtUpdate = dtUpdate;
        this.lativo = lativo;
    }

    public UUID getHash() {
        return hash;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEan13() {
        return ean13;
    }

    public double getPreco() {
        return preco;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public double getEstoqueMin() {
        return estoqueMin;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public String getLativo() {
        return lativo;
    }
}