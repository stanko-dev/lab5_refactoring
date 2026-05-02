package com.restaurant.dto;

import java.util.List;

public class OrderDTO {
    public final int orderId;
    public final String customerName;
    public final List<String> dishNames;
    public final double totalPrice;
    public final String status;

    public OrderDTO(int orderId, String customerName, List<String> dishNames, double totalPrice, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.dishNames = List.copyOf(dishNames);
        this.totalPrice = totalPrice;
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderDTO{id=" + orderId + ", customer='" + customerName + "', total=" + totalPrice + ", status='" + status + "'}";
    }
}
