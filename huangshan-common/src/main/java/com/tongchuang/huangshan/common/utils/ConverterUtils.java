package com.tongchuang.huangshan.common.utils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class ConverterUtils {

    private static MapperFactory MAPPER_FACTORY = new DefaultMapperFactory.Builder().build();

    public static <S, T> T getSimpleConverObject(S sourceObject, Class<T> targetClass, Map<String, String> fieldMap) {
        if (CollectionUtils.isEmpty(fieldMap)) {
            MAPPER_FACTORY.classMap(sourceObject.getClass(), targetClass).byDefault();
        } else {
            ClassMapBuilder<?, T> classMapBuilder = MAPPER_FACTORY.classMap(sourceObject.getClass(), targetClass);
            for (String s : fieldMap.keySet()) {
                classMapBuilder.field(s, fieldMap.get(s));
            }
            classMapBuilder.byDefault().register();
        }
        MapperFacade mapper = MAPPER_FACTORY.getMapperFacade();
        T target = mapper.map(sourceObject, targetClass);
        return target;
    }

    public static <S, T> T getSimpleConverObject(S sourceObject, Class<T> targetClass) {
        ClassMapBuilder<?, T> tClassMapBuilder = MAPPER_FACTORY.classMap(sourceObject.getClass(), targetClass);
        tClassMapBuilder.exclude("");
        MapperFacade mapper = MAPPER_FACTORY.getMapperFacade();
        T target = mapper.map(sourceObject, targetClass);
        return target;
    }

    public static <S, T> T getSimpleConverObject(S sourceObject, Class<T> targetClass,
                                                 Set<String> excludeStr, Map<String, String> fieldMap) {
       final ClassMapBuilder<?, T> tClassMapBuilder = MAPPER_FACTORY.classMap(sourceObject.getClass(), targetClass);
        Optional.ofNullable(excludeStr).orElseGet(HashSet::new).stream().forEach(s->tClassMapBuilder.exclude(s));
        Optional.ofNullable(fieldMap).orElseGet(HashMap::new).keySet().stream()
                .forEach(m->tClassMapBuilder.field(m, fieldMap.get(m)));
        tClassMapBuilder.mapNulls(false).byDefault().register();
        return MAPPER_FACTORY.getMapperFacade().map(sourceObject, targetClass);
    }

    public static <S, T> T getSimpleConverObject(S sourceObject, Class<T> targetClass, Set<String> excludeStr) {
        ClassMapBuilder<?, T> tClassMapBuilder = MAPPER_FACTORY.classMap(sourceObject.getClass(), targetClass);
        if (!CollectionUtils.isEmpty(excludeStr)) {
            for (String s : excludeStr) {
                tClassMapBuilder = tClassMapBuilder.exclude(s);
            }
        }
        tClassMapBuilder.byDefault().register();
        MapperFacade mapper = MAPPER_FACTORY.getMapperFacade();
        T target = mapper.map(sourceObject, targetClass);
        return target;
    }

    public static <S, T> List<T> getConverObjectList(List<S> sourceObjectList, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(sourceObjectList)) {
            return null;
        }
        MAPPER_FACTORY.classMap(sourceObjectList.get(0).getClass(), targetClass).byDefault();
        MapperFacade mapper = MAPPER_FACTORY.getMapperFacade();
        return sourceObjectList.stream().map(s -> mapper.map(s, targetClass)).collect(Collectors.toList());

    }

    public static <S, T> List<T> getConverObjectList(List<S> sourceObjectList, Class<T> targetClass, Map<String, String> fieldMap) {
        if (CollectionUtils.isEmpty(sourceObjectList)) {
            return null;
        }
        if (CollectionUtils.isEmpty(fieldMap)) {
            MAPPER_FACTORY.classMap(sourceObjectList.get(0).getClass(), targetClass).byDefault();
        } else {
            ClassMapBuilder<?, T> classMapBuilder = MAPPER_FACTORY.classMap(sourceObjectList.get(0).getClass(), targetClass);
            for (String s : fieldMap.keySet()) {
                classMapBuilder.field(s, fieldMap.get(s));
            }
            classMapBuilder.byDefault().register();
        }
        MapperFacade mapper = MAPPER_FACTORY.getMapperFacade();
        return sourceObjectList.stream().map(s -> mapper.map(s, targetClass)).collect(Collectors.toList());
    }
}
