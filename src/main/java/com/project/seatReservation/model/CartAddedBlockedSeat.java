package com.project.seatReservation.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_added_blocked_seat")
public class CartAddedBlockedSeat {
    public CartAddedBlockedSeat() {
        this.modifiedTime = Timestamp.valueOf(LocalDateTime.now());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartAddedBlockedSeatId;

    @ManyToOne
    @JoinColumn(name = "block_seat_id")
    private BlockedSeat blockedSeat;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private Timestamp modifiedTime;

    public int getCartAddedBlockedSeatId() {
        return cartAddedBlockedSeatId;
    }

    public void setCartAddedBlockedSeatId(int cartAddedBlockedSeatId) {
        this.cartAddedBlockedSeatId = cartAddedBlockedSeatId;
    }

    public BlockedSeat getBlockedSeat() {
        return blockedSeat;
    }

    public void setBlockedSeat(BlockedSeat blockedSeat) {
        this.blockedSeat = blockedSeat;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
