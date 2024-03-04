package com.ntdx.analysis.business.dao;

import com.ntdx.analysis.business.model.CovidData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class CovidDataDao {
    @Resource
    JdbcTemplate jdbcTemplate;

    public List<CovidData> getSHData() {
        String sql = "SELECT date, city, confirm " +
                "FROM p_sh " +
                "WHERE date = '" + getBeforeDay() + "' " +
                "ORDER BY confirm DESC " +
                "LIMIT 10";
        List<CovidData> list = jdbcTemplate.query(sql, new RowMapper<CovidData>() {
            @Override
            public CovidData mapRow(ResultSet rs, int rowNum) throws SQLException {
                // 此处参数数据库字段名
                String date = rs.getString("date");
                String city = rs.getString("city");
                Integer confirm = rs.getInt("confirm");

                CovidData covidData = new CovidData();
                covidData.setDate(date);
                covidData.setCity(city);
                covidData.setConfirm(confirm);

                return covidData;
            }
        });

        return list;
    }

    public List<CovidData> getTop10Data() {
        String sql = "SELECT date, city, storeConfirm " +
                "FROM p_top " +
                "WHERE date = '" + getBeforeDay() + "' " +
                "ORDER BY storeConfirm DESC " +
                "LIMIT 10";
        List<CovidData> list = jdbcTemplate.query(sql, new RowMapper<CovidData>() {
            @Override
            public CovidData mapRow(ResultSet rs, int rowNum) throws SQLException {
                // 此处参数数据库字段名
                String date = rs.getString("date");
                String city = rs.getString("city");
                Integer storeConfirm = rs.getInt("storeConfirm");

                CovidData covidData = new CovidData();
                covidData.setDate(date);
                covidData.setCity(city);
                covidData.setStoreConfirm(storeConfirm);

                return covidData;
            }
        });

        return list;
    }

    // 获取地图今天的数据
    public List<CovidData> getMapData() {
        String sql = "SELECT date, province, storeConfirm " +
                "FROM p_map " +
                "WHERE date = '" + getBeforeDay() + "' ";
        List<CovidData> list = jdbcTemplate.query(sql, new RowMapper<CovidData>() {
            @Override
            public CovidData mapRow(ResultSet rs, int rowNum) throws SQLException {
                // 此处参数数据库字段名
                String date = rs.getString("date");
                String province = rs.getString("province");
                Integer storeConfirm = rs.getInt("storeConfirm");

                CovidData covidData = new CovidData();
                covidData.setDate(date);
                covidData.setProvince(province);
                covidData.setStoreConfirm(storeConfirm);

                return covidData;
            }
        });

        return list;
    }

    // 获取每日累计新增的数据
    public List<CovidData> getNewData() {
        String sql = "SELECT date, compareConfirm " +
                "FROM p_compare_confirm " +
                "ORDER BY date DESC LIMIT 4";
        List<CovidData> list = jdbcTemplate.query(sql, new RowMapper<CovidData>() {
            @Override
            public CovidData mapRow(ResultSet rs, int rowNum) throws SQLException {
                // 此处参数数据库字段名
                String date = rs.getString("date");
                Integer compareConfirm = rs.getInt("compareConfirm");

                CovidData covidData = new CovidData();
                covidData.setDate(date);
                covidData.setCompareConfirm(compareConfirm);

                return covidData;
            }
        });

        return list;
    }

    // 获取昨日累计确诊、累计死亡、累计治愈的占比
    public List<CovidData> getPieData() {
        String sql = "SELECT date, confirm, dead, heal " +
                "FROM p_total " +
                "WHERE date = '" + getBeforeDay() + "'";
        List<CovidData> list = jdbcTemplate.query(sql, new RowMapper<CovidData>() {
            @Override
            public CovidData mapRow(ResultSet rs, int rowNum) throws SQLException {
                // 此处参数数据库字段名
                String date = rs.getString("date");
                Integer confirm = rs.getInt("confirm");
                Integer dead = rs.getInt("dead");
                Integer heal = rs.getInt("heal");

                CovidData covidData = new CovidData();
                covidData.setDate(date);
                covidData.setConfirm(confirm);
                covidData.setDead(dead);
                covidData.setHeal(heal);

                return covidData;
            }
        });

        return list;
    }

    // 获取4日新增确诊、新增无症状感染者数据
    public List<CovidData> getFourDayData() {
        String sql = "SELECT date, compareConfirm, compareNoSymptom " +
                "FROM p_four_day " +
                "ORDER BY date DESC LIMIT 4";
        List<CovidData> list = jdbcTemplate.query(sql, new RowMapper<CovidData>() {
            @Override
            public CovidData mapRow(ResultSet rs, int rowNum) throws SQLException {
                // 此处参数数据库字段名
                String date = rs.getString("date");
                Integer compareConfirm = rs.getInt("compareConfirm");
                Integer compareNoSymptom = rs.getInt("compareNoSymptom");

                CovidData covidData = new CovidData();
                covidData.setDate(date);
                covidData.setCompareConfirm(compareConfirm);
                covidData.setCompareNoSymptom(compareNoSymptom);

                return covidData;
            }
        });

        return list;
    }

    // 获取昨天日期的String格式
    private static String getBeforeDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        String beforeDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());

        return beforeDay;
    }
}
