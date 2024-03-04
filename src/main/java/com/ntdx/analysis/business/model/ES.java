package com.ntdx.analysis.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 163返回的疫情数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ES {
    private ChinaTotal chinaTotal;
    private List<ChinaDayData> chinaDayList;
    private String lastUpdateTime;
    private String overseaLastUpdateTime;
    private List<AreaData> areaTree;
}
