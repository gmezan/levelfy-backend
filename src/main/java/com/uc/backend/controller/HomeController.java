package com.uc.backend.controller;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.uc.backend.entity.Usuario;
import com.uc.backend.repository.UsuarioRepository;
import com.uc.backend.utils.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;


import static com.uc.backend.utils.CustomConstants.*;


@Controller
public class HomeController {

    @Autowired
    UsuarioRepository usuarioRepository;

    /**@GetMapping("home")
    public String home(){
        return "redirect:/";
    }

    @GetMapping(value = {"","/"})
    public String index(){
        return "index";
    }
*/
    //Página de inicio
    @GetMapping(value = {"/home","","/"})
    public String home(Model model, HttpSession session) {

        Usuario user = (Usuario) session.getAttribute("usuario");
if (user!=null){
       try {
           if ((user.getCelular()==0)  || (user.getUniversidad().isEmpty()) ){
               System.out.println("I'm here");
               model.addAttribute("isDataComplete",false);
               model.addAttribute("universidad",UNIVERSIDAD);

           }else{
               System.out.println("data complete");
               model.addAttribute("isDataComplete",true);

           }
       } catch (NullPointerException e) {
           System.out.println("I'm here 2");

           model.addAttribute("isDataComplete",false);
           model.addAttribute("universidad",UNIVERSIDAD);
       }

    }else{
    model.addAttribute("isDataComplete",true);

}
        return "index";
    }

    @PostMapping(value = {"/homeDataProcess"})
    public String agregarDatos(Model model, HttpSession session,
                               RedirectAttributes redirectAttributes,
                               @RequestParam("universidad") String universidad,
                               @RequestParam("celular") String celular,
                               @RequestParam("fdc") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fdc){
        int celularInt=0;
        try {
         celularInt=Integer.parseInt(celular);

        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("msgDataCompleted","¡Uy! Al parecer hubo un dato incorrecto");
            return "redirect:/home";

        }

        if( universidad.isEmpty() || (celularInt==0)|| fdc==null || (celularInt>999999999)||(celularInt<900000000) ){
            redirectAttributes.addFlashAttribute("msgDataCompleted","¡Uy! Al parecer hubo un dato incorrecto");
        }else{
            Usuario user=(Usuario)session.getAttribute("usuario");
            Usuario usuario = usuarioRepository.findById(user.getIdusuario()).get();
            usuario.setCelular(celularInt);
            usuario.setUniversidad(universidad);
            usuario.setFdc(fdc);
            usuarioRepository.save(usuario);
            session.setAttribute("usuario",usuario);
        }
        return "redirect:/home";

    }


}
