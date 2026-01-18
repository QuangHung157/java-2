package com.example.laptop.service.exception;

// File này dùng để tạo ra một loại lỗi riêng, giúp Controller biết khi nào là lỗi hết hàng
public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }
}