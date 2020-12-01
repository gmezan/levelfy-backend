package com.uc.backend.controller.estudiante;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.uc.backend.dto.RegistroPagoDto;
import com.uc.backend.entity.*;
import com.uc.backend.job.asesper.AsesPersEnrollDeactivationJob;
import com.uc.backend.job.asesper.AsesPersEnrollExpirationJob;
import com.uc.backend.repository.*;
import com.uc.backend.service.CustomEmailService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.uc.backend.utils.CustomConstants.*;

@Controller
@RequestMapping("/service/ases-per")
public class ServAsesoriaPersonalizadaController {
    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    DisponibilidadProfesorRepository disponibilidadProfesorRepository;

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Autowired
    ClaseSesionRepository claseSesionRepository;

    @Autowired
    ComentarioForoRepository comentarioForoRepository;

    @Autowired
    VentaRepository ventaRepository;

    @Autowired
    VentaCanceladaRepository ventaCanceladaRepository;

    @Autowired
    CustomEmailService customEmailService;

    @Autowired
    private Scheduler scheduler;

    //Procesa la inscripción
    @PostMapping("/enroll")
    public String cursoProcessAsesoria(Model model, @ModelAttribute("asesoria") @Valid ClaseEnroll claseEnroll,
                                       BindingResult bindingResult, HttpSession session,
                                       RedirectAttributes attributes) throws SchedulerException {
        ZonedDateTime dateTime = ZonedDateTime.now();
        LocalDateTime ldt;
        Optional<Clase> optionalClase = claseRepository.findByIdclaseAndServicioAndDisponibleIsTrue(claseEnroll.getClase().getIdclase(), SERVICIO_ASESORIA_PERSONALIZADA);
        ArrayList<LocalDate> dtProgramado = new ArrayList<>();
        //Se obtienen las clases programadas (días) para esa asesoría
        optionalClase.ifPresent(clase -> claseEnrollRepository.findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClaseDisponibleIsTrueAndInicioasesoriaIsAfter(
                        clase.getProfesor().getIdusuario(), SERVICIO_ASESORIA_PERSONALIZADA, LocalDateTime.now().minusHours(5)).forEach((claseEnroll1) -> dtProgramado.add(claseEnroll1.getInicioasesoria().toLocalDate())));

        //Día de la asesoría
        if (claseEnroll.getDiaClase()==null){
            bindingResult.rejectValue("diaClase","error.user","Elija un día para la asesoría");
        }
        else if (dtProgramado.contains(claseEnroll.getDiaClase())){
            bindingResult.rejectValue("diaClase","error.user","Elija un día para la asesoría válido");
        }
        else if(claseEnroll.getHoraClase()==null){//Hora de inicio
            bindingResult.rejectValue("horaClase","error.user","Elija una hora de inicio");
        }
        else {
            ldt = LocalDateTime.of(claseEnroll.getDiaClase(),claseEnroll.getHoraClase());
            if (optionalClase.isPresent() && !optionalClase.get().getProfesor().mapDisponibilidad().containsKey(ldt))
                bindingResult.rejectValue("horaClase","error.user","Elija una hora de inicio válida");
        }
        //Horas de asesoría
        if(claseEnroll.getNumHoras()==null || !(claseEnroll.getNumHoras()==1||claseEnroll.getNumHoras()==2))
            bindingResult.rejectValue("numHoras","error.user","Elija un número de horas");
        //Número de estudiantes
        if(claseEnroll.getCantidad()==null || !claseEnroll.validateCant())
            bindingResult.rejectValue("cantidad", "error.user", "Elija un número de estudiantes");
        //Mensaje
        if (claseEnroll.getInfo()!=null && claseEnroll.getInfo().length()>511)
            bindingResult.rejectValue("cantidad", "error.user", "Elija un mensaje válido");

        if (optionalClase.isPresent()) {
            if (bindingResult.hasErrors()){
                claseEnroll.setClase(optionalClase.get());
                model.addAttribute("asesoria", claseEnroll);
                model.addAttribute("paquete", PAQUETES.get(1));
                return "cursos/formAsesoria"; //Envio a formulario de registro a Asesoria
            }
            ldt = LocalDateTime.of(claseEnroll.getDiaClase(),claseEnroll.getHoraClase());
            claseEnroll.setEstudiante((Usuario)session.getAttribute("usuario"));
            claseEnroll.setClase(optionalClase.get());
            claseEnroll.setPagado(Boolean.FALSE);
            claseEnroll.setInicioasesoria(ldt);
            claseEnroll.setFinasesoria(ldt.plusHours(claseEnroll.getNumHoras()));
            claseEnroll.setActive(Boolean.TRUE);
            claseEnrollRepository.save(claseEnroll);
            JobDetail jobDetail = buildExpirationJobDetail(claseEnroll);
            JobDetail jobDetail2 = buildDeactivationJobDetail(claseEnroll);
            scheduler.scheduleJob(jobDetail, buildExpirationJobTrigger(jobDetail, dateTime.plusMinutes(MINUTES_EXPIRATION_TIME_FOR_ASES_PER)));
            //scheduler.scheduleJob(jobDetail2, buildDeactivationJobTrigger(jobDetail2, ZonedDateTime.of(claseEnroll.getFinasesoria().plusHours(6),dateTime.getZone())));
            attributes.addFlashAttribute("msgSuccess", "Gracias por inscribirte, recuerda realizar el pago en las siguientes "+(MINUTES_EXPIRATION_TIME_FOR_ASES_PER/60) + " horas, de lo contrario esta inscripción expirará.");
            return "redirect:/service/ases-per/"+claseEnroll.getIdClaseEnroll();
        } else {
            attributes.addFlashAttribute("msgError", "Ocurrió un problema");
            return "redirect:/c/ases-per";
        }
    }

