package com.kashegypt.gateway.jms.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@UtilityClass
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts an object to json representation
     *
     * @param object          Object to serialize
     * @return Json string representation of the object
     */
    public static String toJsonString(Object object) {
        try {
            ObjectWriter jsonWriter = createJsonWriter(null, null);

            String json = jsonWriter.writeValueAsString(object);
            logger.info(json);
            return json;

        } catch (Exception ex) {
            logger.error("Error while converting object to json string", ex);
            return null;
        }

    }

    /**
     * Converts an object to json representation
     *
     * @param object          Object to serialize
     * @param enableFeatures  Serialization features to enable on writer
     * @param disableFeatures Serialization features to disable on writer
     * @return Json string representation of the object
     */
    public static String toJsonString(Object object, SerializationFeature[] enableFeatures, SerializationFeature[] disableFeatures) {
        try {


            ObjectWriter jsonWriter = createJsonWriter(enableFeatures, disableFeatures);

            String json = jsonWriter.writeValueAsString(object);
            logger.info(json);
            return json;

        } catch (Exception ex) {
            logger.error("Error while converting object to json string", ex);
            return null;
        }

    }

    /**
     * Initializes and returns an instance of a json writer
     *
     * @param enableFeatures  deserialization features to enable on writer
     * @param disableFeatures deserialization features to disable on writer
     * @return an instance of {@link ObjectWriter}
     */
    private static ObjectWriter createJsonWriter(SerializationFeature[] enableFeatures, SerializationFeature[] disableFeatures) {
        ObjectWriter jsonWriter = mapper.writer();
        if (enableFeatures != null && enableFeatures.length > 0)
            jsonWriter = jsonWriter.withFeatures(enableFeatures);
        if (disableFeatures != null && disableFeatures.length > 0)
            jsonWriter = jsonWriter.withoutFeatures(disableFeatures);
        return jsonWriter;
    }

    /**
     * Converts a Json string to Object representation
     *
     * @param json            Json string to deserialize
     * @param type            Type meta data of target class as required by reader to correctly deserialize the json string
     * @param enableFeatures  deserialization features to enable on writer
     * @param disableFeatures deserialization features to disable on writer
     * @return Object representation of provided Json string
     */
    public static <T> T fromString(String json, TypeReference<T> type, DeserializationFeature[] enableFeatures, DeserializationFeature[] disableFeatures) {
        try {

            ObjectReader jsonReader = createJsonReader(type, enableFeatures, disableFeatures);

            T object = jsonReader.readValue(json);
            logger.info(json);
            return object;

        } catch (Exception ex) {
            throw new RuntimeException("Error while converting object to json string", ex);
        }
    }

    /**
     * Initializes and returns an instance of a json reader
     *
     * @param type            Type meta data of target class as required by reader to correctly deserialize the json string
     * @param enableFeatures  deserialization features to enable on writer
     * @param disableFeatures deserialization features to disable on writer
     * @return an instance of {@link ObjectReader}
     */
    private static <T> ObjectReader createJsonReader(TypeReference<T> type, DeserializationFeature[] enableFeatures, DeserializationFeature[] disableFeatures) {
        ObjectReader jsonReader = mapper.readerFor(type);
        if (enableFeatures != null && enableFeatures.length > 0)
            jsonReader = jsonReader.withFeatures(enableFeatures);
        if (disableFeatures != null && disableFeatures.length > 0)
            jsonReader = jsonReader.withoutFeatures(disableFeatures);
        return jsonReader;
    }
}
