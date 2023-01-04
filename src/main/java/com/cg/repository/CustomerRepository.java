package com.cg.repository;


import com.cg.model.Customer;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.ICustomerDTO;
import com.cg.model.dto.RecipientDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findAllByDeletedIsFalse();


    @Query("SELECT NEW com.cg.model.dto.CustomerDTO(" +
                "c.id, " +
                "c.fullName, " +
                "c.email, " +
                "c.phone, " +
                "c.balance, " +
                "c.locationRegion" +
            ") " +
            "FROM Customer AS c " +
            "WHERE c.deleted = false "
    )
    List<CustomerDTO> getAllCustomerDTOByDeletedIsFalse();


    @Query("SELECT " +
                "c.id AS id, " +
                "c.fullName AS fullName, " +
                "c.email AS email, " +
                "c.phone AS phone, " +
                "c.balance AS balance, " +
                "c.locationRegion AS locationRegion " +
            "FROM Customer AS c " +
            "WHERE c.deleted = false "
    )
    List<ICustomerDTO> getAllICustomerDTOByDeletedIsFalse();


    List<Customer> findAllByIdNot(long id);

    Boolean existsByIdEquals(long id);

    List<Customer> getAllByIdNot(long senderId);

    @Query("SELECT NEW com.cg.model.dto.RecipientDTO(" +
                "c.id, " +
                "c.fullName" +
            ") " +
            "FROM Customer AS c " +
            "WHERE c.id <> :senderId " +
            "AND c.deleted = false "
    )
    List<RecipientDTO> getAllRecipientDTO(@Param("senderId") long senderId);


    @Modifying
    @Query("UPDATE Customer AS c " +
            "SET c.balance = c.balance + :transactionAmount " +
            "WHERE c.id = :customerId"
    )
    void incrementBalance(@Param("transactionAmount") BigDecimal transactionAmount, @Param("customerId") long customerId);


    @Modifying
    @Query("UPDATE Customer AS c " +
            "SET c.balance = c.balance - :transactionAmount " +
            "WHERE c.id = :customerId"
    )
    void reduceBalance(@Param("transactionAmount") BigDecimal transactionAmount, @Param("customerId") long customerId);


    @Modifying
    @Query("UPDATE Customer AS c SET c.deleted = true WHERE c.id = :customerId")
    void softDelete(@Param("customerId") long customerId);
}
