package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.exception.MyCsvException;
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
    public String toFormImport(Model model){
        return "database/import-data";
    }

    @PostMapping("importer")
    public String toImportBase( Authentication authentication ,Model model , @RequestParam("fileCustomer") MultipartFile fileCustomer,@RequestParam("fileLeadTicket") MultipartFile fileLeadTicket , @RequestParam("fileBudget") MultipartFile fileBudget){
        try {
            dataService.importCSV(fileCustomer,fileLeadTicket,fileBudget,authentication); 
        } 
        catch(MyCsvException csve){
            model.addAttribute("numLigne",csve.getNumLigne());
            System.out.println("Le numero de ligne est "+csve.getNumLigne());
            model.addAttribute("nomFichier", csve.getNomFichier());
            model.addAttribute("errorMessage", csve.getMessage());
            return "database/import-data";
        }
        catch (Exception e) {
            e.printStackTrace();

            // TODO: handle exception
            throw new RuntimeException(e);
        }
        model.addAttribute("success", 1);
        return "database/import-data";
    }

}
