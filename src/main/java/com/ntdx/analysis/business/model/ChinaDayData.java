package com.ntdx.analysis.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 中国每日数据情况
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChinaDayData {
    private String date;
    private TodayData today;
    private TotalData total;
    private ExtData extData;
    private String lastUpdateTime;
}
