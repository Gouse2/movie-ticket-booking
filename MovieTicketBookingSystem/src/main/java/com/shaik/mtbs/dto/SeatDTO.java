package com.shaik.mtbs.dto;

public class SeatDTO {
    private Long id;
    private int row;
    private int column;
    private String status;
    private double price;

    public SeatDTO(Long id, int row, int column, String status, double price) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.status = status;
        this.price = price;
    }

    // Getters
    public Long getId() { return id; }
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public String getStatus() { return status; }
    public double getPrice() { return price; }
}