package com.cg.controller.rest;


import com.cg.exception.DataInputException;
import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.model.dto.*;
import com.cg.service.customer.ICustomerService;
import com.cg.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private ICustomerService customerService;

    @GetMapping
    public ResponseEntity<?> findAllByDeletedIsFalse() {

        List<CustomerDTO> customers = customerService.getAllCustomerDTOByDeletedIsFalse();
//        List<ICustomerDTO> customers = customerService.getAllICustomerDTOByDeletedIsFalse();

        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Customer customer = customerOptional.get();

        return new ResponseEntity<>(customer.toCustomerDTO(), HttpStatus.OK);
    }

    @GetMapping("/get-all-recipients-without-sender/{senderId}")
    public ResponseEntity<?> getAllRecipientsWithoutSender(@PathVariable long senderId) {

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (!senderOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

//        List<Customer> recipients = customerService.getAllByIdNot(senderId);
//
//        List<RecipientDTO> recipientDTOS = new ArrayList<>();
//
//        for (Customer item : recipients) {
//            recipientDTOS.add(item.toRecipientDTO());
//        }

        List<RecipientDTO> recipientDTOS = customerService.getAllRecipientDTO(senderId);


        return new ResponseEntity<>(recipientDTOS, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomerCreateDTO> create(@RequestBody CustomerCreateDTO customerDTO) {

        customerDTO.setId(0L);
        Customer customer = customerDTO.toCustomer();

        Customer newCustomer = customerService.save(customer);

        customerDTO.setId(newCustomer.getId());
        customerDTO.setBalance("0");

        return new ResponseEntity<>(newCustomer.toCustomerCreateDTO(), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<CustomerCreateDTO> update(@RequestBody CustomerCreateDTO customerCreateDTO) {

        long customerId = customerCreateDTO.getId();

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerCreateDTO.toUpCustomer();

        Customer newCustomer = customerService.save(customer);

        customerCreateDTO.setId(newCustomer.getId());

        return new ResponseEntity<>(newCustomer.toCustomerCreateDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/deposit")
    public ResponseEntity<CustomerDTO> deposit(@RequestBody DepositDTO depositDTO) {

        long customerId = depositDTO.getCustomerId();

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Deposit deposit = new Deposit();
        BigDecimal transactionAmount = new BigDecimal(depositDTO.getTransactionAmount());
        deposit.setTransactionAmount(transactionAmount);
        deposit.setCustomer(customerOptional.get());

        Customer newCustomer = customerService.deposit(customerOptional.get(), deposit);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<CustomerDTO> withdraw(@RequestBody WithdrawDTO withdrawDTO) {

        long customerId = withdrawDTO.getCustomerId();

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        BigDecimal transactionAmount = new BigDecimal(Long.parseLong(withdrawDTO.getTransactionAmount()));

        if (customerOptional.get().getBalance().compareTo(transactionAmount) < 0){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setTransactionAmount(transactionAmount);
        withdraw.setCustomer(customerOptional.get());

        Customer newCustomer = customerService.withdraw(customerOptional.get(), withdraw);

        return new ResponseEntity<>(newCustomer.toCustomerDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferDTO transferDTO, BindingResult bindingResult) {

        new TransferDTO().validate(transferDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtil.mapErrorToResponse(bindingResult);
        }

        Optional<Customer> senderOptional = customerService.findById(transferDTO.getSenderId());

        if (!senderOptional.isPresent()) {
            throw new DataInputException("Thông tin người gửi không hợp lệ");
        }

        Optional<Customer> recipientOptional = customerService.findById(transferDTO.getRecipientId());

        if (!recipientOptional.isPresent()) {
            throw new DataInputException("Thông tin người nhận không hợp lệ");
        }

        Customer sender = senderOptional.get();

        if (senderOptional.get().getId() == recipientOptional.get().getId()) {
            throw new DataInputException("Thông tin người gửi và nhận không hợp lệ");
        }

        BigDecimal currentBalanceSender = sender.getBalance();

        String transferAmountStr = transferDTO.getTransferAmount();
        BigDecimal transferAmount = new BigDecimal(Long.parseLong(transferAmountStr));
        long fees = 10;
        BigDecimal feesAmount = transferAmount.multiply(new BigDecimal(fees)).divide(new BigDecimal(100L));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (currentBalanceSender.compareTo(transactionAmount) < 0) {
            throw new DataInputException("SỐ dư người gửi không đủ thực hiện giao dịch");
        }

        try {
            Transfer transfer = new Transfer();
            transfer.setId(0L);
            transfer.setSender(sender);
            transfer.setRecipient(recipientOptional.get());
            transfer.setTransferAmount(transferAmount);
            transfer.setFees(fees);
            transfer.setFeesAmount(feesAmount);
            transfer.setTransactionAmount(transactionAmount);

            Customer newSender = customerService.transfer(transfer);

            Optional<Customer> newRecipient = customerService.findById(transferDTO.getRecipientId());

            Map<String, CustomerDTO> results = new HashMap<>();
            results.put("sender", newSender.toCustomerDTO());
            results.put("recipient", newRecipient.get().toCustomerDTO());

            return new ResponseEntity<>(results, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new DataInputException("Vui lòng liên hệ Administrator");
        }
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<CustomerDTO> delete(@PathVariable long customerId) {

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            throw new DataInputException("Thông tin khách hàng cần xóa không hợp lệ");
        }

        try {
            customerService.softDelete(customerId);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataInputException("Vui lòng liên hệ Administrator");
        }
    }
}
