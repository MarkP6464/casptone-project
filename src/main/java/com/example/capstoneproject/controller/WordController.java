package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExperienceDto;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/word")
public class WordController {

    @PostMapping("/create")
    public ResponseEntity<byte[]> createWordDocument() {
        try {
            XWPFDocument doc = new XWPFDocument();

            // Tạo một đoạn văn bản và chèn dữ liệu
            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("Hello, World!");

            // Lưu tệp Word
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            doc.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "your_word_file.docx");

            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<byte[]> insertDataIntoWordDocument() {
        try {
            String inputFilePath = "C:\\Users\\lolan\\Downloads\\damanhbang.docx";
            String outputFilePath = "C:\\Users\\lolan\\Downloads\\Modified_damanhbang.docx"; // Đường dẫn cho tệp Word đã chỉnh sửa

            FileInputStream fis = new FileInputStream(new File(inputFilePath));
            XWPFDocument doc = new XWPFDocument(fis);
            fis.close();
            ExperienceDto experienceDto = new ExperienceDto();

// Tạo danh sách để chứa các mục
            List<ExperienceDto> experienceList = new ArrayList<>();

// Tạo mục thứ nhất và đặt giá trị
            ExperienceDto experience1 = new ExperienceDto();
            experience1.setId(1);
            experience1.setRole("Role 1");
            experience1.setCompanyName("Company 1");
            experience1.setDuration("Duration 1");
            experience1.setDescription("Description 1");

// Tạo mục thứ hai và đặt giá trị
            ExperienceDto experience2 = new ExperienceDto();
            experience2.setId(2);
            experience2.setRole("Role 2");
            experience2.setCompanyName("Company 2");
            experience2.setDuration("Duration 2");
            experience2.setDescription("Description 2");

// Thêm các mục vào danh sách
            experienceList.add(experience1);
            experienceList.add(experience2);


            // Lặp qua danh sách đoạn văn bản trong tài liệu Word
            for (ExperienceDto experience : experienceList) {
                for (XWPFParagraph paragraph : doc.getParagraphs()) {
                    for (XWPFRun run : paragraph.getRuns()) {
                        String text = run.getText(0);
                        if (text != null) {
                            text = text.replace("#name#", experience.getRole());
                            text = text.replace("#company#", experience.getCompanyName());
                            text = text.replace("#duration#", experience.getDuration());
                            text = text.replace("#description#", experience.getDescription());

                            run.setText(text, 0);
                        }
                    }
                }
            }

            // Lưu tệp Word đã chỉnh sửa
            FileOutputStream fos = new FileOutputStream(outputFilePath);
            doc.write(fos);
            fos.close();

            // Trả về tệp Word đã chỉnh sửa
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "Modified_Pham Viet Thuan Thien CV.docx"); // Đổi tên tệp đã chỉnh sửa tại đây

            return new ResponseEntity<>(Files.readAllBytes(Paths.get(outputFilePath)), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateWordDocument")
    public ResponseEntity<String> updateWordDocument() {
        try {
            String inputFilePath = "C:\\Users\\lolan\\Downloads\\template.docx";
            String outputFilePath = "C:\\Users\\lolan\\Downloads\\Modified_template.docx"; // Đường dẫn cho tệp Word đã chỉnh sửa

            FileInputStream fis = new FileInputStream(new File(inputFilePath));
            XWPFDocument doc = new XWPFDocument(fis);
            fis.close();

            // Định vị "EXPERIENCE" và "EDUCATION" paragraphs
            XWPFParagraph experience = null;
            XWPFParagraph education = null;

            for (XWPFParagraph paragraph : doc.getParagraphs()) {
                if (paragraph.getText().equals("#EXPERIENCE#")) {
                    experience = paragraph;
                } else if (paragraph.getText().equals("#EDUCATION#")) {
                    education = paragraph;
                }
            }

            if (experience != null && education != null) {
                // Xóa nội dung của "EXPERIENCE"
                int pos = doc.getPosOfParagraph(experience);
                doc.removeBodyElement(pos);

                // Xóa nội dung của "EDUCATION"
                int posEdu = doc.getPosOfParagraph(education);
                doc.removeBodyElement(posEdu);

                // Chèn nội dung của "EDUCATION" vào vị trí mới (trước "#EXPERIENCE#")
                int newPos = doc.getPosOfParagraph(experience);
                if (newPos != -1) {
                    doc.setParagraph(education, newPos);
                } else {
                    System.out.println("Could not find the position for #EXPERIENCE#");
                }

                // Chèn nội dung của "EXPERIENCE" vào vị trí mới (trước "#EDUCATION#")
                int newPosEdu = doc.getPosOfParagraph(education);
                if (newPosEdu != -1) {
                    doc.setParagraph(experience, newPosEdu);
                } else {
                    System.out.println("Could not find the position for #EDUCATION#");
                }

                // Lưu tệp Word đã được cập nhật
                FileOutputStream out = new FileOutputStream(outputFilePath); // Sử dụng outputFilePath để lưu tệp đã cập nhật
                doc.write(out);
                out.close();
            } else {
                System.out.println("Could not find '#EXPERIENCE#' and '#EDUCATION#' in the document.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred.");
        }
        return ResponseEntity.ok("Document updated successfully!");
    }



}

