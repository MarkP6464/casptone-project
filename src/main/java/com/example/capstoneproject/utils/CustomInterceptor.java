package com.example.capstoneproject.utils;

import com.example.capstoneproject.entity.Users;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class CustomInterceptor extends EmptyInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CustomInterceptor.class);

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (entity instanceof Users) {
            logger.debug(entity.toString());
        }
        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object [] previousState, String[] propertyNames, Type[] types) {
        if (entity instanceof Users) {
            logger.debug(entity.toString());
        }
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}