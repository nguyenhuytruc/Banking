package com.cg.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransferHistoryDTO {

    private long id;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss Z", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    private long senderId;
    private String senderName;
    private long recipientId;
    private String recipientName;
    private BigDecimal transferAmount;
    private long fees;
    private BigDecimal feesAmount;
}
