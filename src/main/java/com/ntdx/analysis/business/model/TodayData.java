package com.ntdx.analysis.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodayData {
    private Integer confirm; // 确诊
    private Integer suspect; // 疑似
    private Integer heal; // 治愈
    private Integer dead; // 死亡
    private Integer severe;
    private Integer storeConfirm; // 现有确诊
    private Integer input; // 境外输入
}
