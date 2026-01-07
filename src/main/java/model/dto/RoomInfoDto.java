package model.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class RoomInfoDto {
    private String roomId;
    private String type;
    private double price;
    private int maxGuest;
    private boolean availability;
    private String description;
    private String floor;
}
