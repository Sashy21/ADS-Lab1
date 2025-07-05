package model;

import java.io.Serializable;

public class FruitPrice implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fruitName;
    private double price;

    public FruitPrice(String fruitName, double price) {
        this.fruitName = fruitName;
        this.price = price;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FruitPrice{" +
                "fruitName='" + fruitName + '\'' +
                ", price=" + price +
                '}';
    }
}