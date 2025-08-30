package org.softuni.onlinegrocery.domain.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JsonConverter implements AttributeConverter<String, Object> {

    private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object convertToDatabaseColumn(String attribute) {
        if (attribute == null || attribute.trim().isEmpty()) {
            return null;
        }

        try {
            // Parse the JSON to validate it and return as Object for PostgreSQL
            return objectMapper.readValue(attribute, Object.class);
        } catch (JsonProcessingException e) {
            logger.error("Invalid JSON format for attribute: {}", attribute, e);
            // If it's not valid JSON, wrap it as a simple string value
            try {
                return objectMapper.readValue("\"" + attribute.replace("\"", "\\\"") + "\"", Object.class);
            } catch (JsonProcessingException ex) {
                logger.error("Failed to wrap as JSON string: {}", attribute, ex);
                return null;
            } catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return attribute;
    }

    @Override
    public String convertToEntityAttribute(Object dbData) {
        if (dbData == null) {
            return null;
        }

        try {
            // Convert the database JSON object back to string
            return objectMapper.writeValueAsString(dbData);
        } catch (JsonProcessingException e) {
            logger.error("Failed to convert database JSON to string: {}", dbData, e);
            return dbData.toString();
        }
    }
}