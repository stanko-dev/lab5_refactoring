package com.restaurant.dto;

import java.util.List;

public class OrderDTO {
    private final int orderId;
    private final String customerName;
    private final List<String> dishNames;
    private final double totalPrice;
    private final String status;

    public OrderDTO(int orderId, String customerName, List<String> dishNames,
                    double totalPrice, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.dishNames = List.copyOf(dishNames);
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public List<String> getDishNames() { return dishNames; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "OrderDTO{id=" + orderId + ", customer='" + customerName
                + "', total=" + totalPrice + ", status='" + status + "'}";
    }
}
