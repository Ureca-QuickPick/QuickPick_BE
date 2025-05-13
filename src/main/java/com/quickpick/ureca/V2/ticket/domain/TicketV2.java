package com.quickpick.ureca.V2.ticket.domain;
import com.quickpick.ureca.V2.common.domain.BaseEntityV2;
import com.quickpick.ureca.V2.userticket.domain.UserTicketV2;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketV2 extends BaseEntityV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime reserveDate;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTicketV2> userTickets = new ArrayList<>();

//    @Version
//    private Long version;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

