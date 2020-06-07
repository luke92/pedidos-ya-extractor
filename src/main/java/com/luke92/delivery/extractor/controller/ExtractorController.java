package com.luke92.delivery.extractor.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExtractorController {
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	
	
	@PostMapping()
	public String uploadFile(@RequestParam("user") String user, 
							@RequestParam("password") String password,  
							@RequestParam("file") MultipartFile file, Model model,
							HttpServletResponse response, RedirectAttributes redirectAttributes) {
		byte[] fileBytes = null;
	    String reportName="yourReportName";
	    try {
	        fileBytes = consultaLote(file);
	        if(fileBytes !=null){
		        response.setHeader("Content-Disposition", "attachment; filename=" + reportName + ".xls");
		        response.setContentType("application/xls");
		        response.getOutputStream().write(fileBytes);
		        response.getOutputStream().flush();
		    }
		    redirectAttributes.addFlashAttribute("message",
		            "You successfully uploaded " + file.getOriginalFilename() + "!");
	    } catch(Exception e) {
			model.addAttribute("error", e.getCause() + " " + e.getMessage());
		}	    

	    return "index";


	}
        
        public byte[] consultaLote(MultipartFile file) throws IOException {
            //whatever changes you want, do it here. I am going to converting part
        	File convFile = new File(((MultipartFile) file).getOriginalFilename());
            convFile.createNewFile(); 
            FileOutputStream fos = new FileOutputStream(convFile); 
            fos.write(((MultipartFile) file).getBytes());
            fos.close(); 

            FileInputStream inputStream = new FileInputStream(convFile);

            Workbook workbook = new XSSFWorkbook(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            // before returning you can close your your ByteArrayOutputStream
            //baos.close();
            return baos.toByteArray();
        }
}
