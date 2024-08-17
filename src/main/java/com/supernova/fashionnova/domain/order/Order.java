package com.supernova.fashionnova.domain.order;

import com.supernova.fashionnova.domain.delivery.DeliveryStatus;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.common.Timestamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long totalPrice;

    @Column(nullable = false)
    private Long cost;

    @Column(nullable = false)
    private int discount;

    @Column(nullable = false)
    private Long usedMileage;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Long invoice;

    // 결제 고유 번호
    private String tid;

    @Setter
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList;

    private String orderName;

    // 총 상품 개수가 아닌 건수임(바지 10개, 치마 4개 -> 총 2건)
    private int count;

    @Builder
    public Order(
        User user,
        Long totalPrice,
        Long cost,
        int discount,
        Long usedMileage,
        String address,
        Long invoice,
        DeliveryStatus deliveryStatus,
        OrderStatus orderStatus) {

        this.user = user;
        this.totalPrice = totalPrice;
        this.cost = cost;
        this.discount = discount;
        this.usedMileage = usedMileage;
        this.address = address;
        this.invoice = invoice;
        this.orderStatus = orderStatus;
        this.deliveryStatus = deliveryStatus;
    }

    // 연관관계 편의 메서드(양방향) -> 근데 위에 cascade 해놔서 쓸 일 없음..
    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
        orderDetailList.forEach(e -> e.setOrder(this));
    }

    public void setOrderPerfect(String orderName, int count) {
        this.orderName = orderName;
        this.count = count;
    }

    public void updateTid(String tid) {
        this.tid = tid;
    }

}
