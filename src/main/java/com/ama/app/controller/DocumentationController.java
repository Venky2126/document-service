package com.ama.app.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ama.app.doc.DynamicSwaggerToWordService;
import com.ama.app.doc.DynamicSwaggerToWordServiceResponseFlat;


@RestController
@RequestMapping("/api/documentation")
public class DocumentationController {

    @Autowired
    private DynamicSwaggerToWordService docGenerator;
    @Autowired
    private DynamicSwaggerToWordServiceResponseFlat responseObject;

    @PostMapping("/generate")
    public ResponseEntity<Resource> generateDocumentation(
            @RequestParam("yamlPath") String yamlPath) throws Exception {
        
        File wordDoc = docGenerator.generateWordDocFromYaml(yamlPath);
        
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=\"" + wordDoc.getName() + "\"")
            .body(new FileSystemResource(wordDoc));
    }
    
    @PostMapping("/response")
    public ResponseEntity<Resource> generateDocumentationResponseObject(
            @RequestParam("yamlPath") String yamlPath) throws Exception {
        
        File wordDoc = responseObject.generateWordDocFromYaml(yamlPath);
        
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, 
                   "attachment; filename=\"" + wordDoc.getName() + "\"")
            .body(new FileSystemResource(wordDoc));
    }
    
    

}
