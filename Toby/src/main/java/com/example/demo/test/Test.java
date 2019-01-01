package com.example.demo.test;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;

public class Test {

	public static final String REQ_PATH = "Requset Path";
	public static final String REQ_MIME = "Mime type";
	public static final String FILE_PATH = "File Path";
	
	static final MediaType MIME_HTML = MediaType.TEXT_HTML;
	static final MediaType MIME_JS = MediaType.APPLICATION_JSON;
	
	public static List<Map<String, Object>> Paths = List.of(
		Map.of(
				REQ_PATH, "/", 
				REQ_MIME, MIME_HTML, 
				FILE_PATH, "src/main/views/index.html"
				),
		Map.of(
				REQ_PATH, "/idnex", 
				REQ_MIME, MIME_JS, 
				FILE_PATH, "src/main/views/index.js"
				)
	);
	
	public static void main(String[] args) {
		final String rex = "^([0-9]{1,3})(\\.([0-9]){1,3}){3}:([1-9]){1,4}$";
	
		System.out.println(Pattern.matches(rex, "192.168.0.114:12"));
	}
}
