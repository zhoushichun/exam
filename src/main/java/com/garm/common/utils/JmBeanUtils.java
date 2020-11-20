package com.garm.common.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xq
 * @title JmBeanUtils
 * @description Entity 数据库真实对应的字段
 * @date 2020/4/15 10:20
 */
public class JmBeanUtils<Dto, Entity> {

    /**
     * dto 转换为Entity 工具类
     */
    public static <Entity> Entity dtoToEntity(Object dto, Class<Entity> entityClass) {
        // 判断dto是否为空!
        if (dto == null) {
            return null;
        }
        // 判断EntityClass 是否为空
        if (entityClass == null) {
            return null;
        }
        try {
            Entity newInstance = entityClass.newInstance();
            BeanUtils.copyProperties(newInstance, dto);
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * entity转换为Dto工具类
     */
    public static <Dto> Dto entityToDto(Object entity, Class<Dto> dtoClass) {
        // 判断dto是否为空!
        if (entity == null) {
            return null;
        }
        // 判断EntityClass 是否为空
        if (dtoClass == null) {
            return null;
        }
        try {
            Dto newInstance = dtoClass.newInstance();
            BeanUtils.copyProperties(newInstance, entity);
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * List<Entity>转换为List<Dto>工具类
     */
    public static <Dto> List<Dto> entityToDtoList(List<?> entity, Class<Dto> dtoClass) {
        // 判断dto是否为空!
        if (entity == null) {
            return null;
        }
        // 判断EntityClass 是否为空
        if (dtoClass == null) {
            return null;
        }
        try {
            List<Dto> objects = new ArrayList<>();
            for (Object object : entity) {
                Dto newInstance = dtoClass.newInstance();
                BeanUtils.copyProperties(newInstance, object);
                objects.add(newInstance);
            }
            return objects;
        } catch (Exception e) {
            return null;
        }
    }
}
