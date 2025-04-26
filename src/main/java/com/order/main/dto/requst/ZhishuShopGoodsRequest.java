package com.order.main.dto.requst;

/**
 * 商品信息业务对象 zhishu_shop_goods
 *
 * @author Lion Li
 * @date 2025-03-07
 */
public class ZhishuShopGoodsRequest {

    /**
     * 商品id
     */
    private String id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 产品编码
     */
    private String productId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * isbn
     */
    private String isbn;

    /**
     * 货号
     */
    private String artNo;

    /**
     * 期初库存
     */
    private Long stock;

    /**
     * 标准售价
     */
    private Long price;

    /**
     * 品相
     */
    private Integer conditionCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 商品编号
     */
    private String itemNumber;

    /**
     * 商品定价
     */
    private Long fixPrice;

    /**
     * 库存
     */
    private Integer inventory;

    /**
     * 书图片
     */
    private String bookPic;

    /**
     * 是否已进行货号转换：0-未转换 1-已转换
     */
    private Integer isArtNoConversion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getArtNo() {
        return artNo;
    }

    public void setArtNo(String artNo) {
        this.artNo = artNo;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getConditionCode() {
        return conditionCode;
    }

    public void setConditionCode(Integer conditionCode) {
        this.conditionCode = conditionCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Long getFixPrice() {
        return fixPrice;
    }

    public void setFixPrice(Long fixPrice) {
        this.fixPrice = fixPrice;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getBookPic() {
        return bookPic;
    }

    public void setBookPic(String bookPic) {
        this.bookPic = bookPic;
    }

    public Integer getIsArtNoConversion() {
        return isArtNoConversion;
    }

    public void setIsArtNoConversion(Integer isArtNoConversion) {
        this.isArtNoConversion = isArtNoConversion;
    }
}
