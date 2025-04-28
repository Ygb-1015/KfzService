package com.order.main.dto.response;

public class ItemItemsnUpdateResponse {

    // 详细信息
    private Item item;

    public static class Item {

        // 商品ID
        private Long itemId;

        // 货号
        private String itemSn;

        // 更新时间
        private String updateTime;

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getItemSn() {
            return itemSn;
        }

        public void setItemSn(String itemSn) {
            this.itemSn = itemSn;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
