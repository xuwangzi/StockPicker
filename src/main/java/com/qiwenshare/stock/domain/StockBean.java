package com.qiwenshare.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data // Lombok库提供的一个注解。使用此注解后，编译器会自动生成该类的getter、setter、equals、hashCode和toString方法，从而简化代码。
@Entity // 这是JPA（Java Persistence API）的一个注解。标记一个类为实体类，表示这个类是一个数据库中的表。
        // 当使用JPA或Hibernate这样的框架时，你不需要编写任何SQL来映射实体类到数据库表，框架会自动为你处理。
// JPA的一个注解。指定实体类对应的数据库表名。在这个例子中，数据库中的表名是"stock"。
@Table(name = "stock")
@TableName("stock")
public class StockBean {
    @Id // 表明stockId字段是数据库表的主键。
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 表示主键值是由数据库自动生成的。
    // GenerationType.IDENTITY表示使用数据库的自增策略来生成主键值。
    @TableId(type = IdType.AUTO)    // MyBatisPlus的@TableId注解，实现自增序列id自动插入
    private Long stockId;
    @Column
    private String stockNum;
    @Column
    private String stockName;
    /**
     * 表名，查询用，非实体属性
     * 在Java的JPA（Java Persistence API）上下文中，使用@Transient注解表明该属性不会持久化到数据库中（只想在内存中保留该信息）
     */
    @Transient
    private String stockTableName;
//    //公司代码，解析json用
//    private String COMPANY_CODE;
//    //公司简称，解析json用
//    private String COMPANY_ABBR;
//    //上市日期
//    @Column
//    private String LISTING_DATE;

    //流通股本
    @Column
    private double totalFlowShares;
    //总股本
    @Column
    private double totalShares;

    /**
     * 涨跌幅
     */
    @Column(precision = 5, scale = 4)
    private double upDownRange;

    @Column(precision = 5, scale = 4)
    private double upDownRange3;

    @Column(precision = 5, scale = 4)
    private double upDownRange5;

    /**
     * 换手率
     */
    @Column(precision = 5, scale = 4)
    private double turnOverrate;

    /**
     * 涨跌额
     */
    @Column(precision = 10, scale = 3)
    private double upDownPrices;

    @Column(precision = 10, scale = 3)
    private double open;

    @Column(precision = 10, scale = 3)
    private double close;

    @Column(precision = 10, scale = 3)
    private double high;

    @Column(precision = 10, scale = 3)
    private double low;

    @Column(precision = 10, scale = 3)
    private double preClose;

    @Column(precision = 15)
    private double volume;

    @Column(precision = 15)
    private double amount;

    /**
     * 振幅
     */
    @Column(precision = 5, scale = 4)
    private double amplitude;

    @Column(precision = 15)
    private double totalMarketValue;

    @Column(precision = 15)
    private double flowMarketValue;

    private String updateDate;
    private String listingDate;



    @Override
    public boolean equals(Object obj) {
        // 两个对象是同一个时，直接return true
        if (this == obj) {
            return true;
        }
        if (obj instanceof StockBean) {
            // 比较对象也是StockBean对象时，判断 股票名称 和 股票代码 是否都相同
            StockBean p = (StockBean) obj;
            return stockNum.equals(p.stockNum) && stockName.equals(p.stockName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


}
