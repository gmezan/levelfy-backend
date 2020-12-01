package com.uc.backend.entity;


import com.uc.backend.config.Auditable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;


@Entity
@Table(name = "usuario")
public class Usuario extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private int idusuario;

    @Column(name = "correo", nullable = false)
    private String correo;

    @Column(name = "celular")
    private Integer celular=0;

    @Column(name = "saldo")
    private BigDecimal saldo;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(nullable = false, name = "apellido")
    private String apellido;

    @Column(name="cupon")
    private String cupon;

    @Column(name = "foto_url")
    private String foto="";

    @Column(name = "estado")
    private boolean activo = false;

    @Column(name = "contrasena", nullable = true)
    private String password;

    @Column(name = "universidad")
    private String universidad="";

    @Column(name = "token")
    private String token;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fdc;

    @ManyToOne
    @JoinColumn(name = "idrol")
    private Rol rol;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "usuario")
    private Set<DisponibilidadProfesor> disponibilidadProfesorSet;

    @Column(name = "idinvitante")
    private Integer idinvitante;

    public Set<DisponibilidadProfesor> getDisponibilidadProfesorSet() {

        return disponibilidadProfesorSet;
    }

    public List<DisponibilidadProfesor> getDisponibilidadProfesorList() {
        List<DisponibilidadProfesor> list = new ArrayList<>(disponibilidadProfesorSet);
        Collections.sort(list);
        return list;
    }

    public void setDisponibilidadProfesorSet(Set<DisponibilidadProfesor> disponibilidadProfesorSet) {
        this.disponibilidadProfesorSet = disponibilidadProfesorSet; }

    //Retornar arreglo con String y LocalDateTime para cada disponibilidad
    public Map<LocalDateTime, String> mapDisponibilidad(){

        int horasDeAnticipacion = 4;
        int DAYS = 7;

        LocalDateTime now = LocalDateTime.now().minusHours(5).plusHours(horasDeAnticipacion); // A hora de peru
        LocalDate dateNow = now.toLocalDate();
        List<LocalDate> dias = new ArrayList<>();
        Map<Integer, List<LocalTime>> map = new HashMap<>();

        for (DisponibilidadProfesor disp : disponibilidadProfesorSet){
            if (!map.containsKey(disp.getDia())) map.put(disp.getDia(), new ArrayList<>());
            map.get(disp.getDia()).addAll(disp.splitTimes());
        }
        LocalDate date;
        LocalDateTime ldt;
        Map<LocalDateTime, String> mapD = new HashMap<>();
        for(int i = 0; i < DAYS; i++){
            dias.add(dateNow.plusDays(i));
            if(map.containsKey(dias.get(i).getDayOfWeek().getValue())){
                for(LocalTime localTime : map.get(dias.get(i).getDayOfWeek().getValue())){
                    date = dias.get(i);
                    ldt = LocalDateTime.of(date, localTime);
                    if (now.isBefore(ldt)){
                        mapD.put(ldt,
                                date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)).split(",")[0] + "  -  "
                                        + localTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
                    }
                }
            }
        }

        return mapD;
    }

    public void formatGoogleName(String fn){
        StringBuilder name= new StringBuilder();
        for (String x : fn.toLowerCase().trim().split(" "))
            name.append(x.substring(0, 1).toUpperCase()).append(x.substring(1)).append(" ");

        this.nombre = name.toString().trim();
        this.apellido = "";
    }

    public String getFullName(){
        return this.nombre + ' ' + this.apellido;
    }


    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getCelular() {
        return celular;
    }

    public void setCelular(Integer celular) {
        this.celular = celular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getFdc() {
        return fdc;
    }

    public void setFdc(LocalDate fdc) {
        this.fdc = fdc;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Integer getIdinvitante() {
        return idinvitante;
    }

    public void setIdinvitante(Integer idinvitante) {
        this.idinvitante = idinvitante;
    }

    public String getCupon() {
        return cupon;
    }

    public void setCupon(String cupon) {
        this.cupon = cupon;
    }
}
