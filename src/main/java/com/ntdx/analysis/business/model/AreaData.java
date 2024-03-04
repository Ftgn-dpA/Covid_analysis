package com.ntdx.analysis.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 区域的疫情数据（包括，国，省，市）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaData {
    private TodayData today;
    private TotalData total;
    private ExtData extData;
    private String name;
    private String id;
    private String lastUpdateTime;
    private List<AreaData> children;
}
