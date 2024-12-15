package ma.ensaj.GestionSurveillance.entities;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
@Entity
@Data
@Table(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long session_id;

    private String type;
    private LocalDate startDate;

    private String debutMatin1;
    private String finMatin1;
    private String debutMatin2;
    private String finMatin2;
    private String debutSoir1;
    private String finSoir1;
    private String debutSoir2;
    private String finSoir2;

    public Long getSession_id() {
        return session_id;
    }

    public void setSession_id(Long session_id) {
        this.session_id = session_id;
    }

    public String getFinSoir2() {
        return finSoir2;
    }

    public void setFinSoir2(String finSoir2) {
        this.finSoir2 = finSoir2;
    }

    public String getDebutSoir2() {
        return debutSoir2;
    }

    public void setDebutSoir2(String debutSoir2) {
        this.debutSoir2 = debutSoir2;
    }

    public String getFinSoir1() {
        return finSoir1;
    }

    public void setFinSoir1(String finSoir1) {
        this.finSoir1 = finSoir1;
    }

    public String getDebutSoir1() {
        return debutSoir1;
    }

    public void setDebutSoir1(String debutSoir1) {
        this.debutSoir1 = debutSoir1;
    }

    public String getFinMatin2() {
        return finMatin2;
    }

    public void setFinMatin2(String finMatin2) {
        this.finMatin2 = finMatin2;
    }

    public String getDebutMatin2() {
        return debutMatin2;
    }

    public void setDebutMatin2(String debutMatin2) {
        this.debutMatin2 = debutMatin2;
    }

    public String getFinMatin1() {
        return finMatin1;
    }

    public void setFinMatin1(String finMatin1) {
        this.finMatin1 = finMatin1;
    }

    public String getDebutMatin1() {
        return debutMatin1;
    }

    public void setDebutMatin1(String debutMatin1) {
        this.debutMatin1 = debutMatin1;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
