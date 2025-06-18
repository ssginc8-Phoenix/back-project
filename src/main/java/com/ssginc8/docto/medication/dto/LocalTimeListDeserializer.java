package com.ssginc8.docto.medication.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class LocalTimeListDeserializer extends StdDeserializer<List<LocalTime>> {

	public LocalTimeListDeserializer() {
		super(List.class);  // List 타입으로 지정
	}

	@Override
	public List<LocalTime> deserialize(JsonParser p, DeserializationContext ctxt)
		throws IOException {
		JsonNode root = p.getCodec().readTree(p);
		List<LocalTime> times = new ArrayList<>();

		for (JsonNode node : root) {
			if (node.isTextual()) {
				// "HH:mm" 문자열 처리
				times.add(LocalTime.parse(node.asText()));
			} else if (node.isObject()) {
				// { "hour": 8, "minute": 0 } 형태 처리
				JsonNode hNode = node.get("hour");
				JsonNode mNode = node.get("minute");
				if (hNode != null && mNode != null && hNode.isInt() && mNode.isInt()) {
					times.add(LocalTime.of(hNode.asInt(), mNode.asInt()));
				} else {
					throw new JsonMappingException(p,
						"Invalid time object: " + node.toString());
				}
			} else {
				throw new JsonMappingException(p,
					"Unsupported time format: " + node.toString());
			}
		}
		return times;
	}
}