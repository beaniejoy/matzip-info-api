package com.matzip.matziplist.model.dto.request;

import com.matzip.matziplist.model.entity.Grade;
import com.matzip.matziplist.model.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GradeApiRequest {

    private Double total;

    // json 객체에서 연속으로 두 번 {} 객체로 묶인 데이터는
    // 해당 내용에 camel case가 적용 안된다.
    private Double taste;

    private Double price;

    private Double service;

    private Integer number;

    public Grade toEntity(Restaurant restaurant) {
        return Grade.builder()
                .total(total)
                .taste(taste)
                .price(price)
                .service(service)
                .number(number)
                .restaurant(restaurant)
                .build();
    }
}
