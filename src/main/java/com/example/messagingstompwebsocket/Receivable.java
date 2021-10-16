package com.example.messagingstompwebsocket;

import java.math.BigDecimal;

public class Receivable {

    private String hash;
    private BigDecimal valor;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
