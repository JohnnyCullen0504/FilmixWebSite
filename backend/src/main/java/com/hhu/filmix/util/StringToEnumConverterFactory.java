package com.hhu.filmix.util;

import com.google.common.collect.Maps;
import com.hhu.filmix.enumeration.BaseEnum;
import jakarta.annotation.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class StringToEnumConverterFactory implements ConverterFactory<String, BaseEnum<String>> {
    private static final Map<Class, Converter> CONVERTERS = Maps.newHashMap();

    /**
     * Get the converter to convert from S to target type T, where T is also an instance of R.
     *
     * @param targetType the target type to convert to
     * @return a converter from S to T
     */
    @Override
    public <T extends BaseEnum<String>> Converter<String, T> getConverter(Class<T> targetType) {
        Converter<String, T> converter = CONVERTERS.get(targetType);
        if(converter == null){
            converter = new ObjectToEnumConverter(targetType);
            CONVERTERS.put(targetType, converter);
        }

        return converter;
    }

    static final class ObjectToEnumConverter<E extends BaseEnum> implements Converter<String, E> {
        private final Map<String, E> enumMap = Maps.newHashMap();

        ObjectToEnumConverter(Class<E> enumType){
            E[] enums = enumType.getEnumConstants();
            for (E e : enums) {
                enumMap.put(e.getValue().toString(), e);
            }
        }

        @Override
        @Nullable
        public E convert(String source) {
            E e = enumMap.get(source);
            if(ObjectUtils.isEmpty(e)) {
                return null;
            }

            return e;
        }
    }
}