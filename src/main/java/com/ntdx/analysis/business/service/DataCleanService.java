package com.ntdx.analysis.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ntdx.analysis.business.dao.CovidDataDao;
import com.ntdx.analysis.business.model.*;
import com.ntdx.analysis.business.utils.OkHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class DataCleanService {

    @Value("${const.get163ESInfoURL}")
    private String url;
    @Value("${const.DataSaveDir}")
    private String dir;
    @Value("${const.SaveNamePrefix}")
    private String namePrefix;

    @Resource
    CovidDataDao covidDataDao;

    public CovidData dataClean() {
        // 1. 从163获取数据
        // 通过OkHttpUtils工具访问接口获取数据，返回的数据为JSON格式String数据
        String resStr = OkHttpUtils.okHttpsReq(url);
        // 将返回的JSON格式字符串转为JSON对象
        JSONObject json = JSON.parseObject(resStr);


        // 2.将数据转为ES对象
        // 2.1.判断数据是否网络请求成功，成功才做ES转换
        ES es = null;
        if (json.containsKey("code") && json.containsKey("data")) {
            Integer code = json.getInteger("code");
            if (code == 10000) {
                // 代表成功返回，通过json的key返回value（json格式的字符串）
                String dataStr = json.getString("data");

                // 将返回的dataStr转为ES对象
                es = JSON.parseObject(dataStr, new TypeReference<ES>() {
                });
            }
        }

        // 3. 对数据进行清洗，获取我们需要的数据
        if (es != null) {
            return cleanDataHandle(es);
        }

        return null;
    }

    private CovidData cleanDataHandle(ES es) {
        // 1.获取全国昨日疫情概况数据
        // 创建昨天的日期
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        String beforeDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

        ChinaDayData chinaDayData = es.getChinaDayList().get(es.getChinaDayList().size() - 1);
        // 若数据是昨天的数据，则直接使用
        if (chinaDayData.getDate().equals("beforeDay")) {
            // 若不是昨天的数据，则设置为null
            chinaDayData = null;
        }
        ChinaTotal chinaTotal = es.getChinaTotal();

        // 2.查找出中国的数据
        List<AreaData> areaTree = es.getAreaTree();
        AreaData chinaData = null;
        if (areaTree != null) {
            for (int i = 0; i < areaTree.size(); i++) {
                AreaData areaData = areaTree.get(i);
                if (areaData.getName().equals("中国")) {
                    chinaData = areaData;
                    break;
                }
            }
        }

        // 定义一个List用来存放全国所有城市的疫情信息
        List<CovidData> covidInfoList = new ArrayList<>();

        // 3.将全国概览数据保存为一个对象
        CovidData covidData = null;
        if (chinaData != null) {
            covidData = new CovidData();
            covidData.setDate(beforeDay);
            covidData.setCountry("中国");

            covidData.setInput(chinaTotal.getTotal().getInput());
            covidData.setCompareInput(chinaTotal.getToday().getInput());
            covidData.setNoSymptom(chinaTotal.getExtData().getNoSymptom());
            covidData.setCompareNoSymptom(chinaTotal.getExtData().getIncrNoSymptom());
            covidData.setCompareStoreConfirm(chinaTotal.getToday().getStoreConfirm());
            covidData.setConfirm(chinaTotal.getTotal().getConfirm());
            covidData.setCompareConfirm(chinaTotal.getToday().getConfirm());
            covidData.setHeal(chinaTotal.getTotal().getHeal());
            covidData.setCompareHeal(chinaTotal.getToday().getHeal());
            covidData.setDead(chinaTotal.getTotal().getDead());
            covidData.setCompareDead(chinaTotal.getToday().getDead());

            // 累计现有确诊 = 累计确诊 - 累计死亡 - 累计治愈
            covidData.setStoreConfirm(chinaDayData != null ? chinaDayData.getTotal().getStoreConfirm()
                    : covidData.getConfirm() - covidData.getDead() - covidData.getHeal());

            covidInfoList.add(covidData);

            // 4.将所有地级市的数据取出
            List<AreaData> provList = chinaData.getChildren();
            if (provList != null) {
                // 循环获取每个省份并且拿到城市信息
                for (AreaData proArea : provList) {
                    List<AreaData> cityList = proArea.getChildren();
                    if (cityList != null) {
                        for (AreaData cityArea : cityList) {
                            // 每个城市的疫情信息
                            CovidData cityCovidInfo = new CovidData();
                            cityCovidInfo.setDate(beforeDay);
                            cityCovidInfo.setCountry("中国");
                            cityCovidInfo.setProvince(proArea.getName());
                            cityCovidInfo.setCity(cityArea.getName());

                            cityCovidInfo.setInput(cityArea.getTotal().getInput());
                            cityCovidInfo.setCompareInput(cityArea.getToday().getInput());
                            cityCovidInfo.setNoSymptom(cityArea.getExtData().getNoSymptom());
                            cityCovidInfo.setCompareNoSymptom(cityArea.getExtData().getIncrNoSymptom());
                            cityCovidInfo.setStoreConfirm(cityArea.getTotal().getStoreConfirm());
                            cityCovidInfo.setCompareStoreConfirm(cityArea.getToday().getStoreConfirm());
                            cityCovidInfo.setConfirm(cityArea.getTotal().getConfirm());
                            cityCovidInfo.setCompareConfirm(cityArea.getToday().getConfirm());
                            cityCovidInfo.setHeal(cityArea.getTotal().getHeal());
                            cityCovidInfo.setCompareHeal(cityArea.getToday().getHeal());
                            cityCovidInfo.setDead(cityArea.getTotal().getDead());
                            cityCovidInfo.setCompareDead(cityArea.getToday().getDead());

                            // 将每个城市的信息放到列表中
                            covidInfoList.add(cityCovidInfo);
                        }
                    }
                }
            }

        }
        // 将数据保存到本地文件并将JSON字符串返回
        String jsonStr = JSON.toJSONString(covidInfoList);
        save2Disk(jsonStr);

        return covidData;
    }

    /**
     * 保存数到本地磁盘
     *
     * @param info - 需要保存的信息
     */
    private void save2Disk(String info) {
        // 保存的文件名 保存的文件目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = sdf.format(new Date());
        String path = dir + "/" + namePrefix + fileName + ".json";

        File file = new File(path);
        try {
            File folder = new File(dir);
            // 判断目录是否存在，若不存在则创建
            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }

            // 判断文件是否存在，若不存在则创建
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(path));
            out.write(info);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 返回上海的城市疫情数据
     */
    public List<CovidData> getSHData() {
        return this.covidDataDao.getSHData();
    }

    /**
     * @return 获取全国疫情现有确诊最高的10个城市
     */
    public List<CovidData> getTop10Data() {
        return this.covidDataDao.getTop10Data();
    }

    /**
     * @return 获取全国地图数据
     */
    public List<CovidData> getMapData() {
        return this.covidDataDao.getMapData();
    }

    /**
     * @return 获取每日累计新增的数据
     */
    public List<CovidData> getNewData() {
        return this.covidDataDao.getNewData();
    }

    /**
     * @return 获取昨日累计确诊、累计死亡、累计治愈的占比
     */
    public List<CovidData> getPieData() {
        return this.covidDataDao.getPieData();
    }

    /**
     * @return 获取4日新增确诊、新增无症状感染者数据
     */
    public List<CovidData> getFourDayData() {
        return this.covidDataDao.getFourDayData();
    }
}
