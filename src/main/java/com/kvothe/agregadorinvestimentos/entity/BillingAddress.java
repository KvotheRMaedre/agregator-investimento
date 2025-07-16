package com.kvothe.agregadorinvestimentos.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "billing_address")
public class BillingAddress {

    @Id
    @Column(name = "billing_address_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID billingAddressId;

    @Column(name = "address")
    private String address;

    @Column(name = "number")
    private int number;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;

    public BillingAddress() {
    }

    public BillingAddress(UUID billingAddressId, String address, int number, Account account) {
        this.billingAddressId = billingAddressId;
        this.address = address;
        this.number = number;
        this.account = account;
    }

    public UUID getBillingAddressId() {
        return billingAddressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
