package com.matzip.matziplist.model.dto.request;

import com.matzip.matziplist.model.entity.MenuItem;
import com.matzip.matziplist.model.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemApiRequest {

    private String name;

    private String price;

    public MenuItem toEntity(Restaurant restaurant) {
        return MenuItem.builder()
                .name(name)
                .price(price)
                .restaurant(restaurant)
                .build();
    }
}