    @GetMapping("/{id}")
    public String confirmAsesoria(Model model, @PathVariable(name = "id") int idClaseEnroll,
                                  @ModelAttribute("asesoriaEnroll") ClaseEnroll asesoriaEnroll, HttpSession session)
    {
        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio
                        (idClaseEnroll, user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA);

        if(optionalClaseEnroll.isPresent()){
            asesoriaEnroll = optionalClaseEnroll.get();
            //Si hay un registro de pago
            ventaRepository.findVentaByClaseEnroll_IdClaseEnroll(asesoriaEnroll.getIdClaseEnroll())
                .ifPresent(venta -> model.addAttribute("registro",venta));

            model.addAttribute("asesoriaEnroll", asesoriaEnroll);
            model.addAttribute("paquete", PAQUETES.get(1));
            model.addAttribute("idUsuario",user.getIdusuario());
            return "estudiante/confirmAsesoriaOnline";
        }
        return "redirect:/c/ases-per";
    }


    @GetMapping("/cancel/{id}")
    public String cancelarAsesoria(HttpSession session,
                                   @PathVariable(name = "id") int idClaseEnroll,
                                   RedirectAttributes attributes) {
        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.
                findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio
                        (idClaseEnroll, user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA);
        if (optionalClaseEnroll.isPresent()){

            ClaseEnroll claseEnroll = optionalClaseEnroll.get();

            if (!claseEnroll.getVentaList().isEmpty()){
                //Si es que pago, se guarda
                ventaCanceladaRepository.saveAll(VentaCancelada.generateVentaCanceladas(claseEnroll.getVentaList()));
                ventaRepository.deleteAll(claseEnroll.getVentaList());
                attributes.addFlashAttribute("msgSuccess", "Inscripción cancelada correctamente. Tenías un registro de pago, contáctanos para realizar la devolución.");
            }
            else {
                attributes.addFlashAttribute("msgSuccess", "Inscripción cancelada correctamente");
            }

            claseEnrollRepository.delete(claseEnroll);
        }
        else {
            attributes.addFlashAttribute("msgError", "Hubo un problema");
        }
        return "redirect:/u";
    }

