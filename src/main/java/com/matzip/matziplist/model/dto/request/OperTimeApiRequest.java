package com.matzip.matziplist.model.dto.request;

import com.matzip.matziplist.model.entity.OperTime;
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
public class OperTimeApiRequest {

    private String day;

    private String time;

    public OperTime toEntity(Restaurant restaurant) {
        return OperTime.builder()
                .day(day)
                .time(time)
                .restaurant(restaurant)
                .build();
    }
}
