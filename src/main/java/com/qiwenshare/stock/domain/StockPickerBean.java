package com.qiwenshare.stock.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "stockpicker")
@TableName("stockpicker")
public class StockPickerBean {
  @Id
  @TableId(type = IdType.AUTO)
  private Long stockId;
  @Column
  private String stockNum;
  @Column
  private String stockName;
  @Column
  private LocalDate highestDate;
  @Column
  private LocalDate lowestDate;
  @Column
  private LocalDate subLowestDateB;
  @Column
  private LocalDate subLowestDateC;
  @Column
  private int countSubLow;

}