    @PostMapping("/register")
    public String registroDePago(RegistroPagoDto registroPagoDto, HttpSession session, RedirectAttributes attributes) {
        Usuario user = (Usuario)session.getAttribute("usuario");

        Boolean flag = Boolean.FALSE;
        String msg = "";
        if (registroPagoDto.getIdclase()==null){
            msg="Hubo un problema con el registro, intenta de nuevo. Si el problema persiste, contáctanos";
            attributes.addFlashAttribute("msgError", msg);
            return "redirect:/u";
        }
        else if(registroPagoDto.getFecha()==null || registroPagoDto.getFecha().isBefore(LocalDateTime.now().minusHours(5).minusDays(2)))
            msg = "Hubo un problema al ingresar la fecha";
        else if (registroPagoDto.getPersona()==null || registroPagoDto.getPersona().isEmpty())
            msg = "Hubo un problema al ingresar el nombre de la persona que realizó el pago";
        else if (registroPagoDto.getCorreo()==null || !registroPagoDto.getCorreo().matches(".+@.+\\..+"))
            msg = "Hubo un problema al ingresar el correo electrónico";
        else if (registroPagoDto.getMonto()==null || !(registroPagoDto.getMonto().compareTo(BigDecimal.ZERO)>=0))
            msg = "Ingrese una cantidad válida";
        else if (!(METODOS_DE_PAGO.length>registroPagoDto.getMetodo() && registroPagoDto.getMetodo()>=0))
            msg = "Hubo un problema al ingresar el método de pago";
        else
            flag = Boolean.TRUE;

        if(flag) {
            Optional<ClaseEnroll> optionalClaseEnroll = claseEnrollRepository.
                    findByIdClaseEnrollAndEstudiante_IdusuarioAndActiveIsTrueAndClase_Servicio
                            (registroPagoDto.getIdclase(), user.getIdusuario(),SERVICIO_ASESORIA_PERSONALIZADA);
            if (optionalClaseEnroll.isPresent()) {
                Venta venta = ventaRepository.findVentaByClaseEnroll_IdClaseEnroll(optionalClaseEnroll.get().getIdClaseEnroll()).orElse(new Venta());
                venta.setMonto(registroPagoDto.getMonto());
                venta.setClaseEnroll(optionalClaseEnroll.get());
                venta.setFechapago(registroPagoDto.getFecha());
                venta.setPersona(registroPagoDto.getPersona());
                venta.setMensaje(registroPagoDto.getMensaje());
                venta.setMetodo(registroPagoDto.getMetodoStr());
                if(registroPagoDto.getCupon() != null && !registroPagoDto.getCupon().isEmpty()){
                    venta.setCupon(registroPagoDto.getCupon());
                }

                try {
                    ventaRepository.save(venta);
                    customEmailService.sendAsesPerRegisterMail(user, registroPagoDto);
                    attributes.addFlashAttribute("msgSuccess", "Registro de pago exitoso, confirmaremos el registro en breve");
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                    attributes.addFlashAttribute("msgError", "Hubo un problema con el registro, intenta de nuevo. Si el problema persiste, contáctanos");
                }
            }
        }
        else{
            attributes.addFlashAttribute("msgError", msg);
        }
        return "redirect:/service/ases-per/"+registroPagoDto.getIdclase();
    }

    /*
    @PostMapping("/register")
    public String registroDePago(@RequestParam(name = "nombre") String nombre, @RequestParam(name = "correo") String correo,
                                 @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha, @RequestParam(name = "mensaje") String mensaje,
                                 @RequestParam(name = "monto") String monto, @RequestParam(name = "metodo") int metodo, HttpSession session) {


        mensaje = "Hola se ha registrado el pago con los siguientes datos:\n" + "\n alumno:" + ((Usuario) session.getAttribute("usuario")).getNombre() + "mensaje o número de operación:" + mensaje + "\n fecha del pago:" + fecha + "\n monto:" + monto + "a través de" + METODOS_DE_PAGO[metodo] + "\n Verficaremos los datos y procederemos a activar el curso " + "\n Si confundiste alguno de los datos vuelve a registrar el pago.";
        customEmailService.sendSimpleMessage(correo, "Pago Registrado", mensaje);
        customEmailService.sendSimpleMessage("peru.universityclass@gmail.com", "Pago Registrado", mensaje);

        return "redirect:/home";
    }


*/

    @GetMapping("/asesoriaOnline/foro")
    public String mostraroForo(Model model, @RequestParam("id") int id, HttpSession session) {
        Optional<Clase> claseOptional = claseRepository.findByIdclaseAndServicioAndDisponibleIsTrue(id, SERVICIO_ASESORIA_PERSONALIZADA);
        Usuario usuario = (Usuario) session.getAttribute("usuario");


        if (claseOptional.isPresent()) {
            Clase clase = claseOptional.get();
            List<ComentarioForo> comentarios = comentarioForoRepository.findByClase(clase);
            Optional<ClaseEnroll> asesoriaEnroll = claseEnrollRepository.findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrueAndPagadoIsTrue( clase.getIdclase(),usuario.getIdusuario());

            if  (asesoriaEnroll.isPresent()) {
                ClaseEnroll claseEnroll=asesoriaEnroll.get();
                if (claseEnroll.getPagado()) {
                    session.setAttribute("isPaquete", false);
                    session.setAttribute("asesoria", clase);
                    session.setAttribute("asesoriaEnroll", claseEnroll);
                    model.addAttribute("comentarios", comentarios);
                    model.addAttribute("asesoria", clase);
                    model.addAttribute("asesoriaEnroll", claseEnroll);
                    return "cursos/foro"; //Envio a formulario de registro a Asesoria
                }

            }
            return "redirect:/asesoriaOnline";
        } else {
            return "redirect:/asesoriaOnline";
        }
    }

