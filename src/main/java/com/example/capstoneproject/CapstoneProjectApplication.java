package com.example.capstoneproject;

import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.repository.ExpertRepository;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@OpenAPIDefinition(
		servers = {
				@Server(url = "https://cvbuilder-api.monoinfinity.net", description = "Default Server URL"),
				@Server(url = "http://localhost:8080", description = "Environment Dev"),
				@Server(url = "http://localhost:3000", description = "Open Cors")
		}
)
@SpringBootApplication
public class CapstoneProjectApplication {

	public static void main(String[] args) {
		initializeStanfordCoreNLP();
//		String fullString = "Participating in MoMo Talent Program 2022. Contributing to the gamification campaign, MoMo Barista, with a target of achieving 4,000,000 MAU. Defining data tracking in collaboration with developers. Collaborating with marketing, agencies, and developers to manage 800 recipes and 8000 categories.";
//		String substring = "achieving 4,000,000 MAU. Defining";
//
//		if (isSubstringInString(fullString, substring)) {
//			System.out.println("Substring exists in the full string and matches.");
//		} else {
//			System.out.println("Substring does not exist in the full string or does not match.");
//		}
//		String input = "• Participating in <comment content='That so good'>MoMo Talent Program</comment> 2022. • Contributing to the <comment content='this is test 2'>gamification campaign</comment>, MoMo Barista, with a target of achieving 4,000,000 MAU. • Defining <comment>data tracking in collaboration</comment> with developers. • Collaborating with marketing, agencies, and <comment id='7fad381c-433a-42f6-90cf-20df9f0bf36c' content='this is test 5'>developers to manage 800</comment> recipes and 8000 categories.";
//
//		String output = removeCommentTagsWithoutIdAndContent(input);
//		System.out.println(output);

		SpringApplication.run(CapstoneProjectApplication.class, args);
	}

	private static void initializeStanfordCoreNLP() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	}

//	public static boolean isSubstringInString(String fullString, String substring) {
//		int fullLength = fullString.length();
//		int subLength = substring.length();
//
//		int[][] dp = new int[fullLength + 1][subLength + 1];
//
//		for (int i = 1; i <= fullLength; i++) {
//			for (int j = 1; j <= subLength; j++) {
//				if (fullString.charAt(i - 1) == substring.charAt(j - 1)) {
//					dp[i][j] = dp[i - 1][j - 1] + 1;
//				} else {
//					dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
//				}
//			}
//		}
//
//		return dp[fullLength][subLength] == subLength;
//	}
//
//	public static String removeCommentTagsWithoutIdAndContent(String input) {
//		// Tạo regex pattern để tìm thẻ <comment> không chứa id và content
//		String regex = "<comment>(.*?)</comment>";
//		Pattern pattern = Pattern.compile(regex);
//		Matcher matcher = pattern.matcher(input);
//
//		// Sử dụng StringBuilder để xây dựng chuỗi mới
//		StringBuilder result = new StringBuilder();
//		int lastEnd = 0;
//
//		// Duyệt qua tất cả các thẻ <comment> không chứa id và content
//		while (matcher.find()) {
//			// Lấy vị trí bắt đầu và kết thúc của thẻ
//			int start = matcher.start();
//			int end = matcher.end();
//
//			// Thêm nội dung trước thẻ vào chuỗi kết quả
//			result.append(input, lastEnd, start);
//
//			// Thêm nội dung bên trong thẻ vào chuỗi kết quả
//			result.append(matcher.group(1));
//
//			// Cập nhật vị trí lastEnd
//			lastEnd = end;
//		}
//
//		// Thêm nội dung sau cùng vào chuỗi kết quả
//		result.append(input, lastEnd, input.length());
//
//		return result.toString();
//	}


}
