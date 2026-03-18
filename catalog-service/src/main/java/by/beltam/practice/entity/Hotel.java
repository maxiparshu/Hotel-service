package by.beltam.practice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String address;
    private String city;
    private Double rating;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<RoomType> roomTypes;
}