    @GetMapping("/paqueteAsesoriaOnline/foroPaquete")
    public String mostraroForoPaquete(Model model, @RequestParam("id") int id, HttpSession session, String Ms) {
        Optional<Clase> claseOptional = claseRepository.findByIdclaseAndServicio(id,SERVICIO_ASESORIA_PAQUETE);
        Usuario usuario = (Usuario) session.getAttribute("usuario");


        if (claseOptional.isPresent()) {
            Clase clase = claseOptional.get();
            List<ComentarioForo> comentarios = comentarioForoRepository.findByClase(clase);
            Optional<ClaseEnroll> asesoriaEnroll = claseEnrollRepository.findByClase_IdclaseAndEstudiante_IdusuarioAndActiveIsTrueAndPagadoIsTrue( clase.getIdclase(),usuario.getIdusuario());

            if  (asesoriaEnroll.isPresent()) {
                ClaseEnroll claseEnroll=asesoriaEnroll.get();
                if (claseEnroll.getPagado()) {

                    session.setAttribute("isPaquete", true);
                    model.addAttribute("comentarios", comentarios);
                    session.setAttribute("paqueteAsesoria", clase);


                    model.addAttribute("paqueteAsesoria", clase);

                    return "cursos/foroPaquete"; //Envio a formulario de registro a Asesoria
                }

            }else if (clase.getProfesor().getIdusuario()==usuario.getIdusuario()){
                session.setAttribute("isPaquete", true);
                model.addAttribute("comentarios", comentarios);
                session.setAttribute("paqueteAsesoria", clase);


                model.addAttribute("paqueteAsesoria", clase);

                return "cursos/foroPaquete"; //Envio a formulario de registro a Asesoria
            }

            //Envio a formulario de registro a Asesoria


            return "redirect:/home";
        } else {
            return "redirect:/home";
        }
    }

