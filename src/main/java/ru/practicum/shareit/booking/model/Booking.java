package ru.practicum.shareit.booking.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.Instant;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private Instant start;
    @Column(name = "end_date", nullable = false)
    private Instant end;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    private User booker;
    @Column(name = "status", length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
