/*
 * Copyright(C) 2014 tAsktoys. All rights reserved.
 */
package com.tasktoys.archelon.data.dao.jdbc;

import com.tasktoys.archelon.data.dao.DiscussionDao;
import com.tasktoys.archelon.data.entity.Discussion;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author YuichiroSato
 */
@Repository
public class JdbcDiscussionDao implements DiscussionDao {

    private JdbcTemplate jdbcTemplate;
    private static final String TABLE_NAME = "discussion";
    
    /**
     * Set data source. It invoke from Spring Framework.
     *
     * @param dataSource data source
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    public enum Column {
        ID, AUTHOR_ID, CATEGORY_ID, STATE, CREATE_TIME, UPDATE_TIME, SUBJECT,
        PARTICIPANTS, POSTS;
        
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    @Override
    public List<Discussion> findNewestDiscussionList(int n) {
        String sql = "select * from " + TABLE_NAME
                + " order by " + Column.CREATE_TIME.toString() + " desc"
                + " limit " + n + ";";
        return responseToDiscussionList(jdbcTemplate.queryForList(sql));
    }

    @Override
    public List<Discussion> findDiscussionListAfter(long id, int n) {
        String sql = "select * from " + TABLE_NAME
                + " where " + Column.ID + " > " + Long.toString(id)
                + " order by " + Column.CREATE_TIME.toString() + " desc"
                + " limit " + n + ";";
        return responseToDiscussionList(jdbcTemplate.queryForList(sql));
    }
    
    @Override
    public List<Discussion> findDiscussionListBefore(long id, int n) {
        String sql = "select * from " + TABLE_NAME
                + " where " + Column.ID + " < " + Long.toString(id)
                + " order by " + Column.CREATE_TIME.toString() + " desc"
                + " limit " + n + ";";
        return responseToDiscussionList(jdbcTemplate.queryForList(sql));
    }

    @Override
    public List<Discussion> findNewestDiscussionListByMainCategory(int n, int main_id) {
        String sql = "select * from " + TABLE_NAME
                + " where " + Column.CATEGORY_ID.toString() + " = " + main_id
                + " order by " + Column.CREATE_TIME.toString() + " desc"
                + " limit " + n + ";";
        return responseToDiscussionList(jdbcTemplate.queryForList(sql));
    }

    @Override
    public List<Discussion> findDiscussionListWithMainCategoryBefore(long id, int n, int main_id) {
        String sql = "select * from " + TABLE_NAME
                + " where " + Column.CATEGORY_ID.toString() + " = " + main_id
                + " and " + Column.ID + " < " + Long.toString(id)
                + " order by " + Column.CREATE_TIME.toString() + " desc"
                + " limit " + n + ";";
        return responseToDiscussionList(jdbcTemplate.queryForList(sql));
    }

    @Override
    public List<Discussion> findNewestDiscussionListBySubCategory(int n, int main_id, int sub_id) {
        String sql = "select * from " + TABLE_NAME
                + " where " + Column.CATEGORY_ID.toString() + " = " + main_id
                + " order by " + Column.CREATE_TIME.toString() + " desc"
                + " limit " + n + ";";
        return responseToDiscussionList(jdbcTemplate.queryForList(sql));
    }

    @Override
    public List<Discussion> findDiscussionListWithSubCategoryBefore(long id, int n, int main_id, int sub_id) {
        String sql = "select * from " + TABLE_NAME
                + " where " + Column.CATEGORY_ID.toString() + " = " + main_id
                + " and " + Column.ID + " < " + Long.toString(id)
                + " order by " + Column.CREATE_TIME.toString() + " desc"
                + " limit " + n + ";";
        return responseToDiscussionList(jdbcTemplate.queryForList(sql));
    }
    
    @Override
    public void insertDiscussion(Discussion discussion) {
        String sql = "insert into " + TABLE_NAME + " " +SQLEncoder.toValues(discussion) + ";";
        jdbcTemplate.execute(sql);
    }
    
    private List<Discussion> responseToDiscussionList(List<Map<String, Object>> response) {
        List<Discussion> dls = new ArrayList<>();
        for (Map<String, Object> row : response) {
            Discussion.Builder builder = new Discussion.Builder();
            builder.id((long)row.get(Column.ID.toString()));
            builder.author_id((long)row.get(Column.AUTHOR_ID.toString()));
            builder.category_id((int)row.get(Column.CATEGORY_ID.toString()));
            builder.state((int)row.get(Column.STATE.toString()));
            builder.create_time((Timestamp)row.get(Column.CREATE_TIME.toString()));
            builder.update_time((Timestamp)row.get(Column.UPDATE_TIME.toString()));
            builder.subject((String)row.get(Column.SUBJECT.toString()));
            builder.participants((int)row.get(Column.PARTICIPANTS.toString()));
            builder.posts((int)row.get(Column.POSTS.toString()));
            dls.add(builder.build());
        }
        return dls;
    }
    
    /**
     * Encode <code>Discussion</code> to SQL for insert
     */
    private static class SQLEncoder {
        
        public static String toValues(Discussion discussion) {
            String values = "values(";
            values += encodeID(discussion.getID());
            values += "," + encodeID(discussion.getAuthorID());
            values += "," + encodeInt(discussion.category_id(), Discussion.Builder.ILLEGAL_CATEGORY);
            values += "," + encodeState(discussion.state());
            values += "," + encodeTimestamp(discussion.create_time());
            values += "," + encodeTimestamp(discussion.update_time());
            values += "," + encodeString(discussion.subject());
            values += "," + encodeInt(discussion.participants(), Discussion.Builder.ILLEGAL_PARTICIPANTS);
            values += "," + encodeInt(discussion.posts(), Discussion.Builder.ILLEGAL_POSTS) + ")";
            return values;
        }
        
        private static String encodeID(long id) {
            if (id == Discussion.Builder.ILLEGAL_ID)
                return "null";
            else
                return String.valueOf(id);
        }
        
        private static String encodeInt(int i, int illegal_value) {
            if (i == illegal_value)
                return "null";
            else
                return String.valueOf(i);
        }
        
        private static String encodeState(Discussion.State state) {
            if (state == null)
                return "null";
            else
                return String.valueOf(state.ordinal());
        }
        
        private static String encodeString(String str) {
            if (str == null)
                return "null";
            else
                return "'" + str + "'";
        }
        
        private static String encodeTimestamp(Timestamp timestamp) {
            if (timestamp == null)
                return "null";
            else 
                return "'" + timestamp.toString() + "'";                
        }
    }
}
