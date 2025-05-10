package com.order.main.dto.requst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryOrdersRequest {

    // token
    private String token;

    // 用户类型。取值 seller: 卖家；buyer: 买家。
    private String userType;

    // 订单状态。默认为全部订单。
    private String orderStatus;

    // 页码。默认为：1
    private Integer pageNum;

    // 每页最大条数。默认为：20，最大值为：100
    private Integer pageSize;

    // 是否已删除。默认查询未删除的
    private Integer isDelete;

    // 根据订单生成时间查询的起始日期，格式为yyyy-mm-dd，时区为GMT+8，例如：2020-05-26
    private String startDate;

    // 根据订单生成时间查询的截止日期，格式为yyyy-mm-dd，时区为GMT+8，例如：2020-05-27
    private String endDate;

    // 根据订单生成时间查询的起始时间，格式为yyyy-mm-dd hh:mm:ss，时区为GMT+8，例如：2020-05-26 12:00:00
    private String startTime;

    // 根据订单生成时间查询的截止时间，格式为yyyy-mm-dd hh:mm:ss，时区为GMT+8，例如：2020-05-27 12:00:00
    private String endTime;

    // 根据订单更新时间查询的起始时间，格式为yyyy-mm-dd hh:mm:ss，时区为GMT+8，例如：2020-05-26 12:00:00
    private String startUpdateTime;

    // 根据订单更新时间查询的截止时间，格式为yyyy-mm-dd hh:mm:ss，时区为GMT+8，例如：2020-05-27 12:00:00
    private String endUpdateTime;

}
