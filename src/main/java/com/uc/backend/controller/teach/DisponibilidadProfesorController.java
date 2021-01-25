package com.uc.backend.controller.teach;

import org.springframework.stereotype.Controller;

@Controller
public class DisponibilidadProfesorController {
/*
    @Autowired
    TeacherAvailabilityRepository teacherAvailabilityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    private final int ROL_ASESOR = 2;


    @GetMapping("a/availability")
    public String disponibilidadList(Model entity, HttpSession session){
        entity.addAttribute("availability", teacherAvailabilityRepository
                .findDisponibilidadProfesorsByUsuario_IdusuarioAndUsuario_ActivoIsTrueAndUsuario_Rol_Idrol
                        (((User)session.getAttribute("usuario")).getIdUser(), ROL_ASESOR));
        return "teach/disponibilidad/list";
    }

    @GetMapping("a/availability/new")
    public String disponibilidadNew(@ModelAttribute("disp") TeacherAvailability teacherAvailability,
            Model entity, HttpSession session){
        User user = (User)session.getAttribute("usuario");
        entity.addAttribute("dias", DAYS);
        entity.addAttribute("disp", teacherAvailability);
        entity.addAttribute("titleForm","Nueva Disponibilidad");
        return "teach/disponibilidad/form";
    }


    @GetMapping("a/availability/edit/{id}")
    public String disponibilidadEdit(@ModelAttribute("disp") TeacherAvailability teacherAvailability,
                                     Model entity, @PathVariable("id") Integer id, HttpSession session,
                                     RedirectAttributes attributes){
        User user = (User)session.getAttribute("usuario");
        Optional<TeacherAvailability> optionalDisponibilidadProfesor =
                teacherAvailabilityRepository.findDisponibilidadProfesorByIdAndUsuario_Idusuario(id,user.getIdUser());


        if (optionalDisponibilidadProfesor.isPresent()){
            teacherAvailability = optionalDisponibilidadProfesor.get();
            entity.addAttribute("dias", DAYS);
            entity.addAttribute("disp", teacherAvailability);
            entity.addAttribute("titleForm","Editar Disponibilidad");
            return "teach/disponibilidad/form";
        }
        else {
            attributes.addFlashAttribute("msgError","No se encontro la disponibilidad");
        }

        return "redirect:/a/availability";
    }


    @PostMapping("a/availability/save")
    public String disponibilidadSave(@ModelAttribute("disp") TeacherAvailability teacherAvailability,
                                     BindingResult bindingResult, Model entity, HttpSession session, RedirectAttributes attributes){
        User user = (User)session.getAttribute("usuario");

        if (teacherAvailability.getStart()!=null && teacherAvailability.getEnd()!=null
                && teacherAvailability.getStart().isAfter(teacherAvailability.getEnd()))
            bindingResult.rejectValue("fin", "error.user", "Elija una fecha de fin válida");

        if (teacherAvailability.getId()==0 && !teacherAvailabilityRepository.findDisponibilidadProfesorsByUsuario_IdusuarioAndDia
                (user.getIdUser(), teacherAvailability.getDay()).isEmpty())
            bindingResult.rejectValue("dia","error.user","Este dia no puede tener más de un registro de disponibilidad");

        if (teacherAvailability.getId()!=0 && !teacherAvailabilityRepository.findDisponibilidadProfesorsByUsuario_IdusuarioAndDia
                (user.getIdUser(), teacherAvailability.getDay()).isEmpty()
        && !teacherAvailabilityRepository.findDisponibilidadProfesorsByIdAndUsuario_IdusuarioAndDia
                (teacherAvailability.getId(), user.getIdUser(), teacherAvailability.getDay()).isPresent())
            bindingResult.rejectValue("dia","error.user","Este dia no puede tener más de un registro de disponibilidad");


        if (!bindingResult.hasErrors()){
            if (teacherAvailability.getId()==0){ //Si es nuevo
                teacherAvailability.setUser(user);
                teacherAvailability.setBusy(false);
                attributes.addFlashAttribute("msgSuccess","Disponibilidad creada correctamente");
            }else {
                Optional<TeacherAvailability> optionalDisponibilidadProfesor =
                        teacherAvailabilityRepository.findDisponibilidadProfesorByIdAndUsuario_Idusuario
                                (teacherAvailability.getId(), user.getIdUser());
                if (!optionalDisponibilidadProfesor.isPresent()){
                    attributes.addFlashAttribute("msgError", "Hubo un error");
                    return "redirect:/a/availability";
                }
                else {
                    TeacherAvailability dispTemp = optionalDisponibilidadProfesor.get();
                    dispTemp.setDay(teacherAvailability.getDay());
                    dispTemp.setStart(teacherAvailability.getStart());
                    dispTemp.setEnd(teacherAvailability.getEnd());
                    teacherAvailability = dispTemp;
                }
                attributes.addFlashAttribute("msgSuccess","Disponibilidad actualizada correctamente");
            }
            teacherAvailabilityRepository.save(teacherAvailability);

        }else {
            entity.addAttribute("dias", DAYS);
            entity.addAttribute("disp", teacherAvailability);
            entity.addAttribute("titleForm","Formulario");
            return "teach/disponibilidad/form";
        }
        return "redirect:/a/availability";
    }


    @GetMapping("a/availability/delete/{id}")
    public String disponibilidadDelete(Model entity, @PathVariable("id") Integer id, HttpSession session,
                                       RedirectAttributes attributes){
        User user = (User)session.getAttribute("usuario");
        Optional<TeacherAvailability> optionalDisponibilidadProfesor =
                teacherAvailabilityRepository.findDisponibilidadProfesorByIdAndUsuario_Idusuario(id,user.getIdUser());
        if (optionalDisponibilidadProfesor.isPresent()){
            teacherAvailabilityRepository.delete(optionalDisponibilidadProfesor.get());
            attributes.addFlashAttribute("msgSuccess","Disponibilidad borrada correctamente");
        }
        else {
            attributes.addFlashAttribute("msgError","No se encontro la disponibilidad");
        }

        return "redirect:/a/availability";
    }






    // ------------------------------------------------------- WEB SERVICES -----------------------------------------------------

    @ResponseBody
    @GetMapping(value = "/dev/disponibilidadAsesor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TeacherAvailability>> getDisp(@PathVariable("id") int id){
        return new ResponseEntity<>(teacherAvailabilityRepository
                .findDisponibilidadProfesorsByUsuario_IdusuarioAndUsuario_ActivoIsTrueAndUsuario_Rol_Idrol(id, ROL_ASESOR), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping(value = "/dev/disponibilidadAsesor/a/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> getMap(@PathVariable("id") int id){
        Optional<User> optionalUsuario = userRepository.findByIdusuarioAndRol_IdrolAndActivoIsTrue(id,ROL_ASESOR);
        List<Map<String, Object>> list = new ArrayList<>();
        Map<LocalDate,ArrayList<LocalTime>> map = new HashMap<>();
        ArrayList<LocalDate> dtProgramado = new ArrayList<>();
        optionalUsuario.ifPresent(user -> enrollmentRepository.
                findClaseEnrollsByClase_Profesor_IdusuarioAndClase_ServicioAndClaseDisponibleIsTrueAndInicioasesoriaIsAfter(
                        user.getIdUser(), SERVICIO_ASESORIA_PERSONALIZADA, LocalDateTime.now().minusHours(5))
                .forEach((claseEnroll1) -> {
                    dtProgramado.add(claseEnroll1.getStart().toLocalDate());
                }));
        final int[] i = {1};
        if(optionalUsuario.isPresent()){
            User teach = optionalUsuario.get();
                for (LocalDateTime dp : teach.mapAvailability().keySet()){
                if (!dtProgramado.contains(dp.toLocalDate())){
                    list.add(new HashMap<String,Object>(){{
                        put("start",dp);
                        put("end",dp.plusHours(1));
                        put("text","Disponible");
                        put("id", Integer.toString(i[0]++));

                        if (!map.containsKey(dp.toLocalDate())) map.put(dp.toLocalDate(), new ArrayList<>());
                        map.get(dp.toLocalDate()).add(dp.toLocalTime());
                    }});
                }
            }
        }
        return new ResponseEntity<>(new HashMap<String, Object>(){{
            put("cal", list);
            put("select", new HashMap<LocalDate,ArrayList<LocalTime>>(){{
                ArrayList<LocalDate> newAL = new ArrayList<>(map.keySet());
                Collections.sort(newAL);
                for (LocalDate ld : newAL){
                    put(ld, map.get(ld));
                    Collections.sort(map.get(ld));
                }
            }});
        }},HttpStatus.OK);
    }
*/
}
