package com.order.main.dto.requst;

import java.math.BigDecimal;

public class GoodsItemAddRequest {


    private String token;

    /**
     *  模板编号。模板编号取决于商品所在的分类，取值范围：1～17
     */
    private String tpl;

    /**
     *  分类编号。取值范围参考接口：kongfz.common.category
     */
    private String catId;

    /**
     *  本店分类。取值范围参考接口：kongfz.shop.category.name.list
     */
    private String myCatId;

    /**
     *  商品名称。长度限制：200字符
     */
    private String itemName;

    /**
     *  推荐语。长度限制：200字符
     */
    private String importantDesc;

    /**
     * 售价。取值范围：0.01～99999999.99
     */
    private String price;

    /**
     * 库存。取值范围：1~9999
     */
    private String number;

    /**
     * 品相。可以是编号，也可以是编号对应的文字。取值范围：10,20,30,40,50,60,65,70,75,80,85,90,95,100;文字参考接口：kongfz.common.quality
     */
    private String quality;

    /**
     *  品相描述。长度限制：400字符
     */
    private String qualityDesc;

    /**
     *  货号。长度限制：20字符
     */
    private String itemSn;

    /**
     *  商品主图。可被访问的图片地址，或是孔网商品图片的相对路径。
     */
    private String imgUrl;

    /**
     *  多个商品图片路径，图片路径之间用英文";"隔开；此处只能是孔网商品图片的（相对）路径；可以包含主图图片地址（但一定要是孔网商品图片）；最多支持30张图片地址（若不包含主图地址，最多支持29张图片地址，若包含主图地址，最多支持30张图片地址）
     */
    private String images;

    /**
     *  商品描述。长度限制：10000字符
     */
    private String itemDesc;

    /**
     *  运费设置。取值范围：seller(卖家包邮)，buyer(买家承担运费)
     */
    private String bearShipping;

    /**
     *  运费模板编号。bearShipping=buyer时必填。取值范围参考接口：kongfz.delivery.template.name.list 或 kongfz.delivery.template.simple.list
     */
    private Long mouldId;

    /**
     *  商品重量。当选择的是按重量的运费模板时必填。取值范围：0.01～9999.99，单位：千克
     */
    private BigDecimal weight;

    /**
     *  商品标准本数。当选择的是按标准本的运费模板时必填。取值范围：0.01～9999.99
     */
    private BigDecimal weightPiece;

    /**
     * isbn
     */
    private String isbn;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String press;

    /**
     * 出版时间
     */
    private String pubDate;

    /**
     * 装帧
     */
    private String binding;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTpl() {
        return tpl;
    }

    public void setTpl(String tpl) {
        this.tpl = tpl;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getMyCatId() {
        return myCatId;
    }

    public void setMyCatId(String myCatId) {
        this.myCatId = myCatId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImportantDesc() {
        return importantDesc;
    }

    public void setImportantDesc(String importantDesc) {
        this.importantDesc = importantDesc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getQualityDesc() {
        return qualityDesc;
    }

    public void setQualityDesc(String qualityDesc) {
        this.qualityDesc = qualityDesc;
    }

    public String getItemSn() {
        return itemSn;
    }

    public void setItemSn(String itemSn) {
        this.itemSn = itemSn;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getBearShipping() {
        return bearShipping;
    }

    public void setBearShipping(String bearShipping) {
        this.bearShipping = bearShipping;
    }

    public Long getMouldId() {
        return mouldId;
    }

    public void setMouldId(Long mouldId) {
        this.mouldId = mouldId;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getWeightPiece() {
        return weightPiece;
    }

    public void setWeightPiece(BigDecimal weightPiece) {
        this.weightPiece = weightPiece;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }
}
