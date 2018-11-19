2018.11.14
项目结构初始化,数据结构初始化,完成订单系统

## 品类接口

### 1. 获取品类子节点(平级)

/manage/category/get_category.do

#### REQUEST
categoryId

### 2. 增加节点

/manage/category/add_category.do
#### REQUEST
categoryName,parentId

### 3. 修改品类名称
/manage/category/set_category_name.do/

#### REQUEST
categoryId
categoryName

### 4. 获取当前ID下面的所有节点
/manage/category/get_deep_category.do

#### REQUEST
categoryId
