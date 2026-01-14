package controller.customers;

import javafx.collections.ObservableList;
import model.dto.CustomerInfoDto;

public interface CustomerInfoService {
    ObservableList<CustomerInfoDto> getAllCustomers();
    int addCustomerDetails(String customerId,String firstName,String lastName,String email,String phone,String address,String city,String date);
    int deleteCustomer(CustomerInfoDto selectCustomer);
    int updateCustomerDetails(String customerId,String firstName,String lastName,String email,String phone,String address,String city,String date);

}
