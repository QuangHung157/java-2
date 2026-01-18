package com.example.laptop.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ReceiverDTO {

    @NotBlank(message = "Vui lòng nhập tên người nhận")
    private String receiverName;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    private String receiverAddress;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(regexp = "^(0\\d{9,10})$", message = "Số điện thoại không hợp lệ")
    private String receiverPhone;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
}
