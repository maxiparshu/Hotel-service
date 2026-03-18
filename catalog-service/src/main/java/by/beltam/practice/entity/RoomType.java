package by.beltam.practice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_types")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private BigDecimal basePrice;
    private Integer capacity;
    private Integer totalRooms;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @ManyToMany
    @JoinTable(
            name = "room_type_amenities",
            joinColumns = @JoinColumn(name = "room_type_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")

    )
    private Set<Amenity> amenities;
}
