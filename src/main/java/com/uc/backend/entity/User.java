package com.uc.backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
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
public class User extends Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private int idUser;

    @NotNull
    @Column(name = "correo", nullable = false)
    private String email;

    @Column(name = "celular")
    private Integer phone = 0;

    @Column(name = "saldo")
    private BigDecimal balance;

    @Column(name = "codigo")
    private String code;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(nullable = false, name = "apellido")
    private String lastname;

    @Column(name="cupon")
    private String coupon;

    @Column(name = "foto_url")
    private String photo ="";

    @Column(name = "estado")
    private boolean active = false;

    @JsonIgnore
    @Column(name = "contrasena", nullable = true)
    private String password;

    @Column(name = "universidad")
    private String university ="";

    @Column(name = "token")
    private String token;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_has_rol",
            joinColumns = @JoinColumn(name = "idusuario", referencedColumnName = "idusuario") ,
            inverseJoinColumns = @JoinColumn(name = "idrol", referencedColumnName = "idrol"))
    private Set<Role> role = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<TeacherAvailability> teacherAvailabilitySet;

    @Column(name = "idinvitante")
    private Integer invitingId;

    @JsonIgnore
    public Set<TeacherAvailability> getTeacherAvailabilitySet() {

        return teacherAvailabilitySet;
    }

    @JsonIgnore
    public List<TeacherAvailability> getTeacherAvailabilityList() {
        List<TeacherAvailability> list = new ArrayList<>(teacherAvailabilitySet);
        Collections.sort(list);
        return list;
    }

    public void setTeacherAvailabilitySet(Set<TeacherAvailability> teacherAvailabilitySet) {
        this.teacherAvailabilitySet = teacherAvailabilitySet; }

    // returns string array & LocalDateTime for each availability
    public Map<LocalDateTime, String> mapAvailability(){

        int anticipationHours = 4;
        int DAYS = 7;

        LocalDateTime now = LocalDateTime.now().minusHours(5).plusHours(anticipationHours); // A hora de peru
        LocalDate dateNow = now.toLocalDate();
        List<LocalDate> dias = new ArrayList<>();
        Map<Integer, List<LocalTime>> map = new HashMap<>();

        for (TeacherAvailability availability : teacherAvailabilitySet){
            if (!map.containsKey(availability.getDay())) map.put(availability.getDay(), new ArrayList<>());
            map.get(availability.getDay()).addAll(availability.splitTimes());
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

        this.name = name.toString().trim();
        this.lastname = "";
    }

    public String getFullName(){
        return this.name + ' ' + this.lastname;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Integer getInvitingId() {
        return invitingId;
    }

    public void setInvitingId(Integer invitingId) {
        this.invitingId = invitingId;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
}
