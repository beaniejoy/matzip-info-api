package com.matzip.matziplist.model.entity;

import com.matzip.matziplist.model.dto.response.GradeApiResponse;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"restaurant"})
@Entity
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double total;

    private Double taste;

    private Double price;

    private Double service;

    private Integer number;

    @OneToOne
    private Restaurant restaurant;

    public GradeApiResponse toResponse() {
        return GradeApiResponse.builder()
                .id(id)
                .total(total)
                .taste(taste)
                .price(price)
                .service(service)
                .number(number)
                .build();
    }
}
