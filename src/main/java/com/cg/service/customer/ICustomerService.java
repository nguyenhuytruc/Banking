package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.model.dto.CustomerDTO;
import com.cg.model.dto.ICustomerDTO;
import com.cg.model.dto.RecipientDTO;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICustomerService extends IGeneralService<Customer> {

    List<Customer> findAllByDeletedIsFalse();

    List<CustomerDTO> getAllCustomerDTOByDeletedIsFalse();

    List<ICustomerDTO> getAllICustomerDTOByDeletedIsFalse();

    List<Customer> findAllByIdNot(long id);

    Boolean existsByIdEquals(long id);

    List<Customer> getAllByIdNot(long senderId);

    List<RecipientDTO> getAllRecipientDTO(long senderId);

    Customer deposit(Customer customer, Deposit deposit);

    Customer withdraw(Customer customer, Withdraw withdraw);

    Customer transfer(Transfer transfer);

    void softDelete(@Param("customerId") long customerId);
}
