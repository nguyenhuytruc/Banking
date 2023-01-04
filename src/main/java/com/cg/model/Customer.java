package com.cg.model;

import com.cg.model.dto.CustomerCreateDTO;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.RecipientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
@Accessors(chain = true)
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    @OneToOne
    @JoinColumn(name = "location_region_id", referencedColumnName = "id", nullable = false)
    private LocationRegion locationRegion;

    @Column(precision = 12, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;


    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Deposit> deposits;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<Withdraw> withdraws;

    @OneToMany(mappedBy = "sender", fetch = FetchType.EAGER)
    private Set<Transfer> senders;

    @OneToMany(mappedBy = "recipient", fetch = FetchType.EAGER)
    private Set<Transfer> recipients;


    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", balance=" + balance +
                '}';
    }

    public CustomerCreateDTO toCustomerCreateDTO() {
        return new CustomerCreateDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance.toString())
                .setLocationRegion(locationRegion.toLocationRegionDTO());
    }

    public CustomerDTO toCustomerDTO() {
        return new CustomerDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegionDTO());
    }

    public RecipientDTO toRecipientDTO() {
        return new RecipientDTO()
                .setId(id)
                .setFullName(fullName);
    }

}
