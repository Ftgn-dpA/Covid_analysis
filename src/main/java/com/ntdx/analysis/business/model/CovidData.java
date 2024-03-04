package com.ntdx.analysis.business.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 清洗之后的数据格式
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CovidData {
    private String date;
    private String country;
    private String province;
    private String city;
    private Integer input;
    private Integer compareInput;
    private Integer noSymptom;
    private Integer compareNoSymptom;
    private Integer storeConfirm;
    private Integer compareStoreConfirm;
    private Integer confirm;
    private Integer compareConfirm;
    private Integer dead;
    private Integer compareDead;
    private Integer heal;
    private Integer compareHeal;
}
