package com.quickpick.ureca.OAuth.reserve.domain;

import com.quickpick.ureca.OAuth.common.domain.BaseEntity;
import com.quickpick.ureca.OAuth.reserve.status.ReserveStatus;
import com.quickpick.ureca.OAuth.user.domain.UserOAuth;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@Entity
@Getter
@NoArgsConstructor
public class Reserve extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private Long reserveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserOAuth user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReserveStatus status;
}