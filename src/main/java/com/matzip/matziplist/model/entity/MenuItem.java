package com.matzip.matziplist.model.entity;

import com.matzip.matziplist.model.dto.response.MenuItemApiResponse;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"restaurant"})
@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String price;

    @ManyToOne
    private Restaurant restaurant;

    public MenuItemApiResponse toResponse() {
        return MenuItemApiResponse.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }
}
