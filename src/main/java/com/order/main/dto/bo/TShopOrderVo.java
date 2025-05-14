package com.order.main.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 订单视图对象 t_shop_order
 *
 * @author Lion Li
 * @date 2025-03-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TShopOrderVo implements Serializable {

    private Long id;

    /**
     * 订单来源类型 1-拼多多 2-孔夫子
     */
    private Integer orderSourceType;

    /**
     * 订单异常类型集合（json字符串）：inventoryException-库存异常 goodsSourceUnknownException-未知商品来源
     */
    private String orderExceptionType;

    /**
     * 店铺id
     */
    private String shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 地址
     */
    private String address;

    /**
     * 详细地址
     */
    private String addressMask;

    /**
     * 售后状态 0：无售后 2：买家申请退款，待商家处理 3：退货退款，待商家处理 4：商家同意退款，退款中 5：平台同意退款，退款中 6：驳回退款，待买家处理 7：已同意退货退款,待用户发货 8：平台处理中 9：平台拒绝退款，退款关闭 10：退款成功 11：买家撤销 12：买家逾期未处理，退款失败 13：买家逾期，超过有效期 14：换货补寄待商家处理 15：换货补寄待用户处理 16：换货补寄成功 17：换货补寄失败 18：换货补寄待用户确认完成 21：待商家同意维修 22：待用户确认发货 24：维修关闭 25：维修成功 27：待用户确认收货 31：已同意拒收退款，待用户拒收 32：补寄待商家发货 33：同意召回后退款，待商家召回
     */
    private Integer afterSalesStatus;

    /**
     * 买家留言信息
     */
    private String buyerMemo;

    /**
     * 成交状态：0：未成交、1：已成交、2：已取消
     */
    private Integer confirmStatus;

    /**
     * 成交时间
     */
    private String confirmTime;

    /**
     * 订单创建时间
     */
    private String createdTime;

    /**
     * 是否当日发货，1-是，0-否
     */
    private String deliveryOneDay;

    /**
     * 折扣金额，单位：元，折扣金额=平台优惠+商家优惠+团长免单优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 多多支付立减金额，单位：元
     */
    private BigDecimal duoDuoPayReduction;

    /**
     * 是否多多批发
     */
    private Integer duoduoWholesale;

    /**
     * 商品金额
     */
    private BigDecimal goodsAmount;

    /**
     * 仓库编码
     */
    private String depotCode;

    /**
     * 仓库编码
     */
    private String orderDepotInfo;

    /**
     * 仓库id
     */
    private String depotId;

    /**
     * 支付申报订单号
     */
    private String depotName;

    /**
     * 仓库类型，1：自有仓 2：订阅仓 两者都不是则传空
     */
    private Integer depotType;

    /**
     * 货品id
     */
    private Long wareId;

    /**
     * 货品名称
     */
    private String wareName;

    /**
     * 货品编码
     */
    private String wareSn;

    /**
     * 货品类型（0：普通货品:1：组合货品）
     */
    private Integer wareType;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付单号
     */
    private String payNo;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 平台优惠金额
     */
    private BigDecimal platformDiscount;

    /**
     * 邮费
     */
    private BigDecimal postage;

    /**
     * 预售时间
     */
    private String preSaleTime;

    /**
     * 承诺送达时间
     */
    private String promiseDeliveryTime;

    /**
     * 确认收货时间
     */
    private String receiveTime;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份编码
     */
    private String provinceId;

    /**
     * 市
     */
    private String city;

    /**
     * 市编码
     */
    private String cityId;

    /**
     * 区，乡镇
     */
    private String town;

    /**
     * 区县编码
     */
    private String townId;

    /**
     * 收件人地址
     */
    private String receiverAddress;

    /**
     * 收件人地址
     */
    private String receiverAddressMask;

    /**
     * 收件人姓名
     */
    private String receiverName;

    /**
     * 收件人姓名
     */
    private String receiverNameMask;

    /**
     * 收件人手机号
     */
    private String receiverPhone;

    /**
     * 收件人手机号（打码）
     */
    private String receiverPhoneMask;

    /**
     * 退款状态，枚举值：1：无售后或售后关闭，2：售后处理中，3：退款中，4： 退款成功
     */
    private Integer refundStatus;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 1-红色，2-黄色，3-绿色，4-蓝色，5-紫色
     */
    private String remarkTag;

    /**
     * 订单备注标记名称
     */
    private String remarkTagName;

    /**
     * 退货包运费，1:是，0:否
     */
    private Integer returnFreightPayer;

    /**
     * 订单审核状态（0-正常订单， 1-审核中订单）
     */
    private Integer riskControlStatus;

    /**
     * 是否门店自提
     */
    private Integer selfContained;

    /**
     * 商家优惠金额
     */
    private BigDecimal sellerDiscount;

    /**
     * 缺货处理状态 -1:无缺货处理 0: 缺货待处理 1缺货已处理
     */
    private Integer stockOutHandleStatus;

    /**
     * 全国联保，1:是，0:否
     */
    private Integer supportNationwideWarranty;

    /**
     * 快递单号
     */
    private String trackingNumber;

    /**
     * 以旧换新国家补贴金额，单位：元
     */
    private BigDecimal tradeInNationalSubsidyAmount;

    /**
     * 订单类型 0-普通订单 ，1- 定金订单
     */
    private Integer tradeType;

    /**
     * 订单的更新时间
     */
    private String updatedAt;

    /**
     * 催发货时间
     */
    private String urgeShippingTime;

    /**
     * 预约配送日期
     */
    private String yypsDate;

    /**
     * 预约配送时段
     */
    private String yypsTime;

    /**
     * 合单ID2
     */
    private String openAddressId2;

    /**
     * 店铺状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 租户编码
     */
    private String tenantId;

    /**
     * 商品列表
     */
    private String itemList;

    /**
     * 额外附属信息
     */
    private String extraInfo;

}
