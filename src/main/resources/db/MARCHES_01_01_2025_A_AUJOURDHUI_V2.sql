SELECT  marc.R_MARCHE                 AS  MAR_CODE,      --  Num�ro de march�
        marc.L_MARCHE                 AS  MAR_OBJET,     --  Objet du march�
        marc.DATE_CREATION            AS  MAR_DTE_SAISI, --  Date de saisie du march� (cr�ation)
        sect.r_section || ' - ' || sect.L_SECTION AS  MIN_LIBELLE_LNG, --  Libell� du Minist�re de l'autorit� contractante
        CASE COALESCE(parent.ID_SYSTEME, marc.ID_SYSTEME)
            WHEN 'SIGMAP' THEN juri_2.DESCRIPTION
            ELSE lign.R_LIGNE
        END                       AS  IMPUTATION,      --  Imputation budg�taire
        lide.dotation,
        kde.tresor,
        kde.don,
        kde.emprunt,
        kde.ress_propre,
        kde.excedent_bud,
        kde.bailleurs,
        marc.MONTANT_TTC                AS  MAR_MT_TOT_MAR,
        COALESCE(mhde.MONTANT_TTC, marc.MONTANT_TTC)  AS  MAR_MT_MAR,
        CASE
            WHEN  marc.MONTANT_TVA = 0  THEN 0
            WHEN  marc.MONTANT_HT = 0   THEN 0
            ELSE  ROUND(marc.MONTANT_TVA / marc.MONTANT_HT * 100)
        END                       AS  MAR_TVA,       --  % de TVA
        tier.R_TIERS                  AS  NCC,         --  Num�ro de compte contribuable du titulaire du march�
        tier.L_TIERS                  AS  TITULAIRE,     --  Raison sociale du titulaire (NOM de l'Entreprise)
        unit.R_UNITE                  AS  CODE_AC,       --  Code de l'Autorit� Contractante
        unit.L_UNITE                  AS  LIBELLE_AC,      --  Libell� de l'AC (autorit� Contractante)
        unty.L_UNITE_TYPE             AS  TYPE_STRUCTURE,    --  Type de Structure
        opty.R_OPERATION_TYPE         AS  MODE_PASS,     --  Mode de Passation
        opng.R_OPERATION_NATURE_GROUPE AS  CODE_TYPE_MARCHE,  --  Code type de march�
        opng.L_OPERATION_NATURE_GROUPE AS  LIBELLE_TYPE_MARCHE, --  Libell� du type de march�
        parent.R_MARCHE               AS  MAR_CODE,      --  Num�ro de march� de base de l'avenant (si le march� est un avenant)
        init.MONTANT_TTC              AS  MAR_MT_MAR,      --  Montant du march� de base dont est issu l'avenant
        CASE
            WHEN parent.ID_MARCHE IS NULL THEN NULL
            WHEN parent.ID_SYSTEME = 'SIGMAP' THEN juri_2.DESCRIPTION
            ELSE lign.R_LIGNE
        END                       AS  MAR_LBG_IMPUTATION,  --  Imputation budg�taire du march� de base de l'avenant (si le march� est un avenant)
        MAST.L_STATUT_PARAM
FROM    T_MARCHE marc,
        T_MARCHE_DETAIL mhde,
        T_MARCHE parent,
        T_MARCHE_DETAIL init,
        T_TIERS tier,
        T_UNITE unit,
        T_UNITE_TYPE unty,
        T_SECTION sect,
        t_statut_param MAST,
        T_OPERATION_TYPE opty,
        T_OPERATION_NATURE opna,
        T_OPERATION_NATURE_GROUPE opng,
        (SELECT juri.ID_MARCHE,
                MAX(jude.ID_JURIDIQUE) AS ID_JURIDIQUE,
                MIN(juri.ID_EXERCICE) AS GESTION,
                MAX(ligd.ID_LIGNE) AS ID_LIGNE
         FROM T_JURIDIQUE juri,
              T_JURIDIQUE_DETAIL jude,
              T_DEPENSE depe,
              T_LIGNE_DETAIL ligd
         WHERE juri.ID_JURIDIQUE = jude.ID_JURIDIQUE
           AND jude.ID_DEPENSE = depe.ID_DEPENSE
           AND depe.ID_LIGNE_DETAIL = ligd.ID_LIGNE_DETAIL
         GROUP BY juri.ID_MARCHE) juri,
        T_JURIDIQUE juri_2,
        T_LIGNE lign,
        (SELECT ld.id_ligne Id_ligne, SUM(ld.montant_ae) dotation
         FROM t_ligne_detail ld
         GROUP BY ld.id_ligne) lide,
        (SELECT t.id_ligne,
                b.id_exercice,
                SUM(CASE WHEN f.id_financement_source = 1 THEN p.montant_ae ELSE 0 END) TRESOR,
                SUM(CASE WHEN f.id_financement_source = 2 THEN p.montant_ae ELSE 0 END) DON,
                SUM(CASE WHEN f.id_financement_source = 3 THEN p.montant_ae ELSE 0 END) EMPRUNT,
                SUM(CASE WHEN f.id_financement_source = 4 THEN p.montant_ae ELSE 0 END) RESS_PROPRE,
                SUM(CASE WHEN f.id_financement_source = 5 THEN p.montant_ae ELSE 0 END) EXCEDENT_BUD,
                MAX(k.Bailleurs) Bailleurs
         FROM UT_SIGOMAP.T_LIGNE t,
              UT_SIGOMAP.T_LIGNE_DETAIL p,
              UT_SIGOMAP.t_financement_source f,
              UT_SIGOMAP.t_budget b,
              (SELECT d.id_ligne,
                      LISTAGG(b.l_bailleur, ', ') WITHIN GROUP (ORDER BY b.l_bailleur) Bailleurs
               FROM UT_SIGOMAP.t_bailleur b,
                    (SELECT DISTINCT id_ligne, id_bailleur FROM t_ligne_detail) d
               WHERE d.id_bailleur = b.id_bailleur
               GROUP BY d.id_ligne) k
         WHERE p.id_ligne = t.id_ligne
           AND f.id_financement_source = p.id_financement_source
           AND b.id_budget = t.id_budget
           AND p.id_ligne = k.id_ligne(+)
           AND b.id_exercice = 2024
         GROUP BY t.id_ligne, b.id_exercice) kde
WHERE   marc.R_MARCHE = mhde.R_MARCHE_DETAIL(+)
  AND   mhde.ID_MARCHE = parent.ID_MARCHE(+)
  AND   mhde.ID_MARCHE_DETAIL_TYPE(+) <> 'INI'
  AND   parent.R_MARCHE = init.R_MARCHE_DETAIL(+)
  AND   init.ID_MARCHE_DETAIL_TYPE(+) = 'INI'
  AND   marc.ID_TIERS = tier.ID_TIERS
  AND   marc.ID_UNITE = unit.ID_UNITE
  AND   unit.ID_UNITE_TYPE = unty.ID_UNITE_TYPE
  AND   unit.ID_SECTION = sect.ID_SECTION
  AND   MARC.ID_STATUT = MAST.ID_STATUT
  AND   mast.id_table = 'T_MARCHE'
  AND   COALESCE(marc.ID_OPERATION_TYPE, parent.ID_OPERATION_TYPE) = opty.ID_OPERATION_TYPE(+)
  AND   COALESCE(marc.ID_OPERATION_NATURE, parent.ID_OPERATION_NATURE) = opna.ID_OPERATION_NATURE(+)
  AND   opna.ID_OPERATION_NATURE_GROUPE = opng.ID_OPERATION_NATURE_GROUPE(+)
  AND   COALESCE(parent.ID_MARCHE, marc.ID_MARCHE) = juri.ID_MARCHE(+)
  AND   juri.ID_JURIDIQUE = juri_2.ID_JURIDIQUE(+)
  AND   juri_2.ID_SYSTEME(+) = 'SIGMAP'
  AND   juri.ID_LIGNE = lign.ID_LIGNE(+)
  AND   lign.id_ligne = lide.id_ligne(+)
  AND   lign.id_ligne = kde.id_ligne(+)
  AND   marc.ID_STATUT IN ('APP', 'DEM', 'AVA', 'SIG', 'CON')
  AND   marc.DATE_CREATION >= TO_DATE('2025-01-01', 'YYYY-MM-DD')
ORDER BY sect.r_section, unit.R_UNITE, marc.DATE_CREATION ASC;
