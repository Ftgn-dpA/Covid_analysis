package com.ntdx.analysis.business.controller;

import com.ntdx.analysis.business.model.CovidData;
import com.ntdx.analysis.business.service.DataCleanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    DataCleanService dataCleanService;


    /**
     * @return 返回ES数据到界面
     */
    @RequestMapping("/info")
    public CovidData getESData() {
        return dataCleanService.dataClean();
    }

    /**
     * @return 返回上海数据到界面
     */
    @RequestMapping("/getSHData")
    public List<CovidData> getSHData() {
        return dataCleanService.getSHData();
    }

    /**
     * @return 返回确诊Top10数据到界面
     */
    @RequestMapping("/getTop10Data")
    public List<CovidData> getTop10Data() {
        return dataCleanService.getTop10Data();
    }

    /**
     * @return 返回全国地图数据到界面
     */
    @RequestMapping("/getMapData")
    public List<CovidData> getMapData() {
        return dataCleanService.getMapData();
    }

    /**
     * @return 返回每日累计新增的数据到界面
     */
    @RequestMapping("/getNewData")
    public List<CovidData> getNewData() {
        return dataCleanService.getNewData();
    }

    /**
     * @return 返回昨日累计确诊、累计死亡、累计治愈的占比到界面
     */
    @RequestMapping("/getPieData")
    public List<CovidData> getPieData() {
        return dataCleanService.getPieData();
    }

    /**
     * @return 返回4日新增确诊、新增无症状感染者数据到界面
     */
    @RequestMapping("/getFourDayData")
    public List<CovidData> getFourDayData() {
        return dataCleanService.getFourDayData();
    }
}
