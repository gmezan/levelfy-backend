package com.uc.backend.entity;

import com.uc.backend.config.Auditable;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venta_cancelada")
public class SaleCanceled extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idventa_cancelada")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idestudiante", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "idprofesor", nullable = false)
    private User teacher;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "idcurso", nullable = false, referencedColumnName = "idcurso"),
            @JoinColumn(name = "universidad", nullable = false, referencedColumnName = "universidad")
    })
    private Course course;

    @Column(name = "servicio", nullable = false)
    private String serviceType;

    @Column(nullable = false, name = "fecha_pago")
    private LocalDateTime paymentDateTime;

    @Column(name = "monto", nullable = false)
    private BigDecimal amount;

    @Column(name = "mensaje")
    private String message;

    @Column(name = "metodo")
    private String method;

    @Column(nullable = false, name = "venta_creation")
    private LocalDateTime dateTimeSaleCreation;

    @Column(name = "resuelto")
    private Boolean solved;


    public SaleCanceled(){}

    public SaleCanceled(Sale sale){
        this.student = sale.getEnrollment().getStudent();
        this.teacher = sale.getEnrollment().getService().getTeacher();
        this.course = sale.getEnrollment().getService().getCourse();
        this.serviceType = sale.getEnrollment().getService().getServiceType();
        this.paymentDateTime = sale.getSaleDateTime();
        this.amount = sale.getAmount();
        this.message = sale.getMessage();
        this.method = sale.getMethod();
        this.solved = Boolean.FALSE;
        this.dateTimeSaleCreation = sale.getCreated();
    }

    public static List<SaleCanceled> generateVentaCanceladas(List<Sale> sales){
        return new ArrayList<SaleCanceled>(){{
            sales.forEach(sale -> add(new SaleCanceled(sale)));
        }};
    }


    public Boolean getSolved() {
        return solved;
    }

    public void setSolved(Boolean resuelto) {
        this.solved = resuelto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User estudiante) {
        this.student = estudiante;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User profesor) {
        this.teacher = profesor;
    }

    public Course getCurso() {
        return course;
    }

    public void setCurso(Course course) {
        this.course = course;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String servicio) {
        this.serviceType = servicio;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setPaymentDateTime(LocalDateTime fechaDePago) {
        this.paymentDateTime = fechaDePago;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal monto) {
        this.amount = monto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mensaje) {
        this.message = mensaje;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String metodo) {
        this.method = metodo;
    }

    public LocalDateTime getDateTimeSaleCreation() {
        return dateTimeSaleCreation;
    }

    public void setDateTimeSaleCreation(LocalDateTime dateTimeVentaCreation) {
        this.dateTimeSaleCreation = dateTimeVentaCreation;
    }
}
