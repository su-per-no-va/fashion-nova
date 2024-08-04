package com.supernova.fashionnova.domain.address;

import com.supernova.fashionnova.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String recipientNumber;

    @Column(nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String address;

    private String detail;

    @Column(nullable = false)
    private Boolean defaultAddress;

    @Builder
    public Address(User user, String name, String recipientName, String recipientNumber, String zipCode, String address, String detail) {
        this.user = user;
        this.name = name;
        this.recipientName = recipientName;
        this.recipientNumber = recipientNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.detail = detail;
        this.defaultAddress = false;
    }

    public Boolean isDefaultAddress() {
        return this.defaultAddress;
    }

}
