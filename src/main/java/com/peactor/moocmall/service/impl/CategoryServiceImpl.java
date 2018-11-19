package com.peactor.moocmall.service.impl;

import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.dao.CategoryMapper;
import com.peactor.moocmall.pojo.Category;
import com.peactor.moocmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse addCategory(String categoryName, int parentId) {
        if(StringUtils.isBlank(categoryName)) {
            return ServerResponse.error("创建品类错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setPid(parentId);
        category.setStatus(true);
        int rowCount = categoryMapper.insertSelective(category);
        if(0 < rowCount) {
            return ServerResponse.success("添加成功");
        }
        return ServerResponse.error("创建品类失败");
    }

    public ServerResponse updateCategoryName(int categoryId, String categoryName) {
        if(StringUtils.isBlank(categoryName)) {
            return ServerResponse.error("更新失败,参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(0 < rowCount) {
            return ServerResponse.success("更新品类成功");
        } else {
            return ServerResponse.error("更新品类名称失败");
        }
    }

    @Override
    public ServerResponse<Category> getCategory(int categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(null != category) {
            return ServerResponse.success("ok", category);
        } else {
            return ServerResponse.error("fail");
        }
    }
}
