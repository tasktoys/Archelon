/*
 * Copyright(C) 2014 tAsktoys. All rights reserved.
 */
package com.tasktoys.archelon.data.repository;

import com.tasktoys.archelon.data.entity.DiscussionContent;
import org.springframework.data.repository.CrudRepository;

/**
 * Interface of discussion context. 
 * 
 * <p>The implementation of this interface is auto-generated by Spring Data MongoDB.</p>
 * 
 * @author mikan
 */
public interface DiscussionContentRepository extends CrudRepository<DiscussionContent, Long> {
    
    public DiscussionContent findByDiscussionId(long id);
}
