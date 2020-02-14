package com.han.createbankrecfileapi.mapper;


import com.han.createbankrecfileapi.dto.BankSoftKey;
import com.han.createbankrecfileapi.dto.ProgramInfo;

import java.util.List;

public interface ProgramInfoMapper {
    /**
     * 获取所有的数据
     * @return
     */
    ProgramInfo selectAll();
}
