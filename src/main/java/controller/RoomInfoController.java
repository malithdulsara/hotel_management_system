package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dto.RoomInfoDto;

import java.sql.*;

public class RoomInfoController {

    public ObservableList<RoomInfoDto> getAllRooms(){
        ObservableList<RoomInfoDto> roomInfoDtos = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system","root","1234");

            String sql = "SELECT * FROM rooms";
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                RoomInfoDto roomInfoDto = new RoomInfoDto(
                        resultSet.getString("room_id"),
                        resultSet.getString("type"),
                        resultSet.getDouble("price_per_night"),
                        resultSet.getInt("max_guests"),
                        resultSet.getBoolean("availability"),
                        resultSet.getString("description"),
                        resultSet.getString("floor")
                );
                roomInfoDtos.add(roomInfoDto);
            }
            return roomInfoDtos;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void addRoomDetails(String roomId,String type,double pricePerNight,int maxGuest,boolean availability,String description,int floor){
        try {
            Connection connecction = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");
            PreparedStatement statement = connecction.prepareStatement("INSERT INTO rooms VALUES (?,?,?,?,?,?,?)");

            statement.setObject(1,roomId);
            statement.setObject(2,type);
            statement.setObject(3,pricePerNight);
            statement.setObject(4,maxGuest);
            statement.setObject(5,availability);
            statement.setObject(6,description);
            statement.setObject(7,floor);

            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public int deleteRoom(String roomId){
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");
            String sql = "DELETE FROM rooms WHERE room_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1,roomId);
            int rowsAffected = statement.executeUpdate();

            return rowsAffected;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateRoomDetails(String roomId,String type,double pricePerNight,int maxGuest,boolean availability,String description,int floor){
        int rows = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");

            String sql = "UPDATE rooms SET type=?, price_per_night=?, max_guests=?, availability=?, description=?, floor=? WHERE room_id=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setObject(1, type);
            statement.setObject(2, pricePerNight);
            statement.setObject(3, maxGuest);
            statement.setObject(4,availability);
            statement.setObject(5, description);
            statement.setObject(6, floor);
            statement.setObject(7, roomId);

            rows = statement.executeUpdate();



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rows;
    }


}
