/*
 * Copyright(C) 2014 tAsktoys Project. All rights reserved.
 */
package com.tasktoys.archelon.data.dao;

import com.tasktoys.archelon.data.entity.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
/**
 *
 * @author Yuichiro
 */
@Repository
public class CategoryDao {
    
    private JdbcTemplate jdbcTemplate;
    
    /**
     * Set data source. It invoke from Spring Framework.
     *
     * @param dataSource data source
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    private enum Table {
        category_main, category_sub, discussion
    }
    
    public Category findMainCategoryByID(int id) {
        String sql = "select * from " + Table.category_main.name() + " where id=?";
        
        return null;
    }
    
    public List<Category> findMainCategoryList() {
        String sql = "select * from " + Table.category_main.name() + ";";
        return responseToCategoryList(jdbcTemplate.queryForList(sql));
    }
    
    public List<Category> findSubCategoryList(int selected_id) {
        String sql = "select * from " + Table.category_sub.name() + " where main_id=" + selected_id;
        return responseToCategoryList(jdbcTemplate.queryForList(sql));
    }
    
    private List<Category> responseToCategoryList(List<Map<String, Object>> response) {
        List<Category> list = new ArrayList<>();
        for (Map<String, Object> map : response) {
            Integer id = (Integer)map.get("id");
            String name = (String)map.get("name");
            if (id != null && name != null)
                list.add(new Category(id, name));
        }
        return list;
    }
}