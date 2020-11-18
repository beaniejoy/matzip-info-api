package com.matzip.matziplist.model.dto.request;

import com.matzip.matziplist.model.entity.Restaurant;
import com.matzip.matziplist.model.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagApiRequest {

    private String name;

    public Tag toEntity(Restaurant restaurant) {
        return Tag.builder()
                .name(name)
                .restaurant(restaurant)
                .build();
    }
}
