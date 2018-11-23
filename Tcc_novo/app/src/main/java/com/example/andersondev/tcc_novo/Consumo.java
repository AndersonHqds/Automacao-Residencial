package com.example.andersondev.tcc_novo;

public class Consumo{

    int id, tipo;
    String data, litros;
    Double gastoTotal;

    public Consumo(){}

    public Consumo(int id, int tipo, String data, Double gastoTotal, String litros){
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.gastoTotal = gastoTotal;
        this.litros     = litros;
    }

    public Consumo(int tipo, String data, Double gastoTotal, String litros){
        this.tipo = tipo;
        this.data = data;
        this.gastoTotal = gastoTotal;
        this.litros = litros;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getGastoTotal() {
        return gastoTotal;
    }

    public void setGastoTotal(Double gastoTotal) {
        this.gastoTotal = gastoTotal;
    }

    public String getLitros() {
        return litros;
    }

    public void setLitros(String litros) {
        this.litros = litros;
    }
}
