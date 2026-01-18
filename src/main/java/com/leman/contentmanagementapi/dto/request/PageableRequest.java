package com.leman.contentmanagementapi.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {

    @Min(0)
    private Integer page;

    @Min(1)
    @Max(50)
    private Integer size;

    private String sortBy;
    private Sort.Direction sortDirection;

}
