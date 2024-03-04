package com.qiwenshare.stock.service;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.qiwenshare.stock.api.IStockBidService;
import com.qiwenshare.stock.common.HttpsUtils;
import com.qiwenshare.stock.domain.StockBidBean;
import com.qiwenshare.stock.mapper.StockBidMapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StockBidService implements IStockBidService {

    @Resource
    StockBidMapper stockBidMapper;
    private static final Logger logger = LoggerFactory.getLogger(StockBidService.class);


    @Override
    public void updateStockBid(StockBidBean stockBidBean) {
        LambdaUpdateWrapper<StockBidBean> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(StockBidBean::getStockNum, stockBidBean.getStockNum());
        stockBidMapper.update(stockBidBean, lambdaUpdateWrapper);

    }

    @Override
    public StockBidBean getStockBidBean(String stockNum) {
        LambdaQueryWrapper<StockBidBean> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(StockBidBean::getStockNum, stockNum);
        List<StockBidBean> stockBeanList = stockBidMapper.selectList(lambdaQueryWrapper);
        if (stockBeanList.isEmpty()) {
            return null;
        }
        return stockBeanList.get(0);
    }

    @Override
    public void insertStockBid(StockBidBean stockBidBean) {
        stockBidMapper.insert(stockBidBean);
    }

    @Override
    public StockBidBean crawlBidByStockBean(String stockNum) {

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("stockNum", stockNum);
        String sendResult = HttpsUtils.doGetString("http://localhost:5000/stock/getRealTimeQuote", param);
        JSONObject data = new JSONObject();
        StockBidBean stockBidBean = new StockBidBean();
        try {
            if(JSONArray.parseArray(sendResult).size()<1){
                stockBidBean.setStockNum(stockNum);
                return stockBidBean;
            }
            data = JSONArray.parseArray(sendResult).getJSONObject(0);
        } catch (JSONException e) {
            logger.error("解析jsonb报错：" + data.toJSONString());
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
//        String url = "http://push2.eastmoney.com/api/qt/stock/get";
//        //String param = "select=ask,bid";
//        Map<String, Object> param = new HashMap<String, Object>();
//        param.put("ut", "fa5fd1943c7b386f172d6893dbfba10b");
//        param.put("invt", "2");
//        param.put("fltt", "2");
//        param.put("fields", "f43,f57,f58,f169,f170,f46,f44,f51,f168,f47,f164,f163,f116,f60,f45,f52,f50,f48,f167,f117,f71,f161,f49,f530,f135,f136,f137,f138,f139,f141,f142,f144,f145,f147,f148,f140,f143,f146,f149,f55,f62,f162,f92,f173,f104,f105,f84,f85,f183,f184,f185,f186,f187,f188,f189,f190,f191,f192,f107,f111,f86,f177,f78,f110,f262,f263,f264,f267,f268,f250,f251,f252,f253,f254,f255,f256,f257,f258,f266,f269,f270,f271,f273,f274,f275,f127,f199,f128,f193,f196,f194,f195,f197,f80,f280,f281,f282,f284,f285,f286,f287,f292");
//        param.put("secid", "1." + stockNum);
//        String sendResult = HttpsUtils.doGetString(url, param);
//
//        JSONObject data = new JSONObject();
//        try {
//            data = JSONObject.parseObject(sendResult).getJSONObject("data");
//        } catch (JSONException e) {
//            log.error("解析jsonb报错：" + data.toJSONString());
//        } catch (NullPointerException e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }

        stockBidBean.setStockNum(stockNum);
        stockBidBean.setSellPrice5(objectToBigDecimal(data.get("A5_P")));
        stockBidBean.setSellCount5(objectToInteger(data.get("A5_V").toString()));
        stockBidBean.setSellPrice4(objectToBigDecimal(data.get("A4_P")));
        stockBidBean.setSellCount4(objectToInteger(data.get("A4_V").toString()));
        stockBidBean.setSellPrice3(objectToBigDecimal(data.get("A3_P")));
        stockBidBean.setSellCount3(objectToInteger(data.get("A3_V").toString()));
        stockBidBean.setSellPrice2(objectToBigDecimal(data.get("A2_P")));
        stockBidBean.setSellCount2(objectToInteger(data.get("A2_V").toString()));
        stockBidBean.setSellPrice1(objectToBigDecimal(data.get("A1_P")));
        stockBidBean.setSellCount1(objectToInteger(data.get("A1_V").toString()));

        stockBidBean.setBoughtPrice1(objectToBigDecimal(data.get("B1_P")));
        stockBidBean.setBoughtCount1(objectToInteger(data.get("B1_V").toString()));
        stockBidBean.setBoughtPrice2(objectToBigDecimal(data.get("B2_P")));
        stockBidBean.setBoughtCount2(objectToInteger(data.get("B2_V").toString()));
        stockBidBean.setBoughtPrice3(objectToBigDecimal(data.get("B3_P")));
        stockBidBean.setBoughtCount3(objectToInteger(data.get("B3_V").toString()));
        stockBidBean.setBoughtPrice4(objectToBigDecimal(data.get("B4_P")));
        stockBidBean.setBoughtCount4(objectToInteger(data.get("B4_V").toString()));
        stockBidBean.setBoughtPrice5(objectToBigDecimal(data.get("B5_P")));
        stockBidBean.setBoughtCount5(objectToInteger(data.get("B5_V").toString()));


        return stockBidBean;
    }


    public Double objectToBigDecimal(Object o) {
        try {
            return Double.valueOf(String.valueOf(o));
        } catch (NumberFormatException e) {
            return Double.valueOf(0);
        }


    }

    public Integer objectToInteger(Object o) {
        if (o == null) {
            return null;
        }
        String str = o.toString();
        if (str.equals("-")) {
            return null;
        }
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }




    }
}
