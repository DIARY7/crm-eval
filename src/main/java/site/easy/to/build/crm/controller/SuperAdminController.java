package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import site.easy.to.build.crm.service.database.DatabaseService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/database/")
public class SuperAdminController {
    @Autowired
    DatabaseService dataService;


    @GetMapping("reset")
    public String resetBase(){
        try{
            dataService.resetDatabase();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }

    @GetMapping("import")
<<<<<<< HEAD
    public String toFormImport(){
        return "database/import-data";
=======
    public String toImportBase(@RequestParam("file") MultipartFile file){
        try {
            dataService.importerCsv(file);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "redirect:/";
>>>>>>> 526086c40acecb3f66d0da33d989845967ccd828
    }

    @PostMapping("importer")
    public String toImportBase(@RequestParam("file") MultipartFile file){
        try {
            dataService.importerCsv(file);
        } catch (Exception e) {
            e.printStackTrace();

            // TODO: handle exception
            throw new RuntimeException(e);
        }
        return "redirect:/";
    }
//    @PostMapping("importer")
//    public String importCsvFile(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
//
//        return "redirect:/import";
//    }
}