    @PostMapping("/paqueteAsesoriaOnline/foroPaquete/guardarComentario")
    public String guardarComentarioForoPaquete(Model model, HttpSession session, RedirectAttributes attr, ComentarioForo comentarioForo, @RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
        Clase paqueteAsesoria = (Clase) session.getAttribute("paqueteAsesoria");
        comentarioForo.setClase(paqueteAsesoria);
        LocalDateTime today = LocalDateTime.now();
        comentarioForo.setFecha(today);
      Usuario usuario=  (Usuario) session.getAttribute("usuario");
        comentarioForo.setUsuario((Usuario) session.getAttribute("usuario"));


        if (file.isEmpty()) {
            if (comentarioForo.getComentario().isEmpty()) {

                List<ComentarioForo> comentarios = comentarioForoRepository.findByClase(paqueteAsesoria);

                model.addAttribute("comentarios", comentarios);
                model.addAttribute("paqueteAsesoria", paqueteAsesoria);
                attr.addFlashAttribute("msgFile", "Al parecer no has escrito nada");
                return "redirect:/service/ases-per/paqueteAsesoriaOnline/foroPaquete?id=" + paqueteAsesoria.getIdclase();
            }else{
                comentarioForoRepository.save(comentarioForo);
                if ((usuario.getIdusuario()!=paqueteAsesoria.getProfesor().getIdusuario())) {
                    customEmailService.sendSimpleMessage(paqueteAsesoria.getProfesor().getCorreo(), "Comentario en el foro " + paqueteAsesoria.getCurso().getNombre(), "Se ha registrado un comentario en www.myuniversityclass.com/service/ases-per/paqueteAsesoriaOnline/foroPaquete?id=" + paqueteAsesoria.getIdclase());
                }
                List<ComentarioForo> comentarios = comentarioForoRepository.findByClase(paqueteAsesoria);

                model.addAttribute("comentarios", comentarios);
                model.addAttribute("paqueteAsesoria", paqueteAsesoria);

                return "redirect:/service/ases-per/paqueteAsesoriaOnline/foroPaquete?id=" + paqueteAsesoria.getIdclase();}

        } else {
            String[] nombreYextension = file.getOriginalFilename().split("\\.");
            String nombreArchivo = nombreYextension[0];
            String extension = nombreYextension[1];
            String nombreArchivoFinal = nombreArchivo +((Usuario) session.getAttribute("usuario")).getIdusuario();


            AWSCredentials credentials = new BasicAWSCredentials(AMAZON_KEY_S3,AMAZON_SEC_S3);

            // create a client connection based on credentials
            //AmazonS3 s3client = new AmazonS3Client(credentials);

            AmazonS3Client s3client =(AmazonS3Client) AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.SA_EAST_1)
                    .build();

            InputStream is = file.getInputStream();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(is.available());
            // create bucket - name must be unique for all S3 users
            String bucketName = BUCKET_NAME;
            //  s3client.createBucket(bucketName);



            // create folder into bucket
            String folderName = FOLDER_NAME;
            //CommonService.createFolder(bucketName, folderName, s3client,SUFFIX);

            // upload file to folder and set it to public
            String fileName = folderName + SUFFIX + nombreArchivoFinal+"."+extension;
            s3client.putObject(
                    new PutObjectRequest(bucketName, fileName,is, meta)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
            String url=s3client.getResourceUrl("myuniversityclass",fileName);

            System.out.println("Execution Completed");
            System.out.println(url);
/*
            File serverFile;
            for (int i = 2; true; i++) {
                serverFile = new File( nombreArchivoFinal + "." + extension);
                if (serverFile.exists()) {
                    nombreArchivoFinal = nombreArchivo + "(" + i + ")";
                } else {
                    break;
                }
            }
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                byte[] bytes = file.getBytes();
                stream.write(bytes);

            } catch (IOException e) {
                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, e);
                model.addAttribute("msgFile", "Ocurió un error al subir el archivo");
                return "redirect:/service/ases-per/paqueteAsesoriaOnline/foroPaquete?id=" + paqueteAsesoria.getIdclase();
            }*/
            comentarioForo.setFoto(url);
            comentarioForoRepository.save(comentarioForo);
            List<ComentarioForo> comentarios = comentarioForoRepository.findByClase(paqueteAsesoria);
if ((usuario.getIdusuario()!=paqueteAsesoria.getProfesor().getIdusuario())) {
    customEmailService.sendSimpleMessage(paqueteAsesoria.getProfesor().getCorreo(), "Comentario en el foro " + paqueteAsesoria.getCurso().getNombre(), "Se ha registrado un comentario en www.myuniversityclass.com/service/ases-per/paqueteAsesoriaOnline/foroPaquete?id=" + paqueteAsesoria.getIdclase());
}      model.addAttribute("comentarios", comentarios);
            model.addAttribute("paqueteAsesoria", paqueteAsesoria);

            //Envio a formulario de registro a Asesoria


            return "redirect:/service/ases-per/paqueteAsesoriaOnline/foroPaquete?id=" + paqueteAsesoria.getIdclase();
        }


    }

/*
    @PostMapping("/asesoriaOnline/foro/guardarComentario")
    public String guardarComentarioForo(Model model, HttpSession session, ComentarioForo comentarioForo, @RequestParam(name = "file", required = false) MultipartFile file) {
        Asesoria asesoria = (Asesoria) session.getAttribute("asesoria");
        comentarioForo.setAsesoria(asesoria);
        LocalDateTime today = LocalDateTime.now();
        comentarioForo.setFecha(today);

        comentarioForo.setUsuario((Usuario) session.getAttribute("usuario"));


        comentarioForoRepository.save(comentarioForo);
        List<ComentarioForo> comentarios = comentarioForoRepository.findByAsesoria(asesoria);
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("asesoria", asesoria);

        return "redirect:/asesoriaOnline/foro?id=" + asesoria.getIdasesoria();
    }


*/


    //------------------------------------------------------------------------------------------------------FIN ASESORIA


    //Expiration (3horas)
    private JobDetail buildExpirationJobDetail(ClaseEnroll claseEnroll) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("idClaseEnroll", claseEnroll.getIdClaseEnroll());

        return JobBuilder.newJob(AsesPersEnrollExpirationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "ases-per-jobs")
                .withDescription("ASES_PER Inscription expiration Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
    private Trigger buildExpirationJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "ases-per-triggers")
                .withDescription("Expiration for ASES_PER inscription")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }


    //Deactivation (despues de la clase)
    private JobDetail buildDeactivationJobDetail(ClaseEnroll claseEnroll) {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put("idClaseEnroll", claseEnroll.getIdClaseEnroll());

        return JobBuilder.newJob(AsesPersEnrollDeactivationJob.class)
                .withIdentity(UUID.randomUUID().toString(), "ases-per-deac-jobs")
                .withDescription("ASES_PER Inscription Deactivation Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }
    private Trigger buildDeactivationJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "ases-per-deac-triggers")
                .withDescription("Deactivation for ASES_PER inscription")
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }

}
