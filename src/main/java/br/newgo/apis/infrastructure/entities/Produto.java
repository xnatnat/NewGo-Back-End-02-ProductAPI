package br.newgo.apis.infrastructure.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa um produto em um sistema de gerenciamento de estoque.
 *
 * Esta classe contém informações sobre o produto, incluindo seu nome, descrição, código EAN13, preço, quantidade em estoque
 * e estoque mínimo.
 */
public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private UUID hash;
    private String nome;
    private String descricao;
    private String ean13;
    private double preco;
    private double quantidade;
    private double estoqueMin;

    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private Boolean lativo;

    public Produto(UUID hash, String nome, String descricao, String ean13, double preco, double quantidade, double estoqueMin, LocalDateTime dtCreate, LocalDateTime dtUpdate, Boolean lativo) {
        setHash(hash);
        setNome(nome);
        setDescricao(descricao);
        setEan13(ean13);
        setPreco(preco);
        setQuantidade(quantidade);
        setEstoqueMin(estoqueMin);
        setDtCreate(dtCreate);
        setDtUpdate(dtUpdate);
        setLativo(lativo);
    }

    public Produto(String nome, String descricao, String ean13, double preco, double quantidade, double estoqueMin) {
        setNome(nome);
        setDescricao(descricao);
        setEan13(ean13);
        setPreco(preco);
        setQuantidade(quantidade);
        setEstoqueMin(estoqueMin);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getHash() {
        return hash;
    }

    public void setHash(UUID hash) {
        this.hash = hash;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getEstoqueMin() {
        return estoqueMin;
    }

    public void setEstoqueMin(double estoqueMin) {
        this.estoqueMin = estoqueMin;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public Boolean isLativo() {
        return lativo;
    }

    public void setLativo(Boolean lativo) {
        this.lativo = lativo;
    }
}
