package com.supernova.fashionnova.domain.mileage;

import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.common.Timestamped;
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
@Table(name = "mileage")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mileage extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long mileage;

    @Builder
    public Mileage(User user, Long mileage) {
        this.user = user;
        this.mileage = mileage;
    }

    public void updateMileage(Long mileage) {
        this.mileage = mileage;
    }

}
