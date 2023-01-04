package com.cg.model.dto;

import com.cg.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerCreateDTO {

    private long id;

//    @NotEmpty(message = "Vui lòng nhập tên đầy đủ")
//    @Size(min = 10, max = 50, message = "Họ tên có độ dài nằm trong khoảng 10-50 ký tự")
    private String fullName;

    private String email;

    //    @Pattern(regexp = "^[0][0-9]{9}$", message = "Vui lòng nhập đúng định dạng số điện thoại")
    private String phone;

    private String balance;

    private LocationRegionDTO locationRegion;

    public Customer toCustomer(){
        return new Customer()
            .setId(id)
            .setFullName(fullName)
            .setEmail(email)
            .setPhone(phone)
            .setBalance(new BigDecimal(0L))
            .setLocationRegion(locationRegion.toLocationRegion());
    }

    public Customer toUpCustomer(){
        return new Customer()
            .setId(id)
            .setFullName(fullName)
            .setEmail(email)
            .setPhone(phone)
            .setBalance(new BigDecimal(balance))
            .setLocationRegion(locationRegion.toLocationRegion());
    }
}
