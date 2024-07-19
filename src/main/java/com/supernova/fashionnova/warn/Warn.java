package com.supernova.fashionnova.warn;

import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "warn")
public class Warn extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String detail;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
