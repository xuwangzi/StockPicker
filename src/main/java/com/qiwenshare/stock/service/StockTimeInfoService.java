package com.qiwenshare.stock.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.qiwenshare.stock.api.IStockTimeInfoService;
import com.qiwenshare.stock.common.HttpsUtils;
import com.qiwenshare.stock.domain.StockTimeInfo;
import com.qiwenshare.stock.mapper.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class StockTimeInfoService implements IStockTimeInfoService {

    private static final Logger logger = LoggerFactory.getLogger(StockTimeInfoService.class);
    @Resource
    StockMapper stockMapper;

    @Override
    public List<StockTimeInfo> getStocktimebar(String stockNum) {
        StockTimeInfo stockTimeInfo = new StockTimeInfo();
        stockTimeInfo.setStockTableName("stocktimeinfo_" + stockNum);
        List<StockTimeInfo> result = selectStocktimeListByStockNum(stockTimeInfo);
        return result;
    }

    @Override
    public List<StockTimeInfo> selectStocktimeListByStockNum(StockTimeInfo stockTimeInfo) {
        return stockMapper.selectStocktimeListByStockNum(stockTimeInfo);
    }

    @Override
    public List<StockTimeInfo> crawlStockTimeInfoList(String stockNum) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("stockNum", stockNum);
        String sendResult = HttpsUtils.doGetString("http://localhost:5000/stock/getRealTimeInfo", param);
        JSONArray data = new JSONArray();
        try {
            data = JSONArray.parseArray(sendResult);
        } catch (JSONException e) {
            logger.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

//        String url = "http://push2his.eastmoney.com/api/qt/stock/trends2/get";
//        //String param = "begin=0&end=-1&select=time,price,volume";
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("fields1", "f1%2Cf2%2Cf3%2Cf4%2Cf5%2Cf6%2Cf7%2Cf8%2Cf9%2Cf10%2Cf11%2Cf12%2Cf13");
//        param.put("fields2", "f51%2Cf52%2Cf53%2Cf54%2Cf55%2Cf56%2Cf57%2Cf58");
//        param.put("ut", "7eea3edcaed734bea9cbfc24409ed989");
//        param.put("ndays", "1");
//        param.put("iscr", "0");
//        param.put("secid", "1." + stockNum);
//        String sendResult = HttpsUtils.doGetString(url, param);


        List<StockTimeInfo> stockTimeInfoList = new ArrayList<StockTimeInfo>();
// TODO 昨收价和涨跌额先暂时搁置
//        Double prevClose = objectToBigDecimal(data.get("prePrice"));
        Double sumPrice = 0D;
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = (Map) data.get(i);

            StockTimeInfo stockTimeInfo = new StockTimeInfo();

            Double price = objectToBigDecimal(map.get("PRICE"));
            sumPrice += price;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            stockTimeInfo.setDate(sdf.format(new Date()));
            stockTimeInfo.setTime((String) map.get("TIME"));
            stockTimeInfo.setStockCode(stockNum);
            stockTimeInfo.setPrice(objectToBigDecimal(map.get("PRICE")));
            stockTimeInfo.setAmount(objectToBigDecimal(map.get("AMOUNT")));
            stockTimeInfo.setVolume(objectToBigDecimal(map.get("VOLUME")));
//            stockTimeInfo.setUpDownRange((price-prevClose)/prevClose);
            stockTimeInfo.setAvgPrice(sumPrice/(i+1));
            stockTimeInfoList.add(stockTimeInfo);
        }
        return stockTimeInfoList;
    }

    @Override
    public void insertStockTimeInfo(String stockNum, List<StockTimeInfo> stocktimeinfo) {
        if(stocktimeinfo.size()<1) return;
        Map<String, Object> stockDayInfoMap = new HashMap<String, Object>();
        stockDayInfoMap.put("stockTimeInfoTable", "stocktimeinfo_" + stockNum);
        stockDayInfoMap.put("stocktimeinfo", stocktimeinfo);
        stockMapper.insertStockTimeInfo(stockDayInfoMap);
    }


    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }


    }
}
