package com.example.rentoolstcc;

public class ItemPedidoModel {
    private String dataRetirada, dataDevolução, horarioRetirada;

    public ItemPedidoModel() {
    }

    public ItemPedidoModel(String dataRetirada, String dataDevolução, String horarioRetirada) {
        this.dataRetirada = dataRetirada;
        this.dataDevolução = dataDevolução;
        this.horarioRetirada = horarioRetirada;

    }

    public String getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(String dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getDataDevolução() {
        return dataDevolução;
    }

    public void setDataDevolução(String dataDevolução) {
        this.dataDevolução = dataDevolução;
    }

    public String getHorarioRetirada() {
        return horarioRetirada;
    }

    public void setHorarioRetirada(String horarioRetirada) {
        this.horarioRetirada = horarioRetirada;
    }


}