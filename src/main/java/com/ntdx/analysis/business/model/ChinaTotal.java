package com.ntdx.analysis.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChinaTotal {
    private TodayData today;
    private TotalData total;
    private ExtData extData;

}
