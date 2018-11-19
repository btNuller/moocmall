package com.peactor.moocmall.service;

import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.pojo.Category;

public interface ICategoryService {

    ServerResponse addCategory(String categoryName, int parentId);

    ServerResponse updateCategoryName(int categoryId, String categoryName);

    ServerResponse<Category> getCategory(int categoryId);
}
