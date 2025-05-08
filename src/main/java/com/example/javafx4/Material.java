package com.example.javafx4;

public class Material {
    private int id;
    private String type;
    private String name;
    private int minimalQuantity;
    private int quantity;
    private int cost;

    public Material(int id, String type, String name, int minimalQuantity, int quantity, int cost) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.minimalQuantity = minimalQuantity;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getMinimalQuantity() {
        return minimalQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getCost() {
        return cost;
    }

    public int getPurchaseCost() {
        return (minimalQuantity - quantity) * cost;
    }

    @Override
    public String toString() {
        return String.format("%s | %s\nМин количество: %d                                 Стоимость партии: %d\nКоличество: %d\nСтоимость: %d",
                type, name, minimalQuantity, getPurchaseCost(), quantity, cost);
    }
}
