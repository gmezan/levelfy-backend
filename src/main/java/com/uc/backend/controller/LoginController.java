package com.uc.backend.controller;

import org.springframework.stereotype.Controller;

public class LoginController {

    /*

    private static String authorizationRequestBaseUri = "oauth2/authorization";

    private Map<String, String> oauth2AuthenticationUrls = new HashMap<>();

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    CustomEmailService emailService;


    @GetMapping("/loginForm")
    public String loginForm(Model entity,@RequestParam(value = "codigo",required = false) Integer idinvitante ,Authentication auth, HttpServletRequest request, RedirectAttributes attr, HttpSession session){
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }


        clientRegistrations.forEach(registration -> oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        entity.addAttribute("urls", oauth2AuthenticationUrls);

        if(auth!=null)
            return redirectUser(request,auth.getAuthorities());
        if (idinvitante!=null && idinvitante!=0){
session.setAttribute("idinvitante", idinvitante);

        }

        Integer id=(Integer) session.getAttribute("idinvitante");
        System.out.println("Hola sí funcionas"+id);

        return "login/login";

    }

    @GetMapping("/loginSuccess")
    public String getLoginInfo(HttpServletRequest request, OAuth2AuthenticationToken authentication,
                               HttpSession session, RedirectAttributes attr) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        String userInfoEndpointUri = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();

        if (!StringUtils.isEmpty(userInfoEndpointUri)) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                    .getTokenValue());
            HttpEntity entity = new HttpEntity("", headers);
            ResponseEntity<Map> response = restTemplate
                    .exchange(userInfoEndpointUri, HttpMethod.GET, entity, Map.class);
            Map userAttributes = response.getBody();
            Usuario usuario = usuarioRepository.findByCorreo((String)userAttributes.get("email"));
            if (usuario==null){
                usuario = new Usuario();
                usuario.setCorreo((String)userAttributes.get("email"));
                usuario.formatGoogleName((String)userAttributes.get("name"));
                usuario.setActivo(true);
                Integer id=(Integer) session.getAttribute("idinvitante");
               try{ if (id != 0){
                usuario.setIdinvitante(id);
                }}catch (NullPointerException e){
                   usuario.setIdinvitante(0);
               }
                usuario.setRol(new Rol(1));
                usuarioRepository.save(usuario);
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(usuario.getRol().getNombre()));
            Authentication auth =  new UsernamePasswordAuthenticationToken(usuario.getCorreo(), usuario.getPassword(), authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            session.setAttribute("usuario", usuario);
            return redirectUser(request,auth.getAuthorities());
        }

        return "/loginForm?error";
    }

    private String redirectUser(HttpServletRequest request, Collection<? extends GrantedAuthority> auths){
        String rol = "";
        for (GrantedAuthority role : auths) {
            rol = role.getAuthority();
            break;
        }
        switch (rol) {
            case "admin":
                return "redirect:/admin";
            case "teach":
                return "redirect:/a";
            case "client":
                return "redirect:/home";
            default:
                try{
                    SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
                    request.logout();
                }
                catch (ServletException e) {
                    e.printStackTrace();
                }
                return "redirect:/loginForm";
        }
    }

    @GetMapping("/redirectByRole")
    public String redirectByRole(Authentication auth, HttpSession session){
        String rol = "";
        for (GrantedAuthority role : auth.getAuthorities()){
            rol = role.getAuthority();
            break;
        }

        Usuario usuarioLogueado =  usuarioRepository.findByCorreo(auth.getName());
        session.setAttribute("usuario", usuarioLogueado);

        switch (rol) {
            case "admin":
                return "redirect:/home";
            case "teach":
                return "redirect:/a";
            case "client":
                return "redirect:/home";
            default:
                return "redirect:/";
        }
    }

    @GetMapping("/signup")
    public String signup(Model entity){

        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));
        entity.addAttribute("urls", oauth2AuthenticationUrls);
        return "login/signup";
    }

    @PostMapping("/processSignup")
    public String processSignup(Usuario usuario, RedirectAttributes redirectAttributes){
        usuario.encrypt();
        usuario.setRol(new Rol(1));
        usuarioRepository.save(usuario);
        Optional<Usuario> optionalUsuario =
                usuarioRepository.findById(usuario.getIdusuario());
        if(optionalUsuario.isPresent()){
            String link = "https://www.myuniversityclass.com/signup/confirmationAccountUniversityClass?activar="+usuario.getIdusuario();
            String mensaje="Te haz registrado exitosamente a University Class. Accede al siguiente link para activar tu cuenta: " + link;
            emailService.sendSimpleMessage(usuario.getCorreo(), "Registro exitoso", mensaje);
            redirectAttributes.addFlashAttribute("msg","Registro exitoso se  te ha enviado un correo de confirmación ");
            return "login/confirmacion";
        }
       return "/home";
    }

    @GetMapping("/forgotPassword")
    public String forgotPassword(){ return "login/forgot_password";}

    @PostMapping("/processForgotPassword")
    public String processForgotPassword(Model entity, @RequestParam(value = "username", required = true) String email){
        System.out.println(email);
        entity.addAttribute("msg", email);
        return "login/confirmationForgotPswd";
    }

    @GetMapping("/about")
    public String about(){ return "login/about";}

    @GetMapping("/contact")
    public String contact(){ return "login/contact";}
    @GetMapping(value = {"/TyC"}) public String mostrarPoliticas(){ return "login/tyc"; }

*/

}
