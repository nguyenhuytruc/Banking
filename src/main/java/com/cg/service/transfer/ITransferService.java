package com.cg.service.transfer;

import com.cg.model.Transfer;
import com.cg.model.dto.TransferHistoryDTO;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;

public interface ITransferService extends IGeneralService<Transfer> {

    List<TransferHistoryDTO> getAllHistories();

    BigDecimal getSumFeesAmount();
}
