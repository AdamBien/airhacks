package com.airhacks;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonPatch;
import javax.json.JsonReader;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Example of how to do bullet-proof system tests using Json-P and reference JSON files as specs
 * @author ratcashdev
 */
public class InterfaceTest {
	
	public JsonObject generateGenuineMascot() {
		return Json.createObjectBuilder()
				.add("name", "Duke")
				.add("role", "mascot").build();
	}
	
	public JsonObject generateFalseMascot() {
		return Json.createObjectBuilder()
				.add("name", "Ellington")
				.add("role", "mascott").build();
	}
	
	public JsonObject generateWorkShopObj() {
		return Json.createObjectBuilder()
				.add("workshop", "Java EE 8 and Java 9")
				.add("date", "2017-12-14").build();
	}
	
	JsonObject readFile(String fileName) {
		try (InputStream fis = getClass().getResourceAsStream(fileName)) {
			JsonReader reader = Json.createReader(fis);
			JsonObject obj = reader.readObject();
			return obj;
		} catch (IOException ex) {
			Logger.getLogger(InterfaceTest.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	@Test
	public void testJsonInclValues() {
		String serviceSpecFileName = "service-mascot.json";
		JsonObject refObject = readFile(serviceSpecFileName);
		JsonPatch diff1 = Json.createDiff(generateGenuineMascot(), refObject);
		assertThat(diff1.toJsonArray().size(), is(equalTo(0)));
		
		JsonPatch diff2 = Json.createDiff(generateFalseMascot(), refObject);
		assertThat(diff2.toJsonArray().size(), is(not(equalTo(0))));
	}
	
	
	
	@Test
	public void testJsonStructureOnly() {
		String serviceSpecFileName = "service-mascot.json";
		JsonObject refObject = readFile(serviceSpecFileName);
		
		JsonPatch diff1 = Json.createDiff(generateFalseMascot(), refObject);
		
		// not even the structure is correct
		assertTrue(isValueChangeOnly(diff1));
	}
	
	
	
	@Test
	public void testJsonBadInterface() {
		String serviceSpecFileName = "service-mascot.json";
		JsonObject refObject = readFile(serviceSpecFileName);
		JsonPatch diff1 = Json.createDiff(refObject, generateWorkShopObj());
		assertThat(diff1.toJsonArray().size(), is(not(equalTo(0))));
		
		// not even the structure is correct
		assertFalse(isValueChangeOnly(diff1));
	}
	
	/**
	 * must contain only replace operators, that means structure is identical
	 * @param diff
	 * @return true if only replace operations are contained
	 */
	boolean isValueChangeOnly(JsonPatch diff) {
		JsonArray jsonArray = diff.toJsonArray();
		int replaceOpsSize = jsonArray.stream().filter(f -> 
				f.asJsonObject().getJsonString("op").getString().contentEquals("replace"))
				.collect(Collectors.toList()).size();
		return replaceOpsSize == jsonArray.size();
	}
}
