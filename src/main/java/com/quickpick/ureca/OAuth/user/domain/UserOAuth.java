package com.quickpick.ureca.OAuth.user.domain;

import com.quickpick.ureca.OAuth.common.domain.BaseEntity;
import com.quickpick.ureca.OAuth.userticket.domain.UserTicket;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table
@Entity
@Getter
@NoArgsConstructor
public class UserOAuth extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String gender;

    @Builder
    public UserOAuth(String id, String password, String name, Integer age, String gender) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTicket> userTickets = new ArrayList<>();

    @Override               //사용자의 권한 목록 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override       //사용자 id 반환 (고유한 이름)
    public String getUsername() {
        return  id;
    }

    @Override       //사용자 비밀번호 반환
    public String getPassword() {
        return password;
    }

    @Override       //계정이 만료 되었는지 확인
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override       //계정이 잠겼는지 확인
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override       //비밀번호 만료 확인
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override       //계정 사용여부 확인
    public boolean isEnabled() {
        return true;
    }
}
