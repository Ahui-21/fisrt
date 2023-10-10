package com.bjh.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 出库单
    */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutStore implements Serializable {
    private Integer outsId;

    private Integer productId;

    private Integer storeId;

    private Integer tallyId;

    private BigDecimal outPrice;

    private Integer outNum;

    private Integer createBy;

    private Date createTime;

    /**
    * 0 否 1 是
    */
    private String isOut;

    private static final long serialVersionUID = 1L;

    //·············追加的属性···············
    private  BigDecimal salePrice;//接收商品的售价
    //------------------追加的属性-------------------------

    private String productName;//商品名称

    private String startTime;//起始时间

    private String endTime;//结束时间

    private String storeName;//仓库名称

    private String userCode;//创建出库单的用户的名称




}