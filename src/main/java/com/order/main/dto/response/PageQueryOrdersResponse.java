package com.order.main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageQueryOrdersResponse {

    private Integer total;       // 总条数

    private Integer pages;       // 总页数

    private Integer size;        // 当前页条数

    private Integer pageSize;    // 每页最大条数

    private Integer pageNum;     // 当前页码

    private List<Order> list;    // 订单列表

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {

        private Long orderId;                // 订单编号

        private String createdTime;          // 订单生成时间

        private Long shopId;                 // 店铺编号

        private String shopName;             // 店铺名称

        private Long shopkeeperId;           // 卖家用户编号

        private String shipmentNum;          // 快递单号

        private String shippingCom;          // 快递公司编码

        private String shippingComName;      // 快递公司名称

        private String shippingId;           // 配送方式

        private String shippingName;         // 配送方式名称

        private String shippingFee;          // 快递费

        private String goodsAmount;          // 订单商品金额

        private String favorableMoney;       // 商品优惠金额

        private String orderAmount;          // 订单金额

        private Long userId;                 // 买家用户编号

        private String nickname;             // 买家昵称

        private String receiver;             // 收件人信息（字符串格式）

        private ReceiverInfo receiverInfo;   // 收件人详细信息对象

        private Integer sellerFlagType;      // 备注类型, 0:未打标记,1:红,2:黄,3:绿,4:蓝,5:紫

        private String sellerRemarkText;     // 卖家备注信息

        private String buyerRemark;          // 买家备注信息

        private String orderStatus;          // 订单状态（枚举值）

        private String orderStatusName;      // 订单状态中文名称

        private Long promotionId;            // 活动编号

        private Integer itemsCount;          // 订单商品数量

        private String payTime;              // 支付时间

        private String sellerConfirmedTime;  // 商家确认时间

        private String shippingTime;         // 运输时间

        private String finishTime;           // 完成时间

        private List<Item> items;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Item {

            private Long itemId;               // 商品编号

            private String itemSn;             // 商品货号

            private Integer number;            // 购买数量

            private String itemName;           // 商品名称

            private String img;                // 商品图片URL

            private Boolean isCancel;          // 是否被取消

            private Long orderId;              // 所属订单编号

            private String price;              // 商品价格

            private String favorableAmount;    // 商品总优惠金额

            private String setFavAmount;       // 卖家设置优惠金额

            private String couponFavAmount;    // 优惠券优惠金额

            private String realAmount;         // 商品实付金额

            private String cancelMan;          // 取消者：unkown/seller/buyer

            private String quality;            // 商品品相

            private String isbn;               // 图书ISBN

        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ReceiverInfo {

            private String area;         // 区域编码（如："24006002000"）

            private String zipCode;      // 邮政编码

            private String provName;     // 省份名称（如："山东省"）

            private String address;      // 详细地址（如："顺兴路152号404户"）

            private String cityName;     // 城市名称（如："青岛市"）

            private String areaName;     // 区名（如："市北区"）

            private String receiverName; // 收件人姓名

            private String mobile;       // 手机号码

            private String phoneNum;     // 固定电话

        }

    }

}
