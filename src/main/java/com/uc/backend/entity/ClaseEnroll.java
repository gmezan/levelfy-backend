package com.uc.backend.entity;

import com.uc.backend.config.Auditable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import static com.uc.backend.utils.CustomConstants.MINUTES_EXPIRATION_TIME_FOR_ASES_PER;

@Entity
@Table(name = "clase_enroll")
public class ClaseEnroll extends Auditable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idclase_enroll")
    private int idClaseEnroll;

    @ManyToOne
    @JoinColumn(name = "idclase", nullable = false)
    private Clase clase;

    @ManyToOne
    @JoinColumn(name = "idalumno", nullable = false)
    private Usuario estudiante;

    @Column(name = "pago_confirmado",nullable = false)
    private Boolean pagado;

    //Atributos necesarios para asesorías personalizadas:

    @Column(name = "cantidad_persona")
    private Integer cantidad;

    @Column(name = "ases_per_inicio")
    private LocalDateTime inicioasesoria;

    @Column(name = "ases_per_fin")
    private LocalDateTime finasesoria;

    @Column(name = "info")
    private String info;

    @Column(name = "activo")
    private Boolean active;

    @Column(name = "conference_url")
    private String url;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "claseEnroll")
    private List<Venta> ventaList;


    //Transient attributes:
    @Transient
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate diaClase;

    @Transient
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime horaClase;

    @Transient
    private Integer numHoras;

    public LocalDate getDiaClase() {
        return diaClase;
    }

    public void setDiaClase(LocalDate diaClase) {
        this.diaClase = diaClase;
    }

    public LocalTime getHoraClase() {
        return horaClase;
    }

    public void setHoraClase(LocalTime horaClase) {
        this.horaClase = horaClase;
    }

    public Integer getNumHoras() {
        return numHoras;
    }

    public void setNumHoras(Integer numHoras) {
        this.numHoras = numHoras;
    }

    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    public String getStringDate(){//no necesita convertir a GMT+5
        return this.inicioasesoria.toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    public String getExpirationDateString(){
        return super.fechacreacion.toLocalTime().minusHours(5).plusMinutes(MINUTES_EXPIRATION_TIME_FOR_ASES_PER).
                format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
    }

    public String getCreationDateGMTp5(){
        return this.fechacreacion.minusHours(5).toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public boolean validateCant(){
        return (this.cantidad>0 && this.cantidad<5);
    }

    public LocalDateTime getFinasesoria() {
        return finasesoria;
    }

    public void setFinasesoria(LocalDateTime finasesoria) {
        this.finasesoria = finasesoria;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public LocalDateTime getInicioasesoria() {
        return inicioasesoria;
    }

    public void setInicioasesoria(LocalDateTime inicioasesoria) {
        this.inicioasesoria = inicioasesoria;
    }

    public int getIdClaseEnroll() {
        return idClaseEnroll;
    }

    public void setIdClaseEnroll(int idClaseEnroll) {
        this.idClaseEnroll = idClaseEnroll;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
