package com.example.bookstoreapplication.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderHistory implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_date_time")
    @Expose
    private String orderDateTime;
    @SerializedName("payment_type")
    @Expose
    private Integer paymentType;
    @SerializedName("payment_refrence")
    @Expose
    private String paymentRefrence;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("address_id")
    @Expose
    private Integer addressId;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("bag")
    @Expose
    private List<Cart> carts = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(String orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentRefrence() {
        return paymentRefrence;
    }

    public void setPaymentRefrence(String paymentRefrence) {
        this.paymentRefrence = paymentRefrence;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public List<Cart> getCart() {
        return carts;
    }

    public void setBag(List<Cart> bag) {
        this.carts = bag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
