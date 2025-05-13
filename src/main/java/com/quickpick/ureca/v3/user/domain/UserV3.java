package com.quickpick.ureca.v3.user.domain;

import com.quickpick.ureca.v3.common.BaseEntityV3;
import com.quickpick.ureca.v3.user.constants.GenderV3;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserV3 extends BaseEntityV3 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderV3 genderV3;
}
