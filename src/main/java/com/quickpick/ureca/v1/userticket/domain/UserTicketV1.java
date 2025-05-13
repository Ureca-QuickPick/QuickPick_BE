package com.quickpick.ureca.v1.userticket.domain;

import com.quickpick.ureca.v1.ticket.domain.TicketV1;
import com.quickpick.ureca.v1.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTicketV1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ticket_id")
    private Long userTicketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private TicketV1 ticket;

    public UserTicketV1(User user, TicketV1 ticket) {
        this.user = user;
        this.ticket = ticket;
    }
}