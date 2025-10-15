package ru.artem.NauJava.entity;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;// ID сеанса
    private String movieTitle;    // Название
    private LocalDateTime sessionDateTime; // Дата и время
    private int hallNumber;       // Номер зала
    private int rowNumber;        // Ряд
    private int seatNumber;       // Место
    private String customerName;  // Имя зрителя
    private String customerEmail; // Email зрителя
    private double price;         // Стоимость билета

    public Ticket(Long id, String movieTitle, LocalDateTime sessionDateTime,
                  int hallNumber, int rowNumber, int seatNumber,
                  String customerName, String customerEmail, double price) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.sessionDateTime = sessionDateTime;
        this.hallNumber = hallNumber;
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public LocalDateTime getSessionDateTime() {
        return sessionDateTime;
    }

    public void setSessionDateTime(LocalDateTime sessionDateTime) {
        this.sessionDateTime = sessionDateTime;
    }

    public int getHallNumber() {
        return hallNumber;
    }

    public void setHallNumber(int hallNumber) {
        this.hallNumber = hallNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isValidForSession() {
        return LocalDateTime.now().isBefore(sessionDateTime);
    }

    public String getSeatInfo() {
        return String.format("Зал %d, Ряд %d, Место %d", hallNumber, rowNumber, seatNumber);
    }

}
