package com.order.main.dto.requst;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SoldOutRequest {

    @NotNull(message = "token不能为空")
    private String token;

    @NotNull(message = "itemId不能为空")
    private String itemId;

}
