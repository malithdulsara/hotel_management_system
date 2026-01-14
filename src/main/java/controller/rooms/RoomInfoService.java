package controller.rooms;

import javafx.collections.ObservableList;
import model.dto.RoomInfoDto;

public interface RoomInfoService {
    ObservableList<RoomInfoDto> getAllRooms();
    void addRoomDetails(String roomId,String type,double pricePerNight,int maxGuest,boolean availability,String description,int floor);
    int deleteRoom(String roomId);
    int updateRoomDetails(String roomId,String type,double pricePerNight,int maxGuest,boolean availability,String description,int floor);


    }
