package com.order.main.dto.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemListVo<T> {

    private List<T> orderItems;

    private List<ExceptionItem> itemList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExceptionItem {

        private String orderExceptionType;

        private List<String> orderItemId;

    }

}
