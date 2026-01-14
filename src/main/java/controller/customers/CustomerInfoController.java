package controller.customers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dto.CustomerInfoDto;

import java.sql.*;

public class CustomerInfoController implements CustomerInfoService{
public ObservableList<CustomerInfoDto> getAllCustomers(){
    ObservableList<CustomerInfoDto> customerInfoDtos = FXCollections.observableArrayList();
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");
        String sql = "SELECT * FROM customers";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            CustomerInfoDto customerInfoDto = new CustomerInfoDto(
                    resultSet.getString("customer_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getString("address"),
                    resultSet.getString("city"),
                    resultSet.getString("registered_date")
            );
            customerInfoDtos.add(customerInfoDto);
        }


    } catch (SQLException e) {
        e.printStackTrace();
    }
    return customerInfoDtos;
}
public int addCustomerDetails(String customerId,String firstName,String lastName,String email,String phone,String address,String city,String date){
    int rowAffected;
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");

        String sql = "INSERT INTO customers (customer_id,first_name,last_name,email,phone,address,city,registered_date)" + "VALUES (?,?,?,?,?,?,?,?)";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, customerId);
        statement.setString(2, firstName);
        statement.setString(3, lastName);
        statement.setString(4, email);
        statement.setString(5, phone);
        statement.setString(6, address);
        statement.setString(7, city);
        statement.setString(8, date);

         rowAffected = statement.executeUpdate();

    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return rowAffected;
}
public int deleteCustomer(CustomerInfoDto selectCustomer){
    int rowsAffected;
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);


        statement.setString(1, selectCustomer.getCustomerId());

        rowsAffected = statement.executeUpdate();



    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return rowsAffected;
}

public int updateCustomerDetails(String customerId,String firstName,String lastName,String email,String phone,String address,String city,String date){
    int rowsAffected = 0;
    try {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation_system", "root", "1234");

        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone=?, address=?, city=? WHERE customer_id=?";

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);
        statement.setInt(4, Integer.parseInt(phone));
        statement.setString(5, address);
        statement.setString(6, city);

        statement.setString(7, customerId);

        rowsAffected = statement.executeUpdate();


    } catch (SQLException e) {
        e.printStackTrace();
    }
    return rowsAffected;
}


}
