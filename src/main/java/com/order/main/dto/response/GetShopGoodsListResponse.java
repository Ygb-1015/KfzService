package com.order.main.dto.response;

import java.util.List;

public class GetShopGoodsListResponse {

    private List<ShopGoods> list;

    // 每页最大条数
    private Integer pageSize;

    // 当前页码
    private Integer pageNum;

    // 总记录数
    private Integer total;

    // 总页数
    private Integer pages;

    // 当前页记录数
    private Integer size;

    public static class ShopGoods {

        // 商品添加时间戳
        private Long addTime;

        // 是否草稿。0：不是草稿；1：是草稿
        private Integer isDraft;

        // 折扣。取值10～100,50表示：5折，75表示：75折
        private Integer discount;

        // 年代对应编号
        private Long years;

        // 库存
        private Integer number;

        // 商品名称
        private String itemName;

        // 商品所在地对应编号
        private Long productArea;

        // 商品价格
        private Long price;

        // 店铺分类编号
        private Long myCatId;

        // 出版社
        private String press;

        // 出运费方。buyer：买家承担运费；seller：卖家包邮
        private String bearShipping;

        // 作者
        private String author;

        // 商品重量
        private Double weight;

        // 出版时间
        private String pubDate;

        // 品相。95表示：九五品
        private Integer quality;

        // 商品编号
        private Long itemId;

        // 分类编号
        private Long catId;

        // 店铺类型。1：书店；2：书摊
        private Integer bizType;

        // 审核状态。notCertified：待审核；certified：审核通过；frozen：冻结；failed：驳回；waitApproved：等待复审
        private String certifyStatus;

        // ISBN书号
        private String isbn;

        // 标准本数量
        private Integer weightPiece;

        // 运费模板编号
        private Long mouldId;

        // 关联图书库编号
        private Long booklibId;

        // 上下架状态
        private Integer isOnSale;

        // 删除状态
        private Integer isDelete;

        // 商品更新时间
        private String updateTime;

        // 商品下架时间戳
        private Long endSaleTime;

        // 卖家用户编号
        private Long userId;

        // 商品图片路径
        private String imgUrl;

        // 商品原价
        private Long oriPrice;

        // 商品货号
        private String itemSn;

        // 商品上架时间戳
        private Long beginSaleTime;

        // 是否全新图书
        private Integer isNewBook;

        public Long getAddTime() {
            return addTime;
        }

        public void setAddTime(Long addTime) {
            this.addTime = addTime;
        }

        public Integer getIsDraft() {
            return isDraft;
        }

        public void setIsDraft(Integer isDraft) {
            this.isDraft = isDraft;
        }

        public Integer getDiscount() {
            return discount;
        }

        public void setDiscount(Integer discount) {
            this.discount = discount;
        }

        public Long getYears() {
            return years;
        }

        public void setYears(Long years) {
            this.years = years;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Long getProductArea() {
            return productArea;
        }

        public void setProductArea(Long productArea) {
            this.productArea = productArea;
        }

        public Long getPrice() {
            return price;
        }

        public void setPrice(Long price) {
            this.price = price;
        }

        public Long getMyCatId() {
            return myCatId;
        }

        public void setMyCatId(Long myCatId) {
            this.myCatId = myCatId;
        }

        public String getPress() {
            return press;
        }

        public void setPress(String press) {
            this.press = press;
        }

        public String getBearShipping() {
            return bearShipping;
        }

        public void setBearShipping(String bearShipping) {
            this.bearShipping = bearShipping;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) {
            this.pubDate = pubDate;
        }

        public Integer getQuality() {
            return quality;
        }

        public void setQuality(Integer quality) {
            this.quality = quality;
        }

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public Long getCatId() {
            return catId;
        }

        public void setCatId(Long catId) {
            this.catId = catId;
        }

        public Integer getBizType() {
            return bizType;
        }

        public void setBizType(Integer bizType) {
            this.bizType = bizType;
        }

        public String getCertifyStatus() {
            return certifyStatus;
        }

        public void setCertifyStatus(String certifyStatus) {
            this.certifyStatus = certifyStatus;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Integer getWeightPiece() {
            return weightPiece;
        }

        public void setWeightPiece(Integer weightPiece) {
            this.weightPiece = weightPiece;
        }

        public Long getMouldId() {
            return mouldId;
        }

        public void setMouldId(Long mouldId) {
            this.mouldId = mouldId;
        }

        public Long getBooklibId() {
            return booklibId;
        }

        public void setBooklibId(Long booklibId) {
            this.booklibId = booklibId;
        }

        public Integer getIsOnSale() {
            return isOnSale;
        }

        public void setIsOnSale(Integer isOnSale) {
            this.isOnSale = isOnSale;
        }

        public Integer getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(Integer isDelete) {
            this.isDelete = isDelete;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Long getEndSaleTime() {
            return endSaleTime;
        }

        public void setEndSaleTime(Long endSaleTime) {
            this.endSaleTime = endSaleTime;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Long getOriPrice() {
            return oriPrice;
        }

        public void setOriPrice(Long oriPrice) {
            this.oriPrice = oriPrice;
        }

        public String getItemSn() {
            return itemSn;
        }

        public void setItemSn(String itemSn) {
            this.itemSn = itemSn;
        }

        public Long getBeginSaleTime() {
            return beginSaleTime;
        }

        public void setBeginSaleTime(Long beginSaleTime) {
            this.beginSaleTime = beginSaleTime;
        }

        public Integer getIsNewBook() {
            return isNewBook;
        }

        public void setIsNewBook(Integer isNewBook) {
            this.isNewBook = isNewBook;
        }
    }

    public List<ShopGoods> getList() {
        return list;
    }

    public void setList(List<ShopGoods> list) {
        this.list = list;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
