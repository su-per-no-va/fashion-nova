package com.supernova.fashionnova.address;

import com.supernova.fashionnova.user.User;
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
    private int zipCode;

    @Column(nullable = false)
    private String address;

    private String detail;

    @Column(nullable = false)
    private boolean defaultAddress;

}
