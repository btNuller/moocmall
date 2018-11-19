package com.peactor.moocmall.controller.backend;

import com.peactor.moocmall.common.Const;
import com.peactor.moocmall.common.ResponseCode;
import com.peactor.moocmall.common.ServerResponse;
import com.peactor.moocmall.pojo.Category;
import com.peactor.moocmall.pojo.User;
import com.peactor.moocmall.service.ICategoryService;
import com.peactor.moocmall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICategoryService categoryService;

    @RequestMapping("add_category.do")
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "找不到当前用户");
        }
        if(userService.checkAdminRole(user).isSuccess()) {
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.error("无权限操作需要管理员权限");
        }
    }

    @RequestMapping("set_category_name.do")
    public ServerResponse setCategortName(HttpSession session, int categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "找不到当前用户");
        }
        if(userService.checkAdminRole(user).isSuccess()) {
            //更新categoryname
            return categoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.error("无权限操作需要管理员权限");
        }
    }

    @RequestMapping("get_category.do")
    public ServerResponse<Category> getCategory(HttpSession session, int categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(null == user) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "找不到当前用户");
        }
        if(userService.checkAdminRole(user).isSuccess()) {
            return categoryService.getCategory(categoryId);
        } else {
            return ServerResponse.error("无权限操作需要管理员权限");
        }
    }
}
