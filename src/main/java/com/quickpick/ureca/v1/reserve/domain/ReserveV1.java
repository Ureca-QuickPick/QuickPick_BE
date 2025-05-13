package com.quickpick.ureca.v1.reserve.domain;

import com.quickpick.ureca.v1.common.domain.BaseEntity;
import com.quickpick.ureca.v1.reserve.status.ReserveStatusV1;
import com.quickpick.ureca.v1.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@Entity
@Getter
@NoArgsConstructor
public class ReserveV1 extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private Long reserveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReserveStatusV1 status;
}
