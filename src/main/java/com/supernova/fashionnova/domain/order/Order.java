package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int cost;

    @Column(nullable = false)
    private int discount;

    @Column(nullable = false)
    private int usedMileage;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int invoice;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;
}
