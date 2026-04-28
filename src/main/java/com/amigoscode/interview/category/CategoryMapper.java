package com.amigoscode.interview.category;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryController.CategoryResponse toResponse(Category review);

    Category toEntity(CategoryController.CategoryRequest request);
}