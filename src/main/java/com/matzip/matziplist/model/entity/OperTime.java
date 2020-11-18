package com.matzip.matziplist.model.entity;

import com.matzip.matziplist.model.dto.response.OperTimeApiResponse;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"restaurant"})
@Entity
public class OperTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    private String time;

    @ManyToOne
    private Restaurant restaurant;

    public OperTimeApiResponse toResponse() {
        return OperTimeApiResponse.builder()
                .id(id)
                .day(day)
                .time(time)
                .build();
    }
}
