package com.CarRentalSystem.AgencyService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedSearchResponse {
    private List<AgencySearchResponse> agencies;
    private int currentPage;
    private int totalPages;
    private int totalItems;
    private int pageSize;
}
