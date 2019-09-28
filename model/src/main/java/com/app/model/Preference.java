package com.app.model;

import com.app.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data


public class Preference {
    private int priorityNumber;
    private Category category;


}
