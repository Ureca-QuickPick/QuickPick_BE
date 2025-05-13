package com.quickpick.ureca.V2.userticket.domain;

import com.quickpick.ureca.V2.ticket.domain.TicketV2;
import com.quickpick.ureca.V2.user.domain.UserV2;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class UserTicketV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ticket_id")
    private Long userTicketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserV2 user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private TicketV2 ticket;

    public UserTicketV2(UserV2 user, TicketV2 ticket) {
        this.user = user;
        this.ticket = ticket;
    }

}