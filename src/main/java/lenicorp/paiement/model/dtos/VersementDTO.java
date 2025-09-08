package lenicorp.paiement.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VersementDTO
{
    private Long versementId;
    private String codeVersement;
    private LocalDate dateVersement;
    private BigDecimal montant;
    private String montantLettre;
    private boolean active;
    private String modePaiementCode;
    private String modePaiement;
    private String typePaiementCode;
    private String typePaiement;
    private Long adhesionId;
    private String userId;
    private String firstName;
    private String lastName;
    private String matricule;
    private String email;
    private String tel;
    private String nomCotisation;
    private String motif;
    private Long assoId;
    private String assoName;
    private String assoSigle;
    private String membre;
    private String periodes;

    public VersementDTO(Long versementId, String codeVersement, LocalDate dateVersement, BigDecimal montant, String montantLettre,
                        String modePaiementCode, String modePaiement, String typePaiementCode, String typePaiement,
                        Long adhesionId, String userId, Long cotisationId, String nomCotisation, String motif,
                        Long assoId, String assoName, String assoSigle)
    {
        this.versementId = versementId;
        this.codeVersement = codeVersement;
        this.dateVersement = dateVersement;
        this.montant = montant;
        this.montantLettre = montantLettre;
        this.modePaiementCode = modePaiementCode;
        this.modePaiement = modePaiement;
        this.typePaiementCode = typePaiementCode;
        this.typePaiement = typePaiement;
        this.adhesionId = adhesionId;
        this.userId = userId;
        this.nomCotisation = nomCotisation;
        this.motif = motif;
        this.assoId = assoId;
        this.assoName = assoName;
        this.assoSigle = assoSigle;
    }

    public VersementDTO(Long versementId, String codeVersement, LocalDate dateVersement,
                        BigDecimal montant, String montantLettre, String modePaiementCode,
                        String modePaiement, String firstName, String lastName, String matricule, String email, String tel) {
        this.versementId = versementId;
        this.codeVersement = codeVersement;
        this.dateVersement = dateVersement;
        this.montant = montant;
        this.montantLettre = montantLettre;
        this.modePaiementCode = modePaiementCode;
        this.modePaiement = modePaiement;
        this.firstName = firstName;
        this.lastName = lastName;
        this.matricule = matricule;
        this.email = email;
        this.tel = tel;
    }

    public VersementDTO(Long versementId, String codeVersement, LocalDate dateVersement, BigDecimal montant, String montantLettre, String modePaiement, String firstName, String lastName, String matricule, String email, String tel, String nomCotisation, String motif, Long assoId, String assoName, String assoSigle) {
        this.versementId = versementId;
        this.codeVersement = codeVersement;
        this.dateVersement = dateVersement;
        this.montant = montant;
        this.montantLettre = montantLettre;
        this.modePaiement = modePaiement;
        this.firstName = firstName;
        this.lastName = lastName;
        this.matricule = matricule;
        this.email = email;
        this.tel = tel;
        this.nomCotisation = nomCotisation;
        this.motif = motif;
        this.assoId = assoId;
        this.assoName = assoName;
        this.assoSigle = assoSigle;
    }
}
