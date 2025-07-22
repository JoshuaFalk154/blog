package com.blog.blog.config;

import com.blog.blog.entities.Post;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.id.IdentityGenerator;

public class MyEntitySequenceGenerator extends IdentityGenerator implements BeforeExecutionGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object, Object currentValue, EventType eventType) {
        if (!(object instanceof Post myEntity)) {
            throw new RuntimeException("It's not: " + Post.class.getName());
        }
        return myEntity.getId();
    }



    @Override
    public boolean generatedOnExecution() {
        return super.generatedOnExecution();
    }

    @Override
    public boolean generatedOnExecution(Object entity, SharedSessionContractImplementor session) {
        Post myEntity = (Post) entity;
        return myEntity.getId() == null;
    }

    @Override
    public boolean allowAssignedIdentifiers() {
        return true;
    }
}