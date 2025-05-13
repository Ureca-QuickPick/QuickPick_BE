package com.quickpick.ureca.v3.reserve.domain;

import com.quickpick.ureca.v3.common.BaseEntityV3;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReserveV3 extends BaseEntityV3 {

    @Column(name = "reserve_id")
    @Id @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private ReserveStatusV3 status;
}
