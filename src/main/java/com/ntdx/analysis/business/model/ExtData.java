package com.ntdx.analysis.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 无症状感染者数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtData {
    private Integer noSymptom; // 无症状感染者
    private Integer incrNoSymptom; // 较昨日增加
}
